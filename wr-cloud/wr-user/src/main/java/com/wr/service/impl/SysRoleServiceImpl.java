package com.wr.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wr.domain.LoginPojo.SysRoleBo;
import com.wr.domain.SysRoleMenuPojo.SysRoleMenuPo;
import com.wr.domain.SysRolePojo.*;
import com.wr.domain.SysUserRolePojo.SysUserRolePo;
import com.wr.mapper.SysRoleMapper;
import com.wr.service.ISysRoleMenuService;
import com.wr.service.ISysRoleService;
import com.wr.service.ISysUserRoleService;
import com.wr.utils.BeanUtil;
import com.wr.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRolePo> implements ISysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private ISysUserRoleService iSysUserRoleService;

    @Autowired
    private ISysRoleMenuService iSysRoleMenuService;


    @Override
    public Set<String> getRoleKeys(Long userId) {

        List<Long> roleIds =  iSysUserRoleService.getRoleIds(userId);
        if (StringUtils.isNotEmpty(roleIds)){
            Set<String> set = sysRoleMapper.selectList(Wrappers.lambdaQuery(SysRolePo.class).in(SysRolePo::getRoleId,roleIds)).stream().map(val ->{
                return val.getRoleKey();
            }).collect(Collectors.toSet());
            return set;
        }
        return new HashSet<>();
    }

    @Override
    public String selectUserRoleGroup(Long userId) {
        List<SysRoleBo> list = sysRoleMapper.selectRolesByUserName(userId);
        if (CollectionUtils.isEmpty(list))
        {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRoleBo::getRoleName).collect(Collectors.joining(","));
    }

    @Override
    public List<SysRoleVo> getRoleList(SysRoleDto sysRoleDto) {
        return sysRoleMapper.getRoleList(BeanUtil.beanToBean(sysRoleDto,new SysRolePo()));
    }

    @Override
    public List<SysRoleVo> selectRolePermissionByUserId(Long userId) {
        List<SysRoleVo> userRoles = sysRoleMapper.selectRolePermissionByUserId(userId);
        List<SysRoleVo> roles = sysRoleMapper.getRoleList(new SysRolePo());
        for (SysRoleVo role : roles)
        {
            for (SysRoleVo userRole : userRoles)
            {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue())
                {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    @Override
    public boolean checkRoleNameUnique(String roleName) {
        if (sysRoleMapper.selectList(Wrappers.lambdaQuery(SysRolePo.class).eq(SysRolePo::getRoleName,roleName)).size() > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean checkRoleKeyUnique(String roleKey) {
        if (sysRoleMapper.selectList(Wrappers.lambdaQuery(SysRolePo.class).eq(SysRolePo::getRoleKey,roleKey)).size() > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean insertRole(AddSysRoleDto addSysRoleDto) {
        SysRolePo sysRolePo = BeanUtil.beanToBean(addSysRoleDto,new SysRolePo());
        sysRolePo.setCreateTime(new Date());
        if (sysRoleMapper.insert(sysRolePo) > 0){
            for (Long menuId:addSysRoleDto.getMenuIds()){
                SysRoleMenuPo sysRoleMenuPo = new SysRoleMenuPo();
                sysRoleMenuPo.setRoleId(sysRolePo.getRoleId());
                sysRoleMenuPo.setMenuId(menuId);
                iSysRoleMenuService.save(sysRoleMenuPo);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateRole(UpSysRoleDto upSysRoleDto) {
        SysRolePo sysRolePo = BeanUtil.beanToBean(upSysRoleDto,new SysRolePo());
        sysRolePo.setUpdateTime(new Date());
        if (sysRoleMapper.updateById(sysRolePo) > 0 ){
            //先删除全部
            iSysRoleMenuService.remove(Wrappers.lambdaQuery(SysRoleMenuPo.class).eq(SysRoleMenuPo::getRoleId,upSysRoleDto.getRoleId()));
            //再重新添加
            for (Long menuId:upSysRoleDto.getMenuIds()){
                SysRoleMenuPo sysRoleMenuPo = new SysRoleMenuPo();
                sysRoleMenuPo.setRoleId(sysRolePo.getRoleId());
                sysRoleMenuPo.setMenuId(menuId);
                iSysRoleMenuService.save(sysRoleMenuPo);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteByIds(Long[] roleIds) {

        for (Long roleId:roleIds){
            iSysUserRoleService.remove(Wrappers.lambdaQuery(SysUserRolePo.class).eq(SysUserRolePo::getRoleId,roleId));
            iSysRoleMenuService.remove(Wrappers.lambdaQuery(SysRoleMenuPo.class).eq(SysRoleMenuPo::getRoleId,roleId));
            sysRoleMapper.deleteById(roleId);
        }
        return true;
    }
}
