package com.lw.project.lwware.service.impl;

import com.lw.common.constant.WareConstant;
import com.lw.project.lwware.entity.PurchaseDetailEntity;
import com.lw.project.lwware.service.PurchaseDetailService;
import com.lw.project.lwware.service.WareSkuService;
import com.lw.project.lwware.vo.MergeVo;
import com.lw.project.lwware.vo.PurchaseDoneVo;
import com.lw.project.lwware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lw.common.utils.PageUtils;
import com.lw.common.utils.Query;

import com.lw.project.lwware.dao.PurchaseDao;
import com.lw.project.lwware.entity.PurchaseEntity;
import com.lw.project.lwware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService purchaseDetailService;
    @Autowired
    WareSkuService wareSkuService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        List<Long> items = mergeVo.getItems();
        items.forEach(item->{
            PurchaseDetailEntity detailEntity = purchaseDetailService.getById(item);
            if (detailEntity.getStatus()==WareConstant.PurchaseDetailStatusEnum.CREATED.getCode() ||
                    detailEntity.getStatus()==WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode()){
                return;
            }
        });

        if (purchaseId==null){
            PurchaseEntity purchase = new PurchaseEntity();
            purchase.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchase.setUpdateTime(new Date());
            purchase.setCreateTime(new Date());
            this.save(purchase);
            purchaseId = purchase.getId();
        }
        // TODO 确定采购单状态是0或者1才能合并



        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(i -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(i);
            detailEntity.setPurchaseId(finalPurchaseId);
            detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return detailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(finalPurchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

    @Override
    public void received(List<Long> ids) {
        // 确认当前采购单是新建或者已分配

        List<PurchaseEntity> purchaseEntityList = ids.stream().map(id -> {
            PurchaseEntity byId = this.getById(id);
            return byId;
        }).filter(item -> {
            if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                    item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            }
            return false;
        }).map(item->{
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());

        // 改变采购单的状态
        this.updateBatchById(purchaseEntityList);
        // 改变采购项的状态
        purchaseEntityList.forEach(item->{
            List<PurchaseDetailEntity> detailEntities = purchaseDetailService.listDetailByPurchaseId(item.getId());
            List<PurchaseDetailEntity> purchaseDetailEntities = detailEntities.stream().map(entity -> {
                PurchaseDetailEntity entity1 = new PurchaseDetailEntity();
                entity1.setId(entity.getId());
                entity1.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return entity1;

            }).collect(Collectors.toList());

            purchaseDetailService.updateBatchById(purchaseDetailEntities);
        });
    }

    @Transactional
    @Override
    public void finish(PurchaseDoneVo purchaseDoneVo) {
        Long id = purchaseDoneVo.getId();
        List<PurchaseDetailEntity> updates = new ArrayList<>();
        // 改变采购项状态
        Boolean flag = true;
        List<PurchaseItemDoneVo> items = purchaseDoneVo.getItems();
        for (PurchaseItemDoneVo item : items) {
            PurchaseDetailEntity purchaseDetail = new PurchaseDetailEntity();
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASEERROR.getCode()){
                flag=false;
            }
            purchaseDetail.setStatus(item.getStatus());
            purchaseDetail.setId(item.getItemId());
            updates.add(purchaseDetail);
            // 成功采购的进行入库
            if (flag){
                PurchaseDetailEntity entity = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(entity.getSkuId(),entity.getWareId(),entity.getSkuNum());
            }

        }
        purchaseDetailService.updateBatchById(updates);

        // 改变采购单状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag?WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.HASEERROR.getCode());
        this.updateById(purchaseEntity);
    }

}