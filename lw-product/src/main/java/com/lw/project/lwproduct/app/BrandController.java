package com.lw.project.lwproduct.app;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.lw.common.valid.AddGroup;
import com.lw.common.valid.UpdateGroup;
import com.lw.common.valid.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lw.project.lwproduct.entity.BrandEntity;
import com.lw.project.lwproduct.service.BrandService;
import com.lw.common.utils.PageUtils;
import com.lw.common.utils.R;


/**
 * 品牌
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-17 21:30:42
 */
@RestController
@RequestMapping("lwproduct/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @RequestMapping("/update/status")
    public R updateStatus(@Validated(value = {UpdateStatusGroup.class}) @RequestBody BrandEntity brand){
        brandService.updateById(brand);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("lwproduct:brand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("lwproduct:brand:info")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("lwproduct:brand:save")
    public R save(@Validated(value = {AddGroup.class}) @RequestBody BrandEntity brand){
        brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("lwproduct:brand:update")
    public R update(@Validated(value = {UpdateGroup.class}) @RequestBody BrandEntity brand){
		brandService.updateDetails(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("lwproduct:brand:delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
