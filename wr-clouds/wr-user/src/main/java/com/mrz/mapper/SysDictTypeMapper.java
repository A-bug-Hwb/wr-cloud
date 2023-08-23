package com.wr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.domain.SysDictPojo.SysDictTypePojo.SysDictTypePo;
import com.wr.domain.SysDictPojo.SysDictTypePojo.SysDictTypeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysDictTypeMapper extends BaseMapper<SysDictTypePo> {

    List<SysDictTypeVo> getDictTypeList(SysDictTypePo sysDictTypePo);
}
