package com.lw.project.lwcoupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lw.common.to.SkuReductionTo;
import com.lw.common.utils.PageUtils;
import com.lw.project.lwcoupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-17 22:02:09
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

