package com.wr.domain.SysRoleMenuPojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role_menu")
public class SysRoleMenuPo {

    private Long roleId;
    private Long menuId;

}
