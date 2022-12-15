package com.lw.project.search.controller;

import com.lw.common.exception.BizCodeEnum;
import com.lw.common.to.SkuEsModel;
import com.lw.common.utils.R;
import com.lw.project.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequestMapping("/search/save")
@RestController
public class ElasticSaveController {

    @Autowired
    ProductSaveService productSaveService;
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels){
        boolean b = false;
        try {
            b = productSaveService.productStatusUp(skuEsModels);
        }catch (Exception e){
            log.error("商品上架错误：{}",e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMessage());
        }
        if (!b){
            return R.ok();
        }
        else {
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMessage());
        }
    }
}
