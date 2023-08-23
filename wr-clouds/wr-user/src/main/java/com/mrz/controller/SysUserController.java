package com.wr.controller;

import com.alibaba.fastjson2.JSONObject;
import com.wr.domain.LoginPojo.LoginUser;
import com.wr.domain.LoginPojo.SysUserBo;
import com.wr.domain.SysRolePojo.SysRoleVo;
import com.wr.domain.SysUserPojo.*;
import com.wr.exception.ServiceException;
import com.wr.result.AjaxResult;
import com.wr.service.*;
import com.wr.utils.BeanUtil;
import com.wr.utils.SecurityUtils;
import com.wr.utils.StringUtils;
import com.wr.web.controller.BaseController;
import com.wr.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @Autowired
    private IUserBasicInfoService iUserBasicInfoService;

    @Autowired
    private IUserMobileService iUserMobileService;

    @Autowired
    private IUserMailboxService iUserMailboxService;



    @GetMapping("/getInfo")
    @ApiOperation("获取登录用户信息")
    public AjaxResult getUserInfo(){
        SysUserBo userBo = SecurityUtils.getLoginUser().getSysUserBo();
        //这个不用BeanUtil转，因为前端需要String类型的userId，否则会精度丢失。
        SysUserVo userVo = new SysUserVo();
        userVo.setUserId(userBo.getUserId().toString());
        userVo.setUserName(userBo.getUserName());
        userVo.setAdmin(userBo.isAdmin());
        userVo.setStatus(userBo.getStatus());
        userVo.setUserBasicInfoVo(iUserBasicInfoService.getInfo(userVo.getUserId()));
        userVo.setUserMobileVos(iUserMobileService.getInfoList(userVo.getUserId()));
        userVo.setUserMailboxVos(iUserMailboxService.getInfoList(userVo.getUserId()));
        // 角色集合
        Set<String> roles = iSysPermissionService.getRolePermission(userBo);
        // 权限集合mailbox
        Set<String> permissions = iSysPermissionService.getMenuPermission(userBo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", userVo);
        jsonObject.put("roles", roles);
        jsonObject.put("permissions", permissions);
        return AjaxResult.success(jsonObject);
    }

    @GetMapping("/list")
    @ApiOperation("获取用户列表")
    public TableDataInfo list(SysUserDto sysUserDto)
    {
        List<Long> userIds = iSysUserService.getUserIds(sysUserDto);
        startPage();
        return getDataTable(iSysUserService.getUserList(userIds));
    }
    @GetMapping("/{userId}")
    @ApiOperation("查询用户详细")
    public AjaxResult list(@PathVariable Long userId)
    {
        return success(iSysUserService.getUserInfo(userId));
    }

    @PostMapping("/add")
    @ApiOperation("新增用户")
    public AjaxResult add(@RequestBody AddSysUserDto addSysUserDto)
    {
        if (StringUtils.isNotNull(iSysUserService.findUserByUsername(addSysUserDto.getUserName()))){
            return error("用户名"+addSysUserDto.getUserName()+"已存在");
        }
        if (iSysUserService.install(addSysUserDto)){
            return success("添加成功");
        }
        return error("添加失败");
    }

    @PutMapping("/edit")
    @ApiOperation("修改用户信息")
    public AjaxResult edit(@RequestBody UpSysUserDto upSysUserDto)
    {
        if (iSysUserService.isAdmin(upSysUserDto.getUserId())){
            return error("不允许操作超级管理员用户");
        }else if (StringUtils.isNull(iSysUserService.findUserByUsername(upSysUserDto.getUserName()))){
            return error("用户名"+upSysUserDto.getUserName()+"已存在");
        }
        return success(iSysUserService.updateUserInfo(upSysUserDto));
    }

    @DeleteMapping("/delete/{userIds}")
    @ApiOperation("删除用户")
    public AjaxResult delete(@PathVariable List<Long> userIds)
    {
        for (Long userId:userIds){
            if (iSysUserService.isAdmin(userId)){
                return error("当前选中有超级管理员用户，不允许删除");
            }
        }
        iSysUserService.removeBatchByIds(userIds);
        return success();
    }

    @PutMapping("/resetPwd")
    @ApiOperation("用户密码重置")
    public AjaxResult resetPwd(@RequestBody UpPwdStuDto upPwdStuDto)
    {
        if (iSysUserService.isAdmin(upPwdStuDto.getUserId())){
            return error("不允许操作超级管理员用户");
        }
        return success(iSysUserService.updateUserPassStu(upPwdStuDto));
    }

    @PutMapping("/changeStatus")
    @ApiOperation("用户状态修改")
    public AjaxResult changeStatus(@RequestBody UpPwdStuDto upPwdStuDto)
    {
        if (iSysUserService.isAdmin(upPwdStuDto.getUserId())){
            return error("不允许操作超级管理员用户");
        }
        return success(iSysUserService.updateUserPassStu(upPwdStuDto));
    }

    @GetMapping("/getProfile")
    @ApiOperation("个人中心信息")
    public AjaxResult profile()
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUserVo user = BeanUtil.beanToBean(loginUser.getSysUserBo(),new SysUserVo());
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", iSysRoleService.selectUserRoleGroup(SecurityUtils.getUserId()));
        return ajax;
    }
    @PutMapping("/upProfile")
    @ApiOperation("修改用户信息")
    public AjaxResult upProfile(@RequestBody UpSysUserDto upSysUserDto)
    {
        return success(iSysUserService.updateUserInfo(upSysUserDto));
    }

    @PutMapping("/upProfilePwd")
    @ApiOperation("修改个人用户密码")
    public AjaxResult upProfilePwd(String oldPassword,String newPassword)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String password = loginUser.getSysUserBo().getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password))
        {
            return error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password))
        {
            return error("新密码不能与旧密码相同");
        }
        if (iSysUserService.updatePwd(oldPassword,newPassword)){
            return success("修改密码成功");
        }
        return error("修改密码失败");
    }

    @GetMapping("/authRole/{userId}")
    @ApiOperation("查询已授权角色")
    public AjaxResult authRole(@PathVariable Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        SysUserVo user = BeanUtil.beanToBean(iSysUserService.getById(userId),new SysUserVo());
        List<SysRoleVo> roles = iSysRoleService.selectRolePermissionByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", iSysUserService.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.getRoleKey().equals("admin")).collect(Collectors.toList()));
        return ajax;
    }

    @PutMapping("/upAuthRole")
    @ApiOperation("更新授权角色")
    public AjaxResult upAuthRole(Long userId, Long[] roleIds)
    {
        if (iSysUserRoleService.insertUserAuth(userId, roleIds)){
            return success("授权成功");
        }
        return error("授权失败");
    }
}
