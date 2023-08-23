package com.wr.domain.SysUserPojo;

import com.wr.domain.SysUserPojo.UserBasicInfoPojo.UserBasicInfoVo;
import com.wr.domain.SysUserPojo.UserMailboxPojo.UserMailboxVo;
import com.wr.domain.SysUserPojo.UserMobilePojo.UserMobileVo;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
public class SysUserVo {

    private String userId;
    private String userName;
    private String status;
    private boolean admin;

    private UserBasicInfoVo userBasicInfoVo;
    private List<UserMobileVo> userMobileVos;
    private List<UserMailboxVo> userMailboxVos;

}
