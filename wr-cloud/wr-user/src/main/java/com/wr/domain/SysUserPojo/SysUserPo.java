package com.wr.domain.SysUserPojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("sys_user")
public class SysUserPo {

    //之所以用共有方法，是因为在sql拦截器中反射会用到
    @TableId(type = IdType.AUTO)
    public Long userId;
    private String userName;
    private String password;
    private String status;
    private Long deleted;
}
