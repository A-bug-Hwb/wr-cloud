package com.wr.common.service;

import com.wr.constants.CacheConstants;
import com.wr.constants.UserConstants;
import com.wr.domain.LoginPojo.LoginUser;
import com.wr.domain.LoginPojo.RegisterUser;
import com.wr.domain.LoginPojo.SysUserBo;
import com.wr.enums.UserStatus;
import com.wr.exception.ServiceException;
import com.wr.service.ISysConfigService;
import com.wr.service.ISysPermissionService;
import com.wr.service.ISysUserService;
import com.wr.service.RedisService;
import com.wr.text.Convert;
import com.wr.utils.SecurityUtils;
import com.wr.utils.StringUtils;
import com.wr.utils.ip.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 登录校验方法
 * 
 * @author wr
 */
@Component
public class SysLoginService
{


    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysPermissionService iSysPermissionService;

    @Autowired
    private ISysConfigService iSysConfigService;

    /**
     * 登录
     */
    public LoginUser login(String username, String password, String code, String uuid)
    {
        boolean captchaEnabled = iSysConfigService.selectCaptchaEnabled();
//        // 验证码开关
//        if (captchaEnabled) {
//            validateCaptcha(code, uuid);
//        }
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password))
        {
            throw new ServiceException("用户/密码必须填写");
        }
        // IP黑名单校验
        String blackStr = Convert.toStr(redisService.getCacheObject(CacheConstants.SYS_LOGIN_BLACKIPLIST));
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            throw new ServiceException("很遗憾，访问IP已被列入系统黑名单");
        }

        SysUserBo sysUser = iSysUserService.findUserByUsername(username);
        if (StringUtils.isNull(sysUser))
        {
            throw new ServiceException("很遗憾，访问IP已被列入系统黑名单");
        }


        if (StringUtils.isNull(sysUser) || StringUtils.isNull(sysUser))
        {
            throw new ServiceException("登录用户：" + username + " 不存在");
        }

        if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus()))
        {
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }


        // 角色集合
        Set<String> roles = iSysPermissionService.getRolePermission(sysUser);
        // 权限集合
        Set<String> permissions = iSysPermissionService.getMenuPermission(sysUser);
        LoginUser loginUser = new LoginUser();
        loginUser.setSysUserBo(sysUser);
        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);
        passwordService.validate(sysUser, password);
        return loginUser;
    }

    public boolean logout(String loginName)
    {
        return true;
    }

    /**
     * 注册
     */
    public boolean register(RegisterUser registerUser)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(registerUser.getUsername(), registerUser.getPassword()))
        {
            throw new ServiceException("用户/密码必须填写");
        }
        if (registerUser.getUsername().length() < UserConstants.USERNAME_MIN_LENGTH
                || registerUser.getUsername().length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            throw new ServiceException("账户长度必须在2到20个字符之间");
        }
        if (registerUser.getPassword().length() < UserConstants.PASSWORD_MIN_LENGTH
                || registerUser.getPassword().length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            throw new ServiceException("密码长度必须在5到20个字符之间");
        }
        // 注册用户信息
        registerUser.setPassword(SecurityUtils.encryptPassword(registerUser.getPassword()));
        if (StringUtils.isNotNull(iSysUserService.registerUser(registerUser,0L))){
            return true;
        }
        return false;
    }

    public void validateCaptcha(String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisService.getCacheObject(verifyKey);
        redisService.deleteObject(verifyKey);
        if (captcha == null) {
            throw new ServiceException("请填写验证码");
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new ServiceException("验证码错误");
        }
    }
}
