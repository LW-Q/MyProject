package com.lw.project.lwproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lw.common.utils.PageUtils;
import com.lw.project.lwproduct.entity.SpuInfoEntity;
import com.lw.project.lwproduct.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-17 21:06:27
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo vo);

    void saveBaseSpuInfo(SpuInfoEntity infoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);

    /*
    * 商品上架
    * */
    void up(Long spuId);
}

