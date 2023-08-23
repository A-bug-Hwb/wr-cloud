package com.wr.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wr.domain.SysUserPojo.UserBasicInfoPojo.UserBasicInfoPo;
import com.wr.mapper.UserBasicInfoMapper;
import com.wr.service.IUserBasicInfoService;
import com.wr.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserBasicInfoServiceImpl extends ServiceImpl<UserBasicInfoMapper, UserBasicInfoPo> implements IUserBasicInfoService {

    @Resource
    private UserBasicInfoMapper userBasicInfoMapper;

    @Override
    public UserBasicInfoPo getInfo(Long userId) {
        return userBasicInfoMapper.selectOne(Wrappers.lambdaQuery(UserBasicInfoPo.class).eq(UserBasicInfoPo::getUserId,userId).last("limit 1"));
    }

    @Override
    public boolean registerInfo(UserBasicInfoPo userBasicInfoPo, Long registerType) {
        UserBasicInfoPo userBasicInfoPo1 = userBasicInfoMapper.selectOne(Wrappers.lambdaQuery(UserBasicInfoPo.class).eq(UserBasicInfoPo::getUserId,userBasicInfoPo.getUserId()).last("limit 1"));
        //判断是否存在用户信息
        if (StringUtils.isNotNull(userBasicInfoPo1)){
            userBasicInfoMapper.delete(Wrappers.lambdaQuery(UserBasicInfoPo.class).eq(UserBasicInfoPo::getUserId,userBasicInfoPo.getUserId()));
        }
        userBasicInfoPo.setCreateTime(new Date());
        userBasicInfoPo.setCreateBy("注册");
        userBasicInfoPo.setRegisterType(registerType);
        if (userBasicInfoMapper.insert(userBasicInfoPo)>0){
            return true;
        }
        return false;
    }
}
