package com.lw.project.lwproduct.vo;

import lombok.Data;

@Data
public class AttrRespVo extends  AttrVo{
    private String catelogName;
    private String groupName;
    private Long[] catelogPath;
}
