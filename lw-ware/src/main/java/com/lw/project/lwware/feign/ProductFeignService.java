package com.lw.project.lwware.feign;

import com.lw.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("lw-product")
public interface ProductFeignService {
    @RequestMapping("/lwproduct/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId")Long skuId);
}
