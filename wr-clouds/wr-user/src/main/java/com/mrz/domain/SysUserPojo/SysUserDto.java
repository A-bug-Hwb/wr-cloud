package com.wr.domain.SysUserPojo;

import com.wr.domain.BaseEntityDto;
import lombok.Data;


@Data
public class SysUserDto extends BaseEntityDto {

    private Long roleId;
    private String userName;
    private String mobile;
    private String mailbox;
    private String status;
}
