package com.wr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wr.domain.RouterPojo.RouterVo;
import com.wr.domain.RouterPojo.TreeSelect;
import com.wr.domain.SysMenuPojo.*;

import java.util.List;
import java.util.Set;

public interface ISysMenuService extends IService<SysMenuPo> {

    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenuVo> selectMenuTreeByUserId(Long userId);

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    public List<RouterVo> buildMenus(List<SysMenuVo> menus);


    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenuVo> selectMenuList(SysMenuDto sysMenuDto, Long userId);

    boolean install(AddSysMenuDto addSysMenuDto);

    boolean update(UpSysMenuDto upSysMenuDto);

    boolean deleteMenu(Long menuId);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenuVo> menus);


    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    public List<Long> selectMenuListByRoleId(Long roleId);

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByUserId(Long userId);


    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    public List<SysMenuVo> buildMenuTree(List<SysMenuVo> menus);


}
