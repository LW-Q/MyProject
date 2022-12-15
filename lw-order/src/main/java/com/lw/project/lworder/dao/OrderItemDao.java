package com.lw.project.lworder.dao;

import com.lw.project.lworder.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-18 09:27:00
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
