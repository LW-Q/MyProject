package com.lw.project.lwproduct.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lw.common.utils.PageUtils;
import com.lw.common.utils.Query;

import com.lw.project.lwproduct.dao.ProductAttrValueDao;
import com.lw.project.lwproduct.entity.ProductAttrValueEntity;
import com.lw.project.lwproduct.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveProductAttr(List<ProductAttrValueEntity> collect) {
        this.saveBatch(collect);
    }

    @Override
    public List<ProductAttrValueEntity> attrListForSpu(Long spuId) {
        List<ProductAttrValueEntity> productAttrValueEntities = this.baseMapper.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        return productAttrValueEntities;
    }

    @Transactional
    @Override
    public void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities) {
        // 删除之前spuId的所有数据
        this.baseMapper.delete(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",spuId));
        // 添加修改的数据
        List<ProductAttrValueEntity> collect = entities.stream().map(item -> {
            item.setSpuId(spuId);
            return item;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }

}