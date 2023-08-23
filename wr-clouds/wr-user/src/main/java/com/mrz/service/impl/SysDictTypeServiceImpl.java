package com.wr.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wr.domain.SysDictPojo.SysDictTypePojo.*;
import com.wr.mapper.SysDictTypeMapper;
import com.wr.service.ISysDictTypeService;
import com.wr.utils.BeanUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictTypePo> implements ISysDictTypeService {

    @Resource
    private SysDictTypeMapper sysDictTypeMapper;


    @Override
    public List<SysDictTypeVo> getDictTypeList(SysDictTypeDto sysDictTypeDto) {
        return sysDictTypeMapper.getDictTypeList(BeanUtil.beanToBean(sysDictTypeDto,new SysDictTypePo()));
    }

    @Override
    public SysDictTypeVo getDictTypeInfo(Long dictId) {
        SysDictTypePo sysDictTypePo = sysDictTypeMapper.selectOne(Wrappers.lambdaQuery(SysDictTypePo.class).eq(SysDictTypePo::getDictId,dictId).last("limit 1"));
        return BeanUtil.beanToBean(sysDictTypePo,new SysDictTypeVo());
    }

    @Override
    public boolean addType(AddDictTypeDto addDictTypeDto) {
        SysDictTypePo sysDictTypePo = BeanUtil.beanToBean(addDictTypeDto,new SysDictTypePo());
        sysDictTypePo.setCreateTime(new Date());
        if (sysDictTypeMapper.insert(sysDictTypePo) > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateType(UpDictTypeDto upDictTypeDto) {
        SysDictTypePo sysDictTypePo = BeanUtil.beanToBean(upDictTypeDto,new SysDictTypePo());
        sysDictTypePo.setUpdateTime(new Date());
        if (sysDictTypeMapper.updateById(sysDictTypePo) > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(List<Long> dictIds) {
        return false;
    }
}
