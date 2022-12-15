package com.lw.project.lwproduct.service.impl;

import com.lw.project.lwproduct.service.CategoryBrandRelationService;
import com.lw.project.lwproduct.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        // 查出1级分类id
        List<CategoryEntity> level1Categorys = getLevel1Categorys();
        Map<String, List<Catelog2Vo>> catelogList = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 查出二级分类list
            List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    // 根据二级分类找三级分类
                    List<CategoryEntity> level3Catelog = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", l2.getCatId()));
                    if (level3Catelog!=null){
                        List<Catelog2Vo.Catelog3Vo> catelog3Vos = level3Catelog.stream().map(l3 -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(),l3.getCatId().toString(),l3.getName());

                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catelog3Vos);

                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));

        return catelogList;
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