package com.wr.domain.SysRolePojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wr.domain.BaseEntityPo;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("sys_role")
public class SysRolePo extends BaseEntityPo {

    @TableId(type = IdType.AUTO)
    private Long roleId;
    private String roleName;
    private String roleKey;
    private Long roleSort;
    private boolean menuCheckStrictly;
    private String status;

}
