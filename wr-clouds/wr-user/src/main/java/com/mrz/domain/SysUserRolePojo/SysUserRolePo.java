package com.wr.domain.SysUserRolePojo;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user_role")
public class SysUserRolePo {

    private Long roleId;
    private Long userId;
}
