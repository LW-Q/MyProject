package com.lw.project.lwcoupon.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lw.project.lwcoupon.entity.CouponEntity;
import com.lw.project.lwcoupon.service.CouponService;
import com.lw.common.utils.PageUtils;
import com.lw.common.utils.R;



/**
 * 优惠券信息
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-17 22:02:09
 */
@RefreshScope
@RestController
@RequestMapping("lwcoupon/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

    private Logger log = LoggerFactory.getLogger(this.getClass());
    @RequestMapping("/member/coupons")
    public R membercoupons(){
        CouponEntity coupon = new CouponEntity();
        coupon.setCouponName("法拉利五元优惠券");
        return R.ok().put("coupons", Collections.singletonList(coupon));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("lwcoupon:coupon:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = couponService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("lwcoupon:coupon:info")
    public R info(@PathVariable("id") Long id){
		CouponEntity coupon = couponService.getById(id);

        return R.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("lwcoupon:coupon:save")
    public R save(@RequestBody CouponEntity coupon){
        log.debug(coupon.toString());
		couponService.save(coupon);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("lwcoupon:coupon:update")
    public R update(@RequestBody CouponEntity coupon){
		couponService.updateById(coupon);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("lwcoupon:coupon:delete")
    public R delete(@RequestBody Long[] ids){
        log.debug(Arrays.toString(ids));
		couponService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
