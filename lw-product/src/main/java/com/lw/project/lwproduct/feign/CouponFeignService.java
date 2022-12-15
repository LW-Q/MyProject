package com.lw.project.lwproduct.feign;

import com.lw.common.to.SkuReductionTo;
import com.lw.common.to.SpuBoundTo;
import com.lw.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("lw-coupon")
public interface CouponFeignService {

    @PostMapping("/lwcoupon/spubounds/save")
    R saveSkuBounds(@RequestBody SpuBoundTo spuBoundTo);


    @PostMapping("/lwcoupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
