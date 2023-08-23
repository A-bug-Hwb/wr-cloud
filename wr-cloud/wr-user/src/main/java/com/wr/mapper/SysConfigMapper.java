package com.wr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.domain.SysConfigPojo.SysConfigDto;
import com.wr.domain.SysConfigPojo.SysConfigPo;
import com.wr.domain.SysConfigPojo.SysConfigVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfigPo> {

    List<SysConfigVo> selectConfigList(SysConfigDto sysConfigDto);
}
