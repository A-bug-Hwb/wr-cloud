package com.wr.domain.LoginPojo;

import com.wr.domain.BaseEntityBo;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@ToString
public class SysRoleBo extends BaseEntityBo {

    private Long roleId;
    private String roleName;
    private String roleKey;
    private Long roleSort;
    private boolean menuCheckStrictly;
    private String status;

    private Set<String> permissions;

}
