package com.wr.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wr.domain.LoginPojo.LoginUser;
import com.wr.domain.LoginPojo.RegisterUser;
import com.wr.domain.LoginPojo.SysRoleBo;
import com.wr.domain.LoginPojo.SysUserBo;
import com.wr.exception.ServiceException;
import com.wr.domain.SysRolePojo.SysRolePo;
import com.wr.domain.SysUserPojo.*;
import com.wr.domain.SysUserRolePojo.SysUserRolePo;
import com.wr.mapper.SysUserMapper;
import com.wr.service.ISysRoleService;
import com.wr.service.ISysUserRoleService;
import com.wr.service.ISysUserService;
import com.wr.service.IUserBasicInfoService;
import com.wr.utils.BeanUtil;
import com.wr.utils.IdUtils;
import com.wr.utils.SecurityUtils;
import com.wr.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserPo> implements ISysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISysUserRoleService iSysUserRoleService;

    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private IUserBasicInfoService iUserBasicInfoService;


    @Override
    public SysUserBo findUserByUsername(String username) {
        SysUserBo userBo = sysUserMapper.findUserByUsername(username);
        if (StringUtils.isNotNull(userBo)) {
            List<SysUserRolePo> list = iSysUserRoleService.list(Wrappers.lambdaQuery(SysUserRolePo.class).eq(SysUserRolePo::getUserId, userBo.getUserId()));
            if (StringUtils.isNotEmpty(list)) {
                List<Long> roleIds = list.stream().map(val -> {
                    return val.getRoleId();
                }).collect(Collectors.toList());
                List<SysRoleBo> sysRoleBos = new ArrayList<>();
                List<SysRolePo> rolePos = iSysRoleService.list(Wrappers.lambdaQuery(SysRolePo.class).in(SysRolePo::getRoleId, roleIds));
                for (SysRolePo rolePo : rolePos) {
                    SysRoleBo sysRoleBo = BeanUtil.beanToBean(rolePo, new SysRoleBo());
                    sysRoleBos.add(sysRoleBo);
                }
                userBo.setSysRoleBos(sysRoleBos);
            }
        }
        return userBo;
    }

    @Override
    public Long registerUser(RegisterUser user,Long registerType) {
        SysUserPo userPo = BeanUtil.beanToBean(user, new SysUserPo());
        userPo.setUserName(user.getUsername());
        userPo.setUserId(Long.valueOf(IdUtils.getUUID()));
        userPo.setStatus("0");
        if (sysUserMapper.insert(userPo) > 0) {
            iUserBasicInfoService.registerInfo(userPo.getUserId(),registerType);
            return userPo.getUserId();
        }
        return null;
    }

    @Override
    public List<Long> getUserIds(SysUserDto sysUserDto) {
        return sysUserMapper.getUserIds(sysUserDto);
    }

    @Override
    public List<SysUserVo> getUserList(List<Long> userIds) {
        if (userIds.size()>0){
            return sysUserMapper.getUserList(userIds);
        }
        return new ArrayList<>();
    }

    @Override
    public SysUserVo getUserInfo(Long userId) {
        return sysUserMapper.getUserInfo(userId);
    }

    @Override
    public boolean install(AddSysUserDto addSysUserDto) {
        SysUserPo sysUserPo = BeanUtil.beanToBean(addSysUserDto, new SysUserPo());
        sysUserPo.setUserId(Long.valueOf(IdUtils.getUUID()));
        sysUserPo.setPassword(SecurityUtils.encryptPassword(sysUserPo.getPassword()));
        if (sysUserMapper.insert(sysUserPo) > 0) {
            //更新用户基本信息
            iUserBasicInfoService.registerInfo(sysUserPo.getUserId(),0L);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserInfo(UpSysUserDto upSysUserDto) {
        SysUserPo sysUserPo = BeanUtil.beanToBean(upSysUserDto, new SysUserPo());
        if (sysUserMapper.updateById(sysUserPo) > 0) {
            SysUserBo sysUserBo = BeanUtil.beanToBean(sysUserMapper.selectById(SecurityUtils.getUserId()), new SysUserBo());
            LoginUser loginUser = SecurityUtils.getLoginUser();
            loginUser.setSysUserBo(sysUserBo);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserPassStu(UpPwdStuDto upPwdStuDto) {
        SysUserPo sysUserPo = BeanUtil.beanToBean(upPwdStuDto, new SysUserPo());
        if (sysUserMapper.updateById(sysUserPo) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePwd(String oldPassword, String newPassword) {
        SysUserPo sysUserPo = new SysUserPo();
        sysUserPo.setUserId(SecurityUtils.getUserId());
        sysUserPo.setPassword(SecurityUtils.encryptPassword(newPassword));
        if (sysUserMapper.updateById(sysUserPo) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Long> selectAllocatedIdList(SysUserDto sysUserDto) {
        return sysUserMapper.selectAllocatedIdList(sysUserDto);
    }

    @Override
    public List<Long> selectUnallocatedIdList(SysUserDto sysUserDto) {
        return sysUserMapper.selectUnallocatedIdList(sysUserDto);
    }

    @Override
    public boolean isAdmin(Long userId) {
        return sysUserMapper.isAdmin(userId);
    }

}
