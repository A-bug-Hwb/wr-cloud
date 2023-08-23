package com.wr.domain.SysRolePojo;

import com.wr.domain.BaseEntityDto;
import lombok.Data;

@Data
public class SysRoleDto extends BaseEntityDto {

    private String roleName;
    private String roleKey;
    private String status;
}
