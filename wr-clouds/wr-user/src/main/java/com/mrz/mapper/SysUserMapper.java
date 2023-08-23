package com.wr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.domain.LoginPojo.SysUserBo;
import com.wr.domain.SysUserPojo.SysUserDto;
import com.wr.domain.SysUserPojo.SysUserPo;
import com.wr.domain.SysUserPojo.SysUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUserPo> {

    SysUserBo findUserByUsername(String username);

    List<Long> getUserIds(SysUserDto sysUserDto);
    List<SysUserVo> getUserList(@Param("userIds") List<Long> userIds);

    SysUserVo getUserInfo(Long userId);

    List<Long> selectAllocatedIdList(SysUserDto sysUserDto);

    List<Long> selectUnallocatedIdList(SysUserDto sysUserDto);

    boolean isAdmin(Long userId);
}
