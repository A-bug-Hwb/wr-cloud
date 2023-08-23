package com.wr.domain.SysUserPojo.UserMobilePojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wr.domain.BaseEntityRpo;
import lombok.Data;

@Data
@TableName("user_mobile")
public class UserMobilePo extends BaseEntityRpo {
    private Long userId;
    private String mobile;
}
