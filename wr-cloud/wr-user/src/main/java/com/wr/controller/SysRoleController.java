package com.wr.controller;


import com.wr.domain.SysRolePojo.*;
import com.wr.domain.SysUserPojo.SysUserDto;
import com.wr.domain.SysUserRolePojo.SysUserRoleDto;
import com.wr.result.R;
import com.wr.service.ISysRoleService;
import com.wr.service.ISysUserRoleService;
import com.wr.service.ISysUserService;
import com.wr.utils.SecurityUtils;
import com.wr.utils.bean.BeanUtils;
import com.wr.web.controller.BaseController;
import com.wr.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


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
    public R getInfo(@PathVariable Long roleId) {
        return R.ok(BeanUtils.copyDataProp(iSysRoleService.getById(roleId), new SysRoleVo()));
    }

    /**
     * 新增角色
     */
    @ApiOperation("新增角色")
    @PostMapping("/add")
    public R add(@Validated @RequestBody AddSysRoleDto addSysRoleDto) {
        if (iSysRoleService.checkRoleNameUnique(addSysRoleDto.getRoleName())) {
            return R.fail("新增角色'" + addSysRoleDto.getRoleName() + "'失败，角色名称已存在");
        } else if (iSysRoleService.checkRoleKeyUnique(addSysRoleDto.getRoleKey())) {
            return R.fail("新增角色'" + addSysRoleDto.getRoleName() + "'失败，角色权限已存在");
        }
        return R.ok(iSysRoleService.insertRole(addSysRoleDto));
    }


    @ApiOperation("修改角色信息")
    @PutMapping("/edit")
    public R edit(@RequestBody UpSysRoleDto upSysRoleDto) {
        SysRolePo sysRolePo = iSysRoleService.getById(upSysRoleDto.getRoleId());
        if (sysRolePo != null) {
            if (!sysRolePo.getRoleKey().equals(upSysRoleDto.getRoleKey())) {
                if (iSysRoleService.checkRoleKeyUnique(upSysRoleDto.getRoleName())) {
                    return R.fail("修改角色'" + upSysRoleDto.getRoleName() + "'失败，角色权限已存在");
                }
            } else if (!sysRolePo.getRoleName().equals(upSysRoleDto.getRoleName())) {
                if (iSysRoleService.checkRoleNameUnique(upSysRoleDto.getRoleKey())) {
                    return R.fail("修改角色'" + upSysRoleDto.getRoleName() + "'失败，角色名称已存在");
                }
            }
        }
        if (iSysRoleService.updateRole(upSysRoleDto)) {
            return R.ok("修改成功");
        }
        return R.fail("修改失败");
    }


    @ApiOperation("角色状态修改")
    @PutMapping("/changeStatus")
    public R changeStatus(@RequestBody SysRoleUpStaDto role) {
        if (role.getRoleId().equals(1L)) {
            return R.fail("不能停用超级管理员角色");
        }
        SysRolePo sysRolePo = new SysRolePo();
        sysRolePo.setRoleId(role.getRoleId());
        sysRolePo.setStatus(role.getStatus());
        sysRolePo.setUpdateBy(SecurityUtils.getUsername());
        sysRolePo.setUpdateTime(new Date());
        return R.ok(iSysRoleService.updateById(sysRolePo));
    }

    /**
     * 删除角色
     */
    @ApiOperation("删除角色")
    @DeleteMapping("/delete/{roleIds}")
    public R delete(@PathVariable Long[] roleIds) {
        for (Long roleId : roleIds) {
            if (roleId.equals(1L)) {
                return R.fail("当前选中有超级管理员，不能删除");
            }
        }
        return R.ok(iSysRoleService.deleteByIds(roleIds));
    }

    /**
     * 查询已分配用户角色列表
     */
    @ApiOperation("查询已分配用户角色列表")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedList(SysUserDto sysUserDto) {
        startPage();
        return getDataTable(iSysUserService.selectAllocatedList(sysUserDto));
    }

    /**
     * 查询未分配用户角色列表
     */
    @ApiOperation("查询未分配用户角色列表")
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo unallocatedList(SysUserDto sysUserDto) {
        startPage();
        return getDataTable(iSysUserService.selectUnallocatedList(sysUserDto));
    }

    /**
     * 取消授权用户
     */
    @ApiOperation("取消授权用户")
    @PutMapping("/authUser/cancel")
    public R cancelAuthUser(@RequestBody SysUserRoleDto sysUserRoleDto) {
        return R.ok(iSysUserRoleService.deleteAuthUser(sysUserRoleDto));
    }

    /**
     * 批量取消授权用户
     */
    @ApiOperation("批量取消授权用户")
    @PutMapping("/authUser/cancelAll")
    public R cancelAuthUserAll(Long roleId, Long[] userIds) {
        return R.ok(iSysUserRoleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 批量选择用户授权
     */
    @ApiOperation("批量选择用户授权")
    @PutMapping("/authUser/selectAll")
    public R selectAuthUserAll(Long roleId, Long[] userIds) {
        return R.ok(iSysUserRoleService.insertAuthUsers(roleId, userIds));
    }
}
