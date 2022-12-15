package com.lw.project.lwcoupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lw.common.utils.PageUtils;
import com.lw.project.lwcoupon.entity.CouponEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 优惠券信息
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-17 22:02:09
 */
public interface CouponService extends IService<CouponEntity> {

    PageUtils queryPage(Map<String, Object> params);

}

