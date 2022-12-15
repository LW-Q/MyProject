package com.lw.project.lwproduct.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lw.project.lwproduct.entity.BrandEntity;
import com.lw.project.lwproduct.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lw.project.lwproduct.entity.CategoryBrandRelationEntity;
import com.lw.project.lwproduct.service.CategoryBrandRelationService;
import com.lw.common.utils.PageUtils;
import com.lw.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-17 21:30:42
 */
@RestController
@RequestMapping("lwproduct/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 查询某品牌的关联列表
     */
    @GetMapping("/catelog/list")
    //@RequiresPermissions("lwproduct:categorybrandrelation:list")
    public R catelogList(@RequestParam("brandId")Long brand_id){

        List<CategoryBrandRelationEntity> categoryBrandRelationEntities = categoryBrandRelationService.list(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id",brand_id)
        );

        return R.ok().put("data", categoryBrandRelationEntities);
    }

    @GetMapping("/brands/list")
    public R relationBrandsList(@RequestParam(value = "catId", required = true) Long catId){
        List<BrandEntity> vos = categoryBrandRelationService.getBrandsByCatId(catId);
        List<Object> collect = vos.stream().map((item) -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(item.getBrandId());
            brandVo.setBrandName(item.getName());
            return brandVo;
        }).collect(Collectors.toList());
        return R.ok().put("data",collect);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("lwproduct:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("lwproduct:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("lwproduct:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.saveDetail(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("lwproduct:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("lwproduct:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
