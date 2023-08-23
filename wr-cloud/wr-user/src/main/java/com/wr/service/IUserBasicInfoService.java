package com.wr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wr.domain.SysUserPojo.UserBasicInfoPojo.UserBasicInfoPo;

public interface IUserBasicInfoService extends IService<UserBasicInfoPo> {

    UserBasicInfoPo getInfo(Long userId);

    boolean registerInfo(UserBasicInfoPo userBasicInfoPo,Long registerType);
}
