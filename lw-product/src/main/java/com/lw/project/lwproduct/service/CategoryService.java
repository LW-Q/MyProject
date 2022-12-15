package com.lw.project.lwproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lw.common.utils.PageUtils;
import com.lw.project.lwproduct.entity.CategoryEntity;
import com.lw.project.lwproduct.vo.Catelog2Vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-17 21:06:27
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> asList);

    Long[] findCategoryPath(Long catelogId);

    void updateCascade(CategoryEntity category);

    List<CategoryEntity> getLevel1Categorys();

    Map<String, List<Catelog2Vo>>  getCatalogJson();
}

