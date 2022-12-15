package com.lw.project.lwmember.feign;

import com.lw.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/*
* 声明式的远程调用
* */
@FeignClient("lw-coupon")
public interface CouponFeignService {

    @RequestMapping("/lwcoupon/coupon/member/coupons")
    public R memberCoupons();
}
