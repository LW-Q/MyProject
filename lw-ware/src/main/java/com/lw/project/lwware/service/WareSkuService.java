package com.lw.project.lwware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lw.common.utils.PageUtils;
import com.lw.project.lwware.entity.WareSkuEntity;
import com.lw.project.lwware.vo.SkuHasStockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-18 09:31:14
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);
}

