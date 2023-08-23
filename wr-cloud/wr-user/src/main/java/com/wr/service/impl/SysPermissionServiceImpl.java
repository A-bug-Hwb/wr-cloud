package com.wr.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.wr.domain.LoginPojo.SysRoleBo;
import com.wr.domain.LoginPojo.SysUserBo;
import com.wr.mapper.SysUserMapper;
import com.wr.service.ISysMenuService;
import com.wr.service.ISysPermissionService;
import com.wr.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 用户权限处理
 *
 * @author wr
 */
@Service
public class SysPermissionServiceImpl implements ISysPermissionService
{
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 获取角色数据权限
     *
     * @param user 用户Id
     * @return 角色权限信息
     */
    @Override
    public Set<String> getRolePermission(SysUserBo user)
    {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (sysUserMapper.isAdmin(user.getUserId()))
        {
            roles.add("admin");
        }
        else
        {
            roles.addAll(roleService.getRoleKeys(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户Id
     * @return 菜单权限信息
     */
    @Override
    public Set<String> getMenuPermission(SysUserBo user)
    {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (sysUserMapper.isAdmin(user.getUserId()))
        {
            perms.add("*:*:*");
        }
        else
        {
            List<SysRoleBo> roles = user.getSysRoleBos();
            if (!CollectionUtils.isEmpty(roles))
            {
                // 多角色设置permissions属性，以便数据权限匹配权限
                for (SysRoleBo role : roles)
                {
                    Set<String> rolePerms = menuService.selectMenuPermsByRoleId(role.getRoleId());
                    role.setPermissions(rolePerms);
                    perms.addAll(rolePerms);
                }
            }
            else
            {
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }
}
