package com.wr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wr.domain.LoginPojo.RegisterUser;
import com.wr.domain.LoginPojo.SysUserBo;
import com.wr.domain.SysUserPojo.*;

import java.util.List;


public interface ISysUserService extends IService<SysUserPo> {

    SysUserBo findUserByUsername(String username);

    Long registerUser(RegisterUser user,Long registerType);

    List<Long> getUserIds(SysUserDto sysUserDto);

    List<SysUserVo> getUserList(List<Long> userIds);

    SysUserVo getUserInfo(Long userId);

    boolean install(AddSysUserDto addSysUserDto);

    boolean updateUserInfo(UpSysUserDto upSysUserDto);

    boolean updateUserPassStu(UpPwdStuDto upPwdStuDto);

    boolean updatePwd(String oldPassword,String newPassword);

    List<Long> selectAllocatedIdList(SysUserDto sysUserDto);

    List<Long> selectUnallocatedIdList(SysUserDto sysUserDto);

    boolean isAdmin(Long userId);

}
