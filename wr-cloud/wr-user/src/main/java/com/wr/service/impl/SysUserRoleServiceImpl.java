package com.wr.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wr.domain.SysUserRolePojo.SysUserRoleDto;
import com.wr.domain.SysUserRolePojo.SysUserRolePo;
import com.wr.mapper.UserRoleMapper;
import com.wr.service.ISysUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<UserRoleMapper, SysUserRolePo> implements ISysUserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    public List<Long> getRoleIds(Long userId){
        List<Long> roleIds = userRoleMapper.selectList(Wrappers.lambdaQuery(SysUserRolePo.class).eq(SysUserRolePo::getUserId,userId)).stream().map(val ->{
            return val.getRoleId();
        }).collect(Collectors.toList());
        return roleIds;
    }

    @Override
    public boolean insertUserAuth(Long userId, Long[] roleIds) {
        userRoleMapper.delete(Wrappers.lambdaQuery(SysUserRolePo.class).eq(SysUserRolePo::getUserId,userId));
        for (Long roleId:roleIds){
            SysUserRolePo sysUserRolePo = new SysUserRolePo();
            sysUserRolePo.setUserId(userId);
            sysUserRolePo.setRoleId(roleId);
            userRoleMapper.insert(sysUserRolePo);
        }
        return true;
    }

    @Override
    public boolean deleteAuthUser(SysUserRoleDto sysUserRoleDto) {
        userRoleMapper.delete(Wrappers.lambdaQuery(SysUserRolePo.class)
                .eq(SysUserRolePo::getRoleId,sysUserRoleDto.getRoleId())
                .eq(SysUserRolePo::getUserId,sysUserRoleDto.getUserId()));
        return true;
    }

    @Override
    public boolean deleteAuthUsers(Long roleId, Long[] userIds) {
        if (userIds.length > 0){
            userRoleMapper.delete(Wrappers.lambdaQuery(SysUserRolePo.class)
                    .eq(SysUserRolePo::getRoleId,roleId)
                    .in(SysUserRolePo::getUserId,userIds));
        }
        return true;
    }

    @Override
    public boolean insertAuthUsers(Long roleId, Long[] userIds) {
        for (Long userId:userIds){
            SysUserRolePo sysUserRolePo = new SysUserRolePo();
            sysUserRolePo.setRoleId(roleId);
            sysUserRolePo.setUserId(userId);
            userRoleMapper.insert(sysUserRolePo);
        }
        return true;
    }
}
