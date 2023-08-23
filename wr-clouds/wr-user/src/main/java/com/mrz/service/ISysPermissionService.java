package com.wr.service;


import com.wr.domain.LoginPojo.SysUserBo;

import java.util.Set;

/**
 * 权限信息 服务层
 * 
 * @author wr
 */
public interface ISysPermissionService
{
    /**
     * 获取角色数据权限
     * 
     * @param user 用户
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUserBo user);

    /**
     * 获取菜单数据权限
     * 
     * @param user 用户
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUserBo user);
}
