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
                // 找到子菜单
                .filter((category) -> Objects.equals(category.getParentCid(), root.getCatId()))
                // 继续找子菜单
                .peek((item) -> item.setChildren(getChildren(item, categoryList)))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .collect(Collectors.toList());
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        // TODO 检查菜单在其他地方是否被引用
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

            // TODO 更新级联的所有数据
        }
    }

    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));

        return categoryEntities;
    }


    // 先从redis中查是否有缓存

    // TODO 会产生堆外内存溢出问题
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        // 加入缓存,缓存中存入的都是json字符串
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        Map<String, List<Catelog2Vo>> catalogJsonFromDb = null;
        // 缓存中没有，则查询数据库

        /*
        *  1、空结果缓存：解决穿透
        *  2、设置过期时间（加随机值）：解决雪崩问题
        *  3、加锁：解决击穿问题
        * */

        if (StringUtils.isEmpty(catalogJSON)){
            System.out.println("缓存不命中，查询数据库。。。。。。。。");
            catalogJsonFromDb = getCatalogJsonFromDbWithRedisLock();
        }
        else {
            System.out.println("缓存命中。。。。。直接返回");
            catalogJsonFromDb = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>(){});
        }

        return catalogJsonFromDb;
    }

    // 从数据查询并封装整个分类数据
    // 使用redis锁
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithRedisLock() {
        // 占分布式锁，去redis占坑
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid,300,TimeUnit.SECONDS);
        if (lock){
            System.out.println("获取分布式锁成功");

            // 加锁成功
            // 设置过期时间必须和加锁是同步的
            // redisTemplate.expire("lock",30,TimeUnit.SECONDS);
            Map<String, List<Catelog2Vo>> data;
            try {

                data = getDataFromDb();

            }finally {
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                redisTemplate.execute(new DefaultRedisScript<Long>(script,Long.class),Arrays.asList("lock"),uuid);
            }

            // 删除锁也必须是原子操作，lua脚本解锁
            // 非原子操作
//            String lockValue = redisTemplate.opsForValue().get("lock");
//            if (uuid.equals(lockValue)){
//                redisTemplate.delete("lock");// 删除锁
//            }
            return data;

        }
        else {
            System.out.println("获取分布式锁失败。。。等待重试");
            //加锁失败，重试
            // 休眠100ms
            try {
                Thread.sleep(200);
            }catch (Exception e){
                System.out.println(e);
            }
            return getCatalogJsonFromDbWithRedisLock();//自旋

        }

    }



    // 从数据查询并封装整个分类数据
    // 使用本地锁
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithLocalLock() {


        // 只要是同一把锁，就能锁住需要这个锁的所有线程
        // 本地锁：springboot中所有组件容器都是单例的
        // 但是springboot是分布式的，有很多服务器中不同的实例
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
        // 查出1级分类id
        List<CategoryEntity> level1Categorys = getParent_cid(entityList, 0L);
        Map<String, List<Catelog2Vo>> result =
                level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 查出二级分类list
            List<CategoryEntity> categoryEntities = getParent_cid(entityList, v.getCatId());
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    // 根据二级分类找三级分类
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
        System.out.println("查询了数据库");
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