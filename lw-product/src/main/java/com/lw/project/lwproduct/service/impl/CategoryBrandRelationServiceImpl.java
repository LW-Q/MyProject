package com.lw.project.lwproduct.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lw.project.lwproduct.dao.BrandDao;
import com.lw.project.lwproduct.dao.CategoryDao;
import com.lw.project.lwproduct.entity.BrandEntity;
import com.lw.project.lwproduct.entity.CategoryEntity;
import com.lw.project.lwproduct.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lw.common.utils.PageUtils;
import com.lw.common.utils.Query;

import com.lw.project.lwproduct.dao.CategoryBrandRelationDao;
import com.lw.project.lwproduct.entity.CategoryBrandRelationEntity;
import com.lw.project.lwproduct.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    BrandDao brandDao;
    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryBrandRelationDao relationDao;

    @Autowired
    BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        this.save(categoryBrandRelation);
    }

    @Override
    public void updateBrand(Long brandId, String name) {
        CategoryBrandRelationEntity categoryBrandRelation = new CategoryBrandRelationEntity();
        categoryBrandRelation.setBrandId(brandId);
        categoryBrandRelation.setBrandName(name);
        this.update(categoryBrandRelation,new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id",brandId));
    }

    @Override
    public void saveCategory(Long catId, String name) {
        this.baseMapper.updateCategory(catId,name);
//        CategoryBrandRelationEntity categoryBrandRelation = new CategoryBrandRelationEntity();
//        categoryBrandRelation.setCatelogId(catId);
//        categoryBrandRelation.setCatelogName(name);
//        this.update(categoryBrandRelation,new UpdateWrapper<CategoryBrandRelationEntity>().eq("catelog_id",catId));
    }

    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId) {
        List<CategoryBrandRelationEntity> categoryBrandRelationEntities = relationDao.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        return categoryBrandRelationEntities.stream().map(item -> {
            Long brandId = item.getBrandId();
            return brandService.getById(brandId);
        }).collect(Collectors.toList());
    }
}