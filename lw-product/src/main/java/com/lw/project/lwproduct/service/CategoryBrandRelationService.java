package com.lw.project.lwproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lw.common.utils.PageUtils;
import com.lw.project.lwproduct.entity.BrandEntity;
import com.lw.project.lwproduct.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-17 21:06:27
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    void updateBrand(Long brandId, String name);

    void saveCategory(Long catId, String name);

    List<BrandEntity> getBrandsByCatId(Long catId);
}

