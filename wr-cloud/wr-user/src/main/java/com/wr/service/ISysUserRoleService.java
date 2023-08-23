package com.wr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wr.domain.SysUserRolePojo.SysUserRoleDto;
import com.wr.domain.SysUserRolePojo.SysUserRolePo;

import java.util.List;

public interface ISysUserRoleService extends IService<SysUserRolePo> {

    List<Long> getRoleIds(Long userId);

    boolean insertUserAuth(Long userId, Long[] roleIds);

    boolean deleteAuthUser(SysUserRoleDto sysUserRoleDto);
    boolean deleteAuthUsers(Long roleId, Long[] userIds);
    boolean insertAuthUsers(Long roleId, Long[] userIds);
}
