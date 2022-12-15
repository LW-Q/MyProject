package com.lw.project.lwproduct.dao;

import com.lw.project.lwproduct.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-17 21:06:27
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
