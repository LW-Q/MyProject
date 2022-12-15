package com.lw.project.lwproduct.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.lw.project.lwproduct.entity.AttrEntity;
import com.lw.project.lwproduct.service.AttrAttrgroupRelationService;
import com.lw.project.lwproduct.service.AttrService;
import com.lw.project.lwproduct.service.CategoryService;
import com.lw.project.lwproduct.vo.AttrGroupRelationVo;
import com.lw.project.lwproduct.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lw.project.lwproduct.entity.AttrGroupEntity;
import com.lw.project.lwproduct.service.AttrGroupService;
import com.lw.common.utils.PageUtils;
import com.lw.common.utils.R;



/**
 * 属性分组
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-17 21:30:42
 */
@RestController
@RequestMapping("lwproduct/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;


    @Autowired
    CategoryService categoryService;

    @Autowired
    AttrService attrService;

    @Autowired
    AttrAttrgroupRelationService relationService;

    //
    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId")Long catelogId){
        // 查出当前分类下的所有属性分组
        // 查出每个属性分组的所有属性
        List<AttrGroupWithAttrsVo> vos = attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);
        return R.ok().put("data",vos);
    }


    // 新增关联属性
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){
        relationService.saveBatch(vos);
        return R.ok();
    }
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId")Long attrgroupId){
        List<AttrEntity> entities = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",entities);
    }

    // 查出所有没有关联的分组
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId")Long attrgroupId,
                            @RequestParam Map<String, Object> params){
        PageUtils page = attrService.getNoRelationAttr(params,attrgroupId);
        return R.ok().put("page",page);
    }
    // 删除关联属性
    @PostMapping("/attr/relation/delete")
    public R deleteAttrRelation(@RequestBody AttrGroupRelationVo[] vos){
        attrService.deleteRelation(vos);
        return R.ok();
    }


    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("lwproduct:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId){
        // PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params,catelogId);


        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("lwproduct:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        Long catelogId = attrGroup.getCatelogId();
        Long[] categoryPath = categoryService.findCategoryPath(catelogId);

        attrGroup.setCatelogPath(categoryPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("lwproduct:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("lwproduct:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("lwproduct:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
