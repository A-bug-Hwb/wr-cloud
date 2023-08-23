package com.wr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.domain.SysUserPojo.UserBasicInfoPojo.UserBasicInfoPo;
import com.wr.domain.SysUserPojo.UserBasicInfoPojo.UserBasicInfoVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserBasicInfoMapper extends BaseMapper<UserBasicInfoPo> {

    UserBasicInfoVo getInfo(Long userId);
}
