package com.wr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wr.domain.SysDictPojo.SysDictDataPojo.*;

import java.util.List;

public interface ISysDictDataService extends IService<SysDictDataPo> {
    List<SysDictDataVo> getDictDataList(SysDictDataDto sysDictDataDto);

    List<SysDictDataVo> getDictDataSelectList(String dictType);

    boolean addData(AddDictDataDto addDictDataDto);

    boolean updateData(UpDictDataDto upDictDataDto);
}
