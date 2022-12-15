package com.lw.project.lwware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lw.common.utils.PageUtils;
import com.lw.project.lwware.entity.WareInfoEntity;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-18 09:31:14
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

