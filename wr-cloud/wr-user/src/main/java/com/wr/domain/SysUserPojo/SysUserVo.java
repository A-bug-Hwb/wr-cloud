package com.wr.domain.SysUserPojo;

import com.wr.domain.BaseEntityVo;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class SysUserVo extends BaseEntityVo {

    private String userId;
    private String userName;
    private String nickName;
    private String sex;
    private String registerType;
    private String mobile;
    private String mailbox;
    private String status;
    private boolean admin;
}
