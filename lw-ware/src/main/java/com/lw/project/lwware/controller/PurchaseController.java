package com.lw.project.lwware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.lw.project.lwware.vo.MergeVo;
import com.lw.project.lwware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lw.project.lwware.entity.PurchaseEntity;
import com.lw.project.lwware.service.PurchaseService;
import com.lw.common.utils.PageUtils;
import com.lw.common.utils.R;



/**
 * 采购信息
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-18 09:31:14
 */
@RestController
@RequestMapping("lwware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/done")
    public R finish(@RequestBody PurchaseDoneVo purchaseDoneVo){
        purchaseService.finish(purchaseDoneVo);
        return R.ok();
    }
    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids){
        purchaseService.received(ids);
        return R.ok();
    }
    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo){
        purchaseService.mergePurchase(mergeVo);

        return R.ok();
    }

    @GetMapping("/unreceive/list")
    public R getUnreceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnreceivePurchase(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("lwware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("lwware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("lwware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("lwware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("lwware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
