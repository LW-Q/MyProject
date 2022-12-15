package com.lw.project.lworder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lw.common.utils.PageUtils;
import com.lw.project.lworder.entity.PaymentInfoEntity;

import java.util.Map;

/**
 * 支付信息表
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-18 09:27:00
 */
public interface PaymentInfoService extends IService<PaymentInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

