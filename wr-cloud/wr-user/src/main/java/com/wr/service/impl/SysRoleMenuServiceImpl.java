package com.wr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wr.domain.SysRoleMenuPojo.SysRoleMenuPo;
import com.wr.mapper.SysRoleMenuMapper;
import com.wr.service.ISysRoleMenuService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenuPo> implements ISysRoleMenuService {
}
