package com.wr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.domain.SysDictPojo.SysDictDataPojo.SysDictDataPo;
import com.wr.domain.SysDictPojo.SysDictDataPojo.SysDictDataVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysDictDataMapper extends BaseMapper<SysDictDataPo> {

    List<SysDictDataVo> getDictDataList(SysDictDataPo sysDictDataPo);
}
