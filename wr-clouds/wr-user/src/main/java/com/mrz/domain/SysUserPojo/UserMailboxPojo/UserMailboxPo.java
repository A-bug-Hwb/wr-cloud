package com.wr.domain.SysUserPojo.UserMailboxPojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wr.domain.BaseEntityRpo;
import lombok.Data;

@Data
@TableName("user_mailbox")
public class UserMailboxPo extends BaseEntityRpo {

    private Long userId;

    private String mailbox;
}
