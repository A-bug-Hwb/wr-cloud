package com.wr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wr.domain.SysDictPojo.SysDictTypePojo.*;

import java.util.List;

public interface ISysDictTypeService extends IService<SysDictTypePo> {

    List<SysDictTypeVo> getDictTypeList(SysDictTypeDto sysDictTypeDto);
    SysDictTypeVo getDictTypeInfo(Long dictId);

    boolean addType(AddDictTypeDto addDictTypeDto);

    boolean updateType(UpDictTypeDto upDictTypeDto);

    boolean delete(List<Long> dictIds);
}
