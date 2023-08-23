package com.wr.domain.SysMenuPojo;

import lombok.Data;

@Data
public class UpSysMenuDto {

    private Long menuId;
    private String menuName;
    private Long parentId;
    private Long orderNum;
    private String path;
    private String component;
    private String query;
    private String isFrame;
    private String isCache;
    private String menuType;
    private String visible;
    private String status;
    private String perms;
    private String icon;
    private String remark;

}
