package com.lw.project.lwproduct.service.impl;

import com.lw.project.lwproduct.entity.AttrEntity;
import com.lw.project.lwproduct.entity.CategoryEntity;
import com.lw.project.lwproduct.service.AttrService;
import com.lw.project.lwproduct.vo.AttrGroupWithAttrsVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lw.common.utils.PageUtils;
import com.lw.common.utils.Query;

import com.lw.project.lwproduct.dao.AttrGroupDao;
import com.lw.project.lwproduct.entity.AttrGroupEntity;
import com.lw.project.lwproduct.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrService attrService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId){
        IPage<AttrGroupEntity> page = null;
        if (catelogId==0){
            String key = (String) params.get("key");
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
            if (!StringUtils.isEmpty(key)){
                 wrapper.eq("attr_group_id",key).or().like("attr_group_name",key);
            }
            page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
        }
        else {
            String key = (String) params.get("key");
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id",catelogId);
            if (!StringUtils.isEmpty(key)){
                wrapper.and((obj)->{
                    obj.eq("attr_group_id",key).or().like("attr_group_name",key);
                });
            }
            page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);

        }
        return new PageUtils(page);
    }


    /*
    * 根据分类id查出所有的分组以及组里的属性
    * */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        // 查询分组信息
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<AttrGroupWithAttrsVo> attrsVos = attrGroupEntities.stream().map(group -> {
            AttrGroupWithAttrsVo attrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(group, attrsVo);
            List<AttrEntity> attrs = attrService.getRelationAttr(attrsVo.getAttrGroupId());
            attrsVo.setAttrs(attrs);
            return attrsVo;
        }).collect(Collectors.toList());
        return attrsVos;
    }


}