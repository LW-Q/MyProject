package com.lw.project.lwware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.lw.project.lwware.vo.SkuHasStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lw.project.lwware.entity.WareSkuEntity;
import com.lw.project.lwware.service.WareSkuService;
import com.lw.common.utils.PageUtils;
import com.lw.common.utils.R;



/**
 * 商品库存
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-18 09:31:14
 */
@RestController
@RequestMapping("lwware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;


    @PostMapping("/hasstock")
    public R getSkusHasStock(@RequestBody List<Long> skuIds){
        List<SkuHasStockVo> stockVos = wareSkuService.getSkusHasStock(skuIds);
        return R.ok().setData(stockVos);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("lwware:waresku:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("lwware:waresku:info")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("lwware:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("lwware:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("lwware:waresku:delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
