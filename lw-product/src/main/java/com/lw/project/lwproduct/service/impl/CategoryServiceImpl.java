package com.lw.project.lwproduct.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lw.project.lwproduct.service.CategoryBrandRelationService;
import com.lw.project.lwproduct.vo.Catelog2Vo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lw.common.utils.PageUtils;
import com.lw.common.utils.Query;

import com.lw.project.lwproduct.dao.CategoryDao;
import com.lw.project.lwproduct.entity.CategoryEntity;
import com.lw.project.lwproduct.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {


    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> categoryList = baseMapper.selectList(null);
        return categoryList
                .stream()
                .filter((category) -> category.getParentCid() == 0)
                .peek((item)-> item.setChildren(getChildren(item,categoryList)))
                .sorted((category1,category2)-> category1.getSort()-category2.getSort())
                .collect(Collectors.toList());
    }

    public List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> categoryList){
        return categoryList
                .stream()
                // ???????????????
                .filter((category) -> Objects.equals(category.getParentCid(), root.getCatId()))
                // ??????????????????
                .peek((item) -> item.setChildren(getChildren(item, categoryList)))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .collect(Collectors.toList());
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        // TODO ??????????????????????????????????????????
        baseMapper.deleteBatchIds(asList);
    }


    @Override
    public Long[] findCategoryPath(Long catelogId){
        List<Long> categoryPath = new ArrayList<>();

        getParentIds(catelogId,categoryPath);
        Collections.reverse(categoryPath);
        return categoryPath.toArray(new Long[categoryPath.size()]) ;
    }

    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())){
            categoryBrandRelationService.saveCategory(category.getCatId(),category.getName());

            // TODO ???????????????????????????
        }
    }

    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));

        return categoryEntities;
    }


    // ??????redis?????????????????????

    // TODO ?????????????????????????????????
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        // ????????????,????????????????????????json?????????
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        Map<String, List<Catelog2Vo>> catalogJsonFromDb = null;
        // ????????????????????????????????????

        /*
        *  1?????????????????????????????????
        *  2????????????????????????????????????????????????????????????
        *  3??????????????????????????????
        * */

        if (StringUtils.isEmpty(catalogJSON)){
            System.out.println("?????????????????????????????????????????????????????????");
            catalogJsonFromDb = getCatalogJsonFromDbWithRedisLock();
        }
        else {
            System.out.println("???????????????????????????????????????");
            catalogJsonFromDb = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>(){});
        }

        return catalogJsonFromDb;
    }

    // ??????????????????????????????????????????
    // ??????redis???
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithRedisLock() {
        // ?????????????????????redis??????
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid,300,TimeUnit.SECONDS);
        if (lock){
            System.out.println("????????????????????????");

            // ????????????
            // ?????????????????????????????????????????????
            // redisTemplate.expire("lock",30,TimeUnit.SECONDS);
            Map<String, List<Catelog2Vo>> data;
            try {

                data = getDataFromDb();

            }finally {
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                redisTemplate.execute(new DefaultRedisScript<Long>(script,Long.class),Arrays.asList("lock"),uuid);
            }

            // ????????????????????????????????????lua????????????
            // ???????????????
//            String lockValue = redisTemplate.opsForValue().get("lock");
//            if (uuid.equals(lockValue)){
//                redisTemplate.delete("lock");// ?????????
//            }
            return data;

        }
        else {
            System.out.println("?????????????????????????????????????????????");
            //?????????????????????
            // ??????100ms
            try {
                Thread.sleep(200);
            }catch (Exception e){
                System.out.println(e);
            }
            return getCatalogJsonFromDbWithRedisLock();//??????

        }

    }



    // ??????????????????????????????????????????
    // ???????????????
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithLocalLock() {


        // ??????????????????????????????????????????????????????????????????
        // ????????????springboot????????????????????????????????????
        // ??????springboot??????????????????????????????????????????????????????
        synchronized (this) {
           return getDataFromDb();
        }
    }

    private Map<String, List<Catelog2Vo>> getDataFromDb() {
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (!StringUtils.isEmpty(catalogJSON)) {
            Map<String, List<Catelog2Vo>> data = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
            return data;
        }
        List<CategoryEntity> entityList = baseMapper.selectList(null);
        // ??????1?????????id
        List<CategoryEntity> level1Categorys = getParent_cid(entityList, 0L);
        Map<String, List<Catelog2Vo>> result =
                level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // ??????????????????list
            List<CategoryEntity> categoryEntities = getParent_cid(entityList, v.getCatId());
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    // ?????????????????????????????????
                    List<CategoryEntity> level3Catelog = getParent_cid(entityList, l2.getCatId());
                    if (level3Catelog != null) {
                        List<Catelog2Vo.Catelog3Vo> catelog3Vos = level3Catelog.stream().map(l3 -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());

                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catelog3Vos);

                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));
        String s = JSON.toJSONString(result);
        redisTemplate.opsForValue().set("catalogJSON",s,1, TimeUnit.DAYS);
        System.out.println("??????????????????");
        return result;
    }
    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parent_cid) {
        return selectList.stream().filter(item-> Objects.equals(item.getParentCid(), parent_cid)).collect(Collectors.toList());
    }

    public void getParentIds(Long catelogId, List<Long> categoryPath){
        categoryPath.add(catelogId);
        CategoryEntity category = this.getById(catelogId);
        if (category.getParentCid()!=0){
            getParentIds(category.getParentCid(),categoryPath);
        }
        return;
    }
}