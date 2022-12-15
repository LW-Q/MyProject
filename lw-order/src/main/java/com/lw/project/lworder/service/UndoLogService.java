package com.lw.project.lworder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lw.common.utils.PageUtils;
import com.lw.project.lworder.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-18 09:27:00
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

