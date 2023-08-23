package com.wr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wr.domain.SysUserPojo.UserMailboxPojo.UserMailboxPo;
import com.wr.domain.SysUserPojo.UserMailboxPojo.UserMailboxVo;

import java.util.List;

public interface IUserMailboxService extends IService<UserMailboxPo> {

    List<UserMailboxVo> getInfoList(String userId);
}
