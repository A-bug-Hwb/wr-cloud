package com.wr.controller;


import com.wr.domain.SysRolePojo.*;
import com.wr.domain.SysUserPojo.SysUserDto;
import com.wr.domain.SysUserPojo.SysUserVo;
import com.wr.domain.SysUserRolePojo.SysUserRoleDto;
import com.wr.result.AjaxResult;
import com.wr.service.ISysRoleService;
import com.wr.service.ISysUserRoleService;
import com.wr.service.ISysUserService;
import com.wr.utils.BeanUtil;
import com.wr.utils.SecurityUtils;
import com.wr.web.controller.BaseController;
import com.wr.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Api(tags = "角色管理")
@RestController
@RequestMapping("/sysRole")
public class SysRoleController extends BaseController {

    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ISysUserRoleService iSysUserRoleService;

    @ApiOperation("获取角色列表")
    @GetMapping("/list")
    public TableDataInfo list(SysRoleDto sysRoleDto) {
        startPage();
        return getDataTable(iSysRoleService.getRoleList(sysRoleDto));
    }

    @ApiOperation("获取角色详情")
    @GetMapping("/getInfo/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        return success(BeanUtil.beanToBean(iSysRoleService.getById(roleId), new SysRoleVo()));
    }

    /**
     * 新增角色
     */
    @ApiOperation("新增角色")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody AddSysRoleDto addSysRoleDto) {
        if (iSysRoleService.checkRoleNameUnique(addSysRoleDto.getRoleName())) {
            return AjaxResult.error("新增角色'" + addSysRoleDto.getRoleName() + "'失败，角色名称已存在");
        } else if (iSysRoleService.checkRoleKeyUnique(addSysRoleDto.getRoleKey())) {
            return error("新增角色'" + addSysRoleDto.getRoleName() + "'失败，角色权限已存在");
        }
        return toAjax(iSysRoleService.insertRole(addSysRoleDto));
    }


    @ApiOperation("修改角色信息")
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody UpSysRoleDto upSysRoleDto) {
        SysRolePo sysRolePo = iSysRoleService.getById(upSysRoleDto.getRoleId());
        if (sysRolePo != null) {
            if (!sysRolePo.getRoleKey().equals(upSysRoleDto.getRoleKey())) {
                if (iSysRoleService.checkRoleKeyUnique(upSysRoleDto.getRoleName())) {
                    return error("修改角色'" + upSysRoleDto.getRoleName() + "'失败，角色权限已存在");
                }
            } else if (!sysRolePo.getRoleName().equals(upSysRoleDto.getRoleName())) {
                if (iSysRoleService.checkRoleNameUnique(upSysRoleDto.getRoleKey())) {
                    return AjaxResult.error("修改角色'" + upSysRoleDto.getRoleName() + "'失败，角色名称已存在");
                }
            }
        }
        if (iSysRoleService.updateRole(upSysRoleDto)) {
            return success("修改成功");
        }
        return error("修改失败");
    }


    @ApiOperation("角色状态修改")
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysRoleUpStaDto role) {
        if (role.getRoleId().equals(1L)) {
            return error("不能停用超级管理员角色");
        }
        SysRolePo sysRolePo = new SysRolePo();
        sysRolePo.setRoleId(role.getRoleId());
        sysRolePo.setStatus(role.getStatus());
        sysRolePo.setUpdateBy(SecurityUtils.getUsername());
        sysRolePo.setUpdateTime(new Date());
        return success(iSysRoleService.updateById(sysRolePo));
    }

    /**
     * 删除角色
     */
    @ApiOperation("删除角色")
    @DeleteMapping("/delete/{roleIds}")
    public AjaxResult delete(@PathVariable Long[] roleIds) {
        for (Long roleId : roleIds) {
            if (roleId.equals(1L)) {
                return error("当前选中有超级管理员，不能删除");
            }
        }
        return toAjax(iSysRoleService.deleteByIds(roleIds));
    }

    /**
     * 查询已分配用户角色列表
     */
    @ApiOperation("查询已分配用户角色列表")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedList(SysUserDto sysUserDto) {

        List<Long> userIds = iSysUserService.selectAllocatedIdList(sysUserDto);
        if (userIds.size() < 1) {
            return getDataTable(new ArrayList<>());
        }
        startPage();
        return getDataTable(iSysUserService.getUserList(userIds));
    }

    /**
     * 查询未分配用户角色列表
     */
    @ApiOperation("查询未分配用户角色列表")
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo unallocatedList(SysUserDto sysUserDto) {
        List<Long> userIds = iSysUserService.selectUnallocatedIdList(sysUserDto);
        if (userIds.size() < 1) {
            return getDataTable(new ArrayList<>());
        }
        startPage();
        return getDataTable(iSysUserService.getUserList(userIds));
    }

    /**
     * 取消授权用户
     */
    @ApiOperation("取消授权用户")
    @PutMapping("/authUser/cancel")
    public AjaxResult cancelAuthUser(@RequestBody SysUserRoleDto sysUserRoleDto) {
        return toAjax(iSysUserRoleService.deleteAuthUser(sysUserRoleDto));
    }

    /**
     * 批量取消授权用户
     */
    @ApiOperation("批量取消授权用户")
    @PutMapping("/authUser/cancelAll")
    public AjaxResult cancelAuthUserAll(Long roleId, Long[] userIds) {
        return toAjax(iSysUserRoleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 批量选择用户授权
     */
    @ApiOperation("批量选择用户授权")
    @PutMapping("/authUser/selectAll")
    public AjaxResult selectAuthUserAll(Long roleId, Long[] userIds) {
        return toAjax(iSysUserRoleService.insertAuthUsers(roleId, userIds));
    }
}
