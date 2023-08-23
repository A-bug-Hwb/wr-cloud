package com.wr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wr.domain.SysUserPojo.UserMailboxPojo.UserMailboxPo;
import com.wr.domain.SysUserPojo.UserMailboxPojo.UserMailboxVo;
import com.wr.mapper.UserMailboxMapper;
import com.wr.service.IUserMailboxService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserMailboxServiceImpl extends ServiceImpl<UserMailboxMapper,UserMailboxPo> implements IUserMailboxService {

    @Resource
    private UserMailboxMapper userMailboxMapper;

    @Override
    public List<UserMailboxVo> getInfoList(String userId) {
        return userMailboxMapper.getInfoList(userId);
    }
}
