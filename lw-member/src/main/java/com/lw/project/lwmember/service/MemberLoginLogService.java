package com.lw.project.lwmember.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lw.common.utils.PageUtils;
import com.lw.project.lwmember.entity.MemberLoginLogEntity;

import java.util.Map;

/**
 * 会员登录记录
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-18 09:24:52
 */
public interface MemberLoginLogService extends IService<MemberLoginLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

