package com.wr.domain.SysRolePojo;

import com.wr.domain.BaseEntityVo;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@ToString
public class UpSysRoleDto extends BaseEntityVo {

    private Long roleId;
    private String roleName;
    private String roleKey;
    private Long roleSort;
    private boolean menuCheckStrictly;
    private String status;
    private boolean flag = false;

    private Long[] menuIds;

    /** 角色菜单权限 */
    private Set<String> permissions;

}
