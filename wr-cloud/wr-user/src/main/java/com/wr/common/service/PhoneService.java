package com.wr.common.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wr.common.utils.SMSUtils;
import com.wr.domain.LoginPojo.LoginUser;
import com.wr.domain.LoginPojo.RegisterUser;
import com.wr.domain.LoginPojo.SysUserBo;
import com.wr.domain.SysUserPojo.UserBasicInfoPojo.UserBasicInfoPo;
import com.wr.enums.UserStatus;
import com.wr.exception.ServiceException;
import com.wr.result.R;
import com.wr.service.*;
import com.wr.utils.*;
import com.wr.utils.bean.BeanUtils;
import com.wr.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class PhoneService {

    @Autowired
    private IUserBasicInfoService iUserBasicInfoService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysPermissionService iSysPermissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisService redisService;

    public R logReg(String mobile) {
        //先查询是否存在手机号
        UserBasicInfoPo userBasicInfoPo = iUserBasicInfoService.getOne(Wrappers.lambdaQuery(UserBasicInfoPo.class).eq(UserBasicInfoPo::getMobile, mobile).last("limit 1"));
        //有手机号,直接登录
        if (StringUtils.isNotNull(userBasicInfoPo)) {
            return login(userBasicInfoPo.getUserId());
        } else {
            //否则先注册再登录
            return login(register(mobile));
        }
    }

    public R logReg(String mobile, String key, String code) {
        //如果是短信登录，校验验证码
        try {
            String code1 = redisService.getCacheObject(key);
            if (code.equals(code1)){
                redisService.deleteObject(key);
                redisService.deleteObject(mobile);
            }else {
                throw new ServiceException("验证码错误");
            }
        } catch (Exception e) {
            throw new ServiceException("验证码已失效");
        }
        return logReg(mobile);
    }


    public R getCode(String mobile) {
        //先获取手机号缓存，如果存在。则证明此手机号在60s内
        String mobileVal = redisService.getCacheObject(mobile);
        if (mobileVal != null){
            throw new ServiceException("请60秒后重试");
        }
        String code = IdUtils.getFourNum().toString();
        SMSUtils.sendMessage(mobile, code);
        String key = IdUtils.simpleUUID();
        //将验证码放入缓存
        redisService.setCacheObject(key, code, 5L, TimeUnit.MINUTES);
        //将手机号缓存60秒
        redisService.setCacheObject(mobile,mobile,60L,TimeUnit.SECONDS);
        return R.ok(key, "短信发送成功");
    }

    private R login(Long userId) {
        SysUserBo sysUserBo = BeanUtils.copyDataProp(iSysUserService.getById(userId),new SysUserBo());
        if (UserStatus.DISABLE.getCode().equals(sysUserBo.getStatus())) {
            throw new ServiceException("对不起，您的账号：" + sysUserBo.getMobile() + " 已停用");
        }
        // 角色集合
        Set<String> roles = iSysPermissionService.getRolePermission(sysUserBo);
        // 权限集合
        Set<String> permissions = iSysPermissionService.getMenuPermission(sysUserBo);
        LoginUser loginUser = new LoginUser();
        loginUser.setSysUserBo(sysUserBo);
        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);
        return R.ok(tokenService.createToken(loginUser));
    }

    private Long register(String mobile) {
        // 注册用户信息
        RegisterUser registerUser = new RegisterUser();
        registerUser.setUsername(mobile);
        registerUser.setPassword(SecurityUtils.encryptPassword(""));
        registerUser.setMobile(mobile);
        registerUser.setNickName(mobile);
        Long userId = iSysUserService.registerUser(registerUser, 1L);
        return userId;
    }
}
