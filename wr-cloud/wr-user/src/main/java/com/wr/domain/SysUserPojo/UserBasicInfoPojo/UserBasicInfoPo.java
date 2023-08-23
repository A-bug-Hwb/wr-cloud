package com.wr.domain.SysUserPojo.UserBasicInfoPojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wr.domain.BaseEntityEpo;
import lombok.Data;

@Data
@TableName("user_basic_info")
public class UserBasicInfoPo extends BaseEntityEpo {

    private Long userId;
    private String sex;
    private Long registerType;
    private String nickName;
    private String mobile;
    private String mailbox;
}
