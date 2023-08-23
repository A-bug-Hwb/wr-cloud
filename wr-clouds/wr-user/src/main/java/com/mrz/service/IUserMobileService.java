package com.wr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wr.domain.SysUserPojo.UserMobilePojo.UserMobilePo;
import com.wr.domain.SysUserPojo.UserMobilePojo.UserMobileVo;

import java.util.List;

public interface IUserMobileService extends IService<UserMobilePo> {

    List<UserMobileVo> getInfoList(String userId);
}
