package com.lw.project.lwproduct.feign;

import com.lw.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient("lw-ware")
public interface WareFeignService {
    @PostMapping("/lwware/waresku/hasstock")
    R getSkuHasStock(List<Long> skuIdList);
}
