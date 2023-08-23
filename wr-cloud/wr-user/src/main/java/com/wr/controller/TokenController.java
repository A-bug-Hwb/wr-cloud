package com.wr.controller;

import com.wr.auth.AuthUtil;
import com.wr.common.service.SysLoginService;
import com.wr.constants.CacheConstants;
import com.wr.constants.Constants;
import com.wr.domain.LoginPojo.LoginUser;
import com.wr.domain.LoginPojo.LoginUserDto;
import com.wr.domain.LoginPojo.RegisterUser;
import com.wr.result.AjaxResult;
import com.wr.result.R;
import com.wr.service.RedisService;
import com.wr.service.TokenService;
import com.wr.utils.*;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * token 控制
 *
 * @author wr
 */
@Api(tags = "登录注册")
@RestController
public class TokenController
{
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    private RedisService redisService;

    @Value("${config.captcha}")
    private String captchaType;

    @ApiOperation("登录")
    @PostMapping("/login")
    public R<?> login(@RequestBody LoginUserDto loginUserDto)
    {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(loginUserDto.getUserName(), loginUserDto.getPassword(),loginUserDto.getCode(),loginUserDto.getUuid());
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo));
    }

    @ApiOperation("退出登录")
    @DeleteMapping("/logout")
    public R<?> logout(HttpServletRequest request)
    {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            String username = JwtUtils.getUserName(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志
//            sysLoginService.logout(username);
        }
        return R.ok();
    }

    @ApiOperation("刷新token")
    @PostMapping("/refresh")
    public R<?> refresh(HttpServletRequest request)
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return R.ok();
        }
        return R.ok();
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public R<?> register(@RequestBody RegisterUser registerUser)
    {
        if (!registerUser.getPassword().equals(registerUser.getConfirmPassword())){
            return R.fail("两次密码不一样");
        }
        // 用户注册
        sysLoginService.register(registerUser);
        return R.ok();
    }

    @ApiOperation("登录验证码")
    @GetMapping("/captchaImage")
    public AjaxResult getCode()
    {
        AjaxResult ajax = AjaxResult.success();

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        String code = null;
        ajax.put("uuid", uuid);
        // 生成验证码
        if ("math".equals(captchaType))
        {
            ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36, 2);
            code = captcha.text();
            ajax.put("img", captcha.toBase64());
        }
        else if ("char".equals(captchaType))
        {
            SpecCaptcha captcha = new SpecCaptcha(111, 36, 4);
            code = captcha.text();
            ajax.put("img", captcha.toBase64());
        }
        redisService.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        return ajax;
    }
}