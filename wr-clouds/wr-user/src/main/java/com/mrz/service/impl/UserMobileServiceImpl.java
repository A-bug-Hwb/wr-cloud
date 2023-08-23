package com.wr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wr.domain.SysUserPojo.UserMobilePojo.UserMobilePo;
import com.wr.domain.SysUserPojo.UserMobilePojo.UserMobileVo;
import com.wr.mapper.UserMobileMapper;
import com.wr.service.IUserMobileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserMobileServiceImpl extends ServiceImpl<UserMobileMapper, UserMobilePo> implements IUserMobileService {

    @Resource
    private UserMobileMapper userMobileMapper;

    @Override
    public List<UserMobileVo> getInfoList(String userId) {
        return userMobileMapper.getInfoList(userId);
    }
}
