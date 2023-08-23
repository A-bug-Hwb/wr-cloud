package com.wr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wr.domain.LoginPojo.RegisterUser;
import com.wr.domain.LoginPojo.SysUserBo;
import com.wr.domain.SysUserPojo.*;

import java.util.List;


public interface ISysUserService extends IService<SysUserPo> {

    SysUserBo findUserByUsername(String username);

    Long registerUser(RegisterUser user,Long registerType);

    List<SysUserVo> getUserList(SysUserDto sysUserDto);

    SysUserVo getUserInfo(Long userId);

    boolean install(AddSysUserDto addSysUserDto);

    boolean updateUserInfo(UpSysUserDto upSysUserDto);

    boolean updateUserPassStu(UpPwdStuDto upPwdStuDto);

    boolean updatePwd(String oldPassword,String newPassword);

    List<SysUserVo> selectAllocatedList(SysUserDto sysUserDto);

    List<SysUserVo> selectUnallocatedList(SysUserDto sysUserDto);

    boolean isAdmin(Long userId);

}
