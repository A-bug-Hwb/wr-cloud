package com.wr.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wr.domain.SysRolePojo.*;

import java.util.List;
import java.util.Set;

public interface ISysRoleService extends IService<SysRolePo> {

    Set<String> getRoleKeys(Long userId);

    String selectUserRoleGroup(Long userId);

    List<SysRoleVo> getRoleList(SysRoleDto sysRoleDto);

    List<SysRoleVo> selectRolePermissionByUserId(Long userId);


    boolean checkRoleNameUnique(String roleName);

    boolean checkRoleKeyUnique(String roleKey);

    boolean insertRole(AddSysRoleDto addSysRoleDto);

    boolean updateRole(UpSysRoleDto upSysRoleDto);

    boolean deleteByIds(Long[] roleId);
}
