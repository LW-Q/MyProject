package com.lw.project.lwproduct.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.lw.project.lwproduct.entity.ProductAttrValueEntity;
import com.lw.project.lwproduct.service.ProductAttrValueService;
import com.lw.project.lwproduct.vo.AttrRespVo;
import com.lw.project.lwproduct.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lw.project.lwproduct.service.AttrService;
import com.lw.common.utils.PageUtils;
import com.lw.common.utils.R;



/**
 * 商品属性
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-17 21:30:42
 */
@RestController
@RequestMapping("lwproduct/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @PostMapping("/update/{spuId}")
    public R attrUpdateSpu(@RequestBody List<ProductAttrValueEntity> entities,@PathVariable("spuId") Long spuId){
        productAttrValueService.updateSpuAttr(spuId, entities);
        return R.ok();
    }
    @GetMapping("/base/listforspu/{spuId}")
    public R attrListForSpu(@PathVariable("spuId") Long spuId){
        List<ProductAttrValueEntity> list = productAttrValueService.attrListForSpu(spuId);

        return R.ok().put("data",list);
    }


    // 根据属性类型和目录id查找所有属性列表
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("catelogId") Long catelogId,
                          @PathVariable("attrType") String attrType){
        PageUtils page = attrService.queryBaseAttrPage(params, catelogId, attrType);
        return R.ok().put("page",page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("lwproduct:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("lwproduct:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		//AttrEntity attr = attrService.getById(attrId);
        AttrRespVo attrRespVo = attrService.getAttrInfo(attrId);
        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("lwproduct:attr:save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("lwproduct:attr:update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("lwproduct:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
