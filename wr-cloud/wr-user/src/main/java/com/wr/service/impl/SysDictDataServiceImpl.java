package com.wr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wr.domain.SysDictPojo.SysDictDataPojo.*;
import com.wr.mapper.SysDictDataMapper;
import com.wr.service.ISysDictDataService;
import com.wr.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictDataPo> implements ISysDictDataService {

    @Resource
    private SysDictDataMapper sysDictDataMapper;

    @Override
    public List<SysDictDataVo> getDictDataList(SysDictDataDto sysDictDataDto) {
        return sysDictDataMapper.getDictDataList(BeanUtils.copyDataProp(sysDictDataDto,new SysDictDataPo()));
    }

    @Override
    public List<SysDictDataVo> getDictDataSelectList(String dictType) {
        SysDictDataPo sysDictDataPo = new SysDictDataPo();
        sysDictDataPo.setDictType(dictType);
        return sysDictDataMapper.getDictDataList(sysDictDataPo);
    }

    @Override
    public boolean addData(AddDictDataDto addDictDataDto) {
        SysDictDataPo sysDictDataPo = BeanUtils.copyDataProp(addDictDataDto,new SysDictDataPo());
        sysDictDataPo.setCreateTime(new Date());
        if (sysDictDataMapper.insert(sysDictDataPo) > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateData(UpDictDataDto upDictDataDto) {
        SysDictDataPo sysDictDataPo = BeanUtils.copyDataProp(upDictDataDto,new SysDictDataPo());
        sysDictDataPo.setUpdateTime(new Date());
        if (sysDictDataMapper.updateById(BeanUtils.copyDataProp(upDictDataDto,new SysDictDataPo())) > 0){
            return true;
        }
        return false;
    }
}
