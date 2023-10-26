package com.wr.controller;

import com.alibaba.fastjson2.JSONObject;
import com.wr.domain.LoginPojo.LoginUser;
import com.wr.domain.LoginPojo.SysUserBo;
import com.wr.domain.SysRolePojo.SysRoleVo;
import com.wr.domain.SysUserPojo.*;
import com.wr.result.R;
import com.wr.service.*;
import com.wr.utils.SecurityUtils;
import com.wr.utils.StringUtils;
import com.wr.utils.format.FormatUtils;
import com.wr.web.controller.BaseController;
import com.wr.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Api(tags = "用户信息")
@RequestMapping("/sysUser")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private ISysUserRoleService iSysUserRoleService;

    @Autowired
    private ISysPermissionService iSysPermissionService;

    @GetMapping("/getInfo")
    @ApiOperation("获取登录用户信息")
    public R getUserInfo(){
        SysUserBo userBo = SecurityUtils.getLoginUser().getSysUserBo();
        //这个不用BeanUtil转，因为前端需要String类型的userId，否则会精度丢失。
        SysUserVo userVo = iSysUserService.getUserInfo(userBo.getUserId());
        // 角色集合
        Set<String> roles = iSysPermissionService.getRolePermission(userBo);
        // 权限集合mailbox
        Set<String> permissions = iSysPermissionService.getMenuPermission(userBo);
        Map<String, Object> map = new HashMap<>(3);
        map.put("user", userVo);
        map.put("roles", roles);
        map.put("permissions", permissions);
        return R.ok(map);
    }

    @GetMapping("/list")
    @ApiOperation("获取用户列表")
    public TableDataInfo list(SysUserDto sysUserDto)
    {
        startPage();
        return getDataTable(iSysUserService.getUserList(sysUserDto));
    }
    @GetMapping("/{userId}")
    @ApiOperation("查询用户详细")
    public R list(@PathVariable Long userId)
    {
        return R.ok(iSysUserService.getUserInfo(userId));
    }

    @PostMapping("/add")
    @ApiOperation("新增用户")
    public R add(@RequestBody AddSysUserDto addSysUserDto)
    {
        if (StringUtils.isNotNull(iSysUserService.findUserByUsername(addSysUserDto.getUserName()))){
            return R.fail("用户名"+addSysUserDto.getUserName()+"已存在");
        }
        if (iSysUserService.install(addSysUserDto)){
            return R.ok("添加成功");
        }
        return  R.fail("添加失败");
    }

    @PutMapping("/edit")
    @ApiOperation("修改用户信息")
    public R edit(@RequestBody UpSysUserDto upSysUserDto)
    {

        if (iSysUserService.isAdmin(upSysUserDto.getUserId())){
            return  R.fail("不允许操作超级管理员用户");
        }else if (StringUtils.isNull(iSysUserService.findUserByUsername(upSysUserDto.getUserName()))){
            return  R.fail("用户名"+upSysUserDto.getUserName()+"已存在");
        }
        if (!FormatUtils.isMobile(upSysUserDto.getMobile())){
            return  R.fail("手机号格式不正确");
        }
        return R.ok(iSysUserService.updateUserInfo(upSysUserDto));
    }

    @DeleteMapping("/delete/{userIds}")
    @ApiOperation("删除用户")
    public R delete(@PathVariable List<Long> userIds)
    {
        for (Long userId:userIds){
            if (iSysUserService.isAdmin(userId)){
                return R.fail("当前选中有超级管理员用户，不允许删除");
            }
        }
        iSysUserService.removeBatchByIds(userIds);
        return R.ok();
    }

    @PutMapping("/resetPwd")
    @ApiOperation("用户密码重置")
    public R resetPwd(@RequestBody UpPwdStuDto upPwdStuDto)
    {
        if (iSysUserService.isAdmin(upPwdStuDto.getUserId())){
            return R.fail("不允许操作超级管理员用户");
        }
        return R.ok(iSysUserService.updateUserPassStu(upPwdStuDto));
    }

    @PutMapping("/changeStatus")
    @ApiOperation("用户状态修改")
    public R changeStatus(@RequestBody UpPwdStuDto upPwdStuDto)
    {
        if (iSysUserService.isAdmin(upPwdStuDto.getUserId())){
            return R.fail("不允许操作超级管理员用户");
        }
        return R.ok(iSysUserService.updateUserPassStu(upPwdStuDto));
    }

    @GetMapping("/getProfile")
    @ApiOperation("个人中心信息")
    public R profile()
    {
        SysUserVo userVo = iSysUserService.getUserInfo(SecurityUtils.getUserId());
        Map<String, Object> map = new HashMap<>(2);
        map.put("user", userVo);
        map.put("roleGroup", iSysRoleService.selectUserRoleGroup(SecurityUtils.getUserId()));
        return R.ok(map);
    }
    @PutMapping("/upProfile")
    @ApiOperation("修改用户信息")
    public R upProfile(@RequestBody UpSysUserDto upSysUserDto)
    {
        if (!FormatUtils.isMobile(upSysUserDto.getMobile())){
            return R.fail("手机号格式错误");
        }
        if (StringUtils.isNull(iSysUserService.findUserByUsername(upSysUserDto.getUserName()))){
            return R.fail("用户名"+upSysUserDto.getUserName()+"已存在");
        }
        return R.ok(iSysUserService.updateUserInfo(upSysUserDto));
    }

    @PutMapping("/upProfilePwd")
    @ApiOperation("修改个人用户密码")
    public R upProfilePwd(String oldPassword,String newPassword)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String password = loginUser.getSysUserBo().getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password))
        {
            return R.fail("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password))
        {
            return R.fail("新密码不能与旧密码相同");
        }
        if (iSysUserService.updatePwd(oldPassword,newPassword)){
            return R.ok("修改密码成功");
        }
        return R.fail("修改密码失败");
    }

    @GetMapping("/authRole/{userId}")
    @ApiOperation("查询已授权角色")
    public R authRole(@PathVariable Long userId)
    {
        Map<String,Object> map = new HashMap<>(2);
        SysUserVo user = iSysUserService.getUserInfo(userId);
        List<SysRoleVo> roles = iSysRoleService.selectRolePermissionByUserId(userId);
        map.put("user", user);
        map.put("roles", iSysUserService.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.getRoleKey().equals("admin")).collect(Collectors.toList()));
        return R.ok(map);
    }

    @PutMapping("/upAuthRole")
    @ApiOperation("更新授权角色")
    public R upAuthRole(Long userId, Long[] roleIds)
    {
        if (iSysUserRoleService.insertUserAuth(userId, roleIds)){
            return R.ok("授权成功");
        }
        return R.fail("授权失败");
    }
}
