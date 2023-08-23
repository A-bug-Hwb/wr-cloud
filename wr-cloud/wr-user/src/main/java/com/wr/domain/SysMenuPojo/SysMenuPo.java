package com.wr.domain.SysMenuPojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wr.domain.BaseEntityPo;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@TableName("sys_menu")
public class SysMenuPo extends BaseEntityPo {

    @TableId(type = IdType.AUTO)
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

    @TableField(exist = false)
    private List<SysMenuVo> children = new ArrayList<SysMenuVo>();

}
