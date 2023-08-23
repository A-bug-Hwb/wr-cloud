package com.wr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.domain.SysUserPojo.UserMailboxPojo.UserMailboxPo;
import com.wr.domain.SysUserPojo.UserMailboxPojo.UserMailboxVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMailboxMapper extends BaseMapper<UserMailboxPo> {

    List<UserMailboxVo> getInfoList(String userId);
}
