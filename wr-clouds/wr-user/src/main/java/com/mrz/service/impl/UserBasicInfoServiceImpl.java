package com.wr.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wr.domain.SysUserPojo.UserBasicInfoPojo.UserBasicInfoPo;
import com.wr.domain.SysUserPojo.UserBasicInfoPojo.UserBasicInfoVo;
import com.wr.mapper.UserBasicInfoMapper;
import com.wr.service.IUserBasicInfoService;
import com.wr.utils.BeanUtil;
import com.wr.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserBasicInfoServiceImpl extends ServiceImpl<UserBasicInfoMapper, UserBasicInfoPo> implements IUserBasicInfoService {

    @Resource
    private UserBasicInfoMapper userBasicInfoMapper;

    @Override
    public UserBasicInfoVo getInfo(String userId) {
        UserBasicInfoPo userBasicInfoPo = userBasicInfoMapper.selectOne(Wrappers.lambdaQuery(UserBasicInfoPo.class).eq(UserBasicInfoPo::getUserId,userId).last("limit 1"));
        if (StringUtils.isNotNull(userBasicInfoPo)){
            return BeanUtil.beanToBean(userBasicInfoPo,new UserBasicInfoVo());
        }
        return null;
    }

    @Override
    public boolean registerInfo(Long userId, Long registerType) {
        UserBasicInfoPo userBasicInfoPo = userBasicInfoMapper.selectOne(Wrappers.lambdaQuery(UserBasicInfoPo.class).eq(UserBasicInfoPo::getUserId,userId).last("limit 1"));
        //判断是否存在用户信息
        if (StringUtils.isNotNull(userBasicInfoPo)){
            userBasicInfoMapper.delete(Wrappers.lambdaQuery(UserBasicInfoPo.class).eq(UserBasicInfoPo::getUserId,userId));
        }
        UserBasicInfoPo userBasicInfoPoInstall = new UserBasicInfoPo();
        userBasicInfoPoInstall.setUserId(userId);
        userBasicInfoPoInstall.setCreateTime(new Date());
        userBasicInfoPoInstall.setCreateBy("注册");
        userBasicInfoPoInstall.setRegisterType(registerType);
        if (userBasicInfoMapper.insert(userBasicInfoPoInstall)>0){
            return true;
        }
        return false;
    }
}
