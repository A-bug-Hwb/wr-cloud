package com.wr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wr.domain.SysUserPojo.UserBasicInfoPojo.UserBasicInfoPo;
import com.wr.domain.SysUserPojo.UserBasicInfoPojo.UserBasicInfoVo;

public interface IUserBasicInfoService extends IService<UserBasicInfoPo> {

    UserBasicInfoVo getInfo(String userId);

    boolean registerInfo(Long userId,Long registerType);
}
