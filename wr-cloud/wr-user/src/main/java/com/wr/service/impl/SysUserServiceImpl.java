package com.wr.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wr.domain.LoginPojo.LoginUser;
import com.wr.domain.LoginPojo.RegisterUser;
import com.wr.domain.LoginPojo.SysRoleBo;
import com.wr.domain.LoginPojo.SysUserBo;
import com.wr.domain.SysUserPojo.UserBasicInfoPojo.UserBasicInfoPo;
import com.wr.domain.SysRolePojo.SysRolePo;
import com.wr.domain.SysUserPojo.*;
import com.wr.domain.SysUserRolePojo.SysUserRolePo;
import com.wr.mapper.SysUserMapper;
import com.wr.service.ISysRoleService;
import com.wr.service.ISysUserRoleService;
import com.wr.service.ISysUserService;
import com.wr.service.IUserBasicInfoService;
import com.wr.utils.SecurityUtils;
import com.wr.utils.StringUtils;
import com.wr.utils.bean.BeanUtils;
import com.wr.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
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
                    SysRoleBo sysRoleBo = BeanUtils.copyDataProp(rolePo, new SysRoleBo());
                    sysRoleBos.add(sysRoleBo);
                }
                userBo.setSysRoleBos(sysRoleBos);
            }
        }
        return userBo;
    }

    @Override
    public Long registerUser(RegisterUser user, Long registerType) {
        SysUserPo userPo = BeanUtils.copyDataProp(user, new SysUserPo());
        userPo.setUserName(user.getUsername());
        userPo.setUserId(Long.valueOf(IdUtils.getUUID()));
        userPo.setStatus("0");
        if (sysUserMapper.insert(userPo) > 0) {
            UserBasicInfoPo userBasicInfoPo = BeanUtils.copyDataProp(user, new UserBasicInfoPo());
            userBasicInfoPo.setUserId(userPo.getUserId());
            userBasicInfoPo.setRegisterType(registerType);
            System.out.println(userBasicInfoPo);
            iUserBasicInfoService.registerInfo(userBasicInfoPo, registerType);
            return userPo.getUserId();
        }
        return null;
    }

    @Override
    public List<SysUserVo> getUserList(SysUserDto sysUserDto) {
        return sysUserMapper.getUserList(sysUserDto);
    }

    @Override
    public SysUserVo getUserInfo(Long userId) {
        return sysUserMapper.getUserInfo(userId);
    }

    @Override
    public boolean install(AddSysUserDto addSysUserDto) {
        SysUserPo sysUserPo = BeanUtils.copyDataProp(addSysUserDto, new SysUserPo());
        sysUserPo.setUserId(Long.valueOf(IdUtils.getUUID()));
        sysUserPo.setPassword(SecurityUtils.encryptPassword(sysUserPo.getPassword()));
        if (sysUserMapper.insert(sysUserPo) > 0) {
            UserBasicInfoPo userBasicInfoPo = new UserBasicInfoPo();
            userBasicInfoPo.setUserId(sysUserPo.getUserId());
            //添加用户基本信息
            iUserBasicInfoService.registerInfo(userBasicInfoPo, 0L);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserInfo(UpSysUserDto upSysUserDto) {
        SysUserPo sysUserPo = BeanUtils.copyDataProp(upSysUserDto, new SysUserPo());
        if (sysUserMapper.updateById(sysUserPo) > 0) {
            SysUserBo sysUserBo = BeanUtils.copyDataProp(sysUserMapper.selectById(SecurityUtils.getUserId()), new SysUserBo());
            LoginUser loginUser = SecurityUtils.getLoginUser();
            loginUser.setSysUserBo(sysUserBo);

            UserBasicInfoPo userBasicInfoPoUpIn = BeanUtils.copyDataProp(upSysUserDto, new UserBasicInfoPo());

            UserBasicInfoPo userBasicInfoPo = iUserBasicInfoService.getInfo(sysUserPo.getUserId());
            if (StringUtils.isNotNull(userBasicInfoPo)) {
                userBasicInfoPoUpIn = BeanUtils.copyDataProp(upSysUserDto, new UserBasicInfoPo());
                userBasicInfoPoUpIn.setUpdateTime(new Date());
                userBasicInfoPoUpIn.setUpdateBy(SecurityUtils.getUsername());
                iUserBasicInfoService.update(userBasicInfoPoUpIn, Wrappers.lambdaUpdate(UserBasicInfoPo.class).eq(UserBasicInfoPo::getUserId, userBasicInfoPoUpIn.getUserId()));
            }else {
                userBasicInfoPoUpIn.setCreateBy(SecurityUtils.getUsername());
                userBasicInfoPoUpIn.setCreateTime(new Date());
                userBasicInfoPoUpIn.setUpdateTime(new Date());
                userBasicInfoPoUpIn.setUpdateBy(SecurityUtils.getUsername());
                iUserBasicInfoService.save(userBasicInfoPoUpIn);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserPassStu(UpPwdStuDto upPwdStuDto) {
        SysUserPo sysUserPo = BeanUtils.copyDataProp(upPwdStuDto, new SysUserPo());
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
    public List<SysUserVo> selectAllocatedList(SysUserDto sysUserDto) {
        return sysUserMapper.selectAllocatedList(sysUserDto);
    }

    @Override
    public List<SysUserVo> selectUnallocatedList(SysUserDto sysUserDto) {
        return sysUserMapper.selectUnallocatedList(sysUserDto);
    }

    @Override
    public boolean isAdmin(Long userId) {
        return sysUserMapper.isAdmin(userId);
    }

}
