package com.lw.project.lwproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lw.common.utils.PageUtils;
import com.lw.project.lwproduct.entity.SpuCommentEntity;

import java.util.Map;

/**
 * 商品评价
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-17 21:06:27
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

