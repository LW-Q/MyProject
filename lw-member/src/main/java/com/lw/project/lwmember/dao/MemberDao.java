package com.lw.project.lwmember.dao;

import com.lw.project.lwmember.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author liwei
 * @email 1017519980@qq.com
 * @date 2022-11-18 09:24:52
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
