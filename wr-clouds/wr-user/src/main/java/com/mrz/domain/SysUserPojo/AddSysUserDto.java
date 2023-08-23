package com.wr.domain.SysUserPojo;

import lombok.Data;

@Data
public class AddSysUserDto {
    private String userName;
    private String password;
    private String status;
    private String remark;
}
