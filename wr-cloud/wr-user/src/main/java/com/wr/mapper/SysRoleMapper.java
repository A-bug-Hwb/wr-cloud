package com.wr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.domain.LoginPojo.SysRoleBo;
import com.wr.domain.SysRolePojo.SysRolePo;
import com.wr.domain.SysRolePojo.SysRoleVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRolePo> {

    List<SysRoleBo> selectRolesByUserName(Long userId);

    List<SysRoleVo> getRoleList(SysRolePo sysRolePo);

    List<SysRoleVo> selectRolePermissionByUserId(Long userId);
}
