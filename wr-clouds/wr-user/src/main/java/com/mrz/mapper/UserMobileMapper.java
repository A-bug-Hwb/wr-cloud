package com.wr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.domain.SysUserPojo.UserMobilePojo.UserMobilePo;
import com.wr.domain.SysUserPojo.UserMobilePojo.UserMobileVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMobileMapper extends BaseMapper<UserMobilePo> {

    List<UserMobileVo> getInfoList(String userId);
}
