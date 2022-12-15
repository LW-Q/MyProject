package com.lw.project.lworder.dao;

import com.lw.project.lworder.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-18 09:27:00
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
