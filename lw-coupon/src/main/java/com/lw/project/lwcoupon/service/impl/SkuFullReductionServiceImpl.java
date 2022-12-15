package com.lw.project.lwcoupon.service.impl;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.lw.common.to.MemberPrice;
import com.lw.common.to.SkuReductionTo;
import com.lw.project.lwcoupon.entity.MemberPriceEntity;
import com.lw.project.lwcoupon.entity.SkuLadderEntity;
import com.lw.project.lwcoupon.service.MemberPriceService;
import com.lw.project.lwcoupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lw.common.utils.PageUtils;
import com.lw.common.utils.Query;

import com.lw.project.lwcoupon.dao.SkuFullReductionDao;
import com.lw.project.lwcoupon.entity.SkuFullReductionEntity;
import com.lw.project.lwcoupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    SkuLadderService skuLadderService;

    @Autowired
    MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }



    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        // 1、sku的优惠、满减等信息
        // 阶梯价格
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
        if (skuReductionTo.getFullCount()>0){
            skuLadderService.save(skuLadderEntity);
        }

        // 满减
        SkuFullReductionEntity reductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo,reductionEntity);
        if (reductionEntity.getReducePrice().compareTo(new BigDecimal("0"))>0) {
            this.save(reductionEntity);
        }

        // 会员价格
        List<MemberPrice> memberPrice = skuReductionTo.getMemberPrice();
        if (memberPrice!=null){
            List<MemberPriceEntity> collect = memberPrice.stream().map(item -> {
                MemberPriceEntity priceEntity = new MemberPriceEntity();
                priceEntity.setSkuId(reductionEntity.getSkuId());
                priceEntity.setMemberLevelId(item.getId());
                priceEntity.setMemberLevelName(item.getName());
                priceEntity.setMemberPrice(item.getPrice());
                priceEntity.setAddOther(1);
                return priceEntity;

            }).filter(item-> item.getMemberPrice().compareTo(new BigDecimal("0"))>0).collect(Collectors.toList());
            memberPriceService.saveBatch(collect);
        }

    }

}