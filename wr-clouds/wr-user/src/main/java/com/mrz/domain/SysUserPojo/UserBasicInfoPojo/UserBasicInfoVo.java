package com.wr.domain.SysUserPojo.UserBasicInfoPojo;

import com.wr.domain.BaseEntityVo;
import lombok.Data;

@Data
public class UserBasicInfoVo extends BaseEntityVo {

    private String sex;
    private Long registerType;

}
