package com.lw.project.lwproduct.dao;

import com.lw.project.lwproduct.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 * 
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-17 21:06:27
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    List<Long> selectSearchAttrIds(@Param("attrIds")List<Long> attrIds);
}
