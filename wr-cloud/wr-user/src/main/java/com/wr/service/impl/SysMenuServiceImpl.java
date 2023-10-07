package com.wr.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wr.constants.Constants;
import com.wr.constants.UserConstants;
import com.wr.domain.RouterPojo.MetaVo;
import com.wr.domain.RouterPojo.RouterVo;
import com.wr.domain.RouterPojo.TreeSelect;
import com.wr.domain.SysMenuPojo.*;
import com.wr.domain.SysRoleMenuPojo.SysRoleMenuPo;
import com.wr.domain.SysRolePojo.SysRolePo;
import com.wr.mapper.SysMenuMapper;
import com.wr.mapper.SysRoleMapper;
import com.wr.mapper.SysRoleMenuMapper;
import com.wr.mapper.SysUserMapper;
import com.wr.service.ISysMenuService;
import com.wr.utils.BeanUtil;
import com.wr.utils.SecurityUtils;
import com.wr.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenuPo> implements ISysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenuVo> selectMenuTreeByUserId(Long userId) {
        List<SysMenuVo> menus = null;
        if (sysUserMapper.isAdmin(userId))
        {
            menus = sysMenuMapper.selectMenuTreeAll();
        }
        else
        {
            menus = sysMenuMapper.selectMenuTreeByUserId(userId);
        }
        return getChildPerms(menus, 0);
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenuVo> menus)
    {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenuVo menu : menus)
        {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getIsCache().equals("1"), menu.getPath()));
            List<SysMenuVo> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType()))
            {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
            else if (isMenuFrame(menu))
            {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getIsCache().equals("1"), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            else if (menu.getParentId().intValue() == 0 && isInnerLink(menu))
            {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    @Override
    public List<SysMenuVo> selectMenuList(SysMenuDto sysMenuDto, Long userId) {
        List<SysMenuVo> menuList = new ArrayList<>();
        SysMenuPo sysMenuPo = BeanUtil.beanToBean(sysMenuDto,new SysMenuPo());
        if (sysUserMapper.isAdmin(userId)){
            menuList = sysMenuMapper.selectMenuList(sysMenuPo);
        }else {
            sysMenuPo.getParams().put("userId", userId);
            menuList = sysMenuMapper.selectMenuListByUserId(sysMenuPo);
        }
        return menuList;
    }

    @Override
    public boolean install(AddSysMenuDto addSysMenuDto) {
        SysMenuPo sysMenuPo = BeanUtil.beanToBean(addSysMenuDto,new SysMenuPo());
        sysMenuPo.setCreateBy(SecurityUtils.getUsername());
        sysMenuPo.setCreateTime(new Date());
        if (sysMenuMapper.insert(sysMenuPo) > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean update(UpSysMenuDto upSysMenuDto) {
        SysMenuPo sysMenuPo = BeanUtil.beanToBean(upSysMenuDto,new SysMenuPo());
        sysMenuPo.setUpdateBy(SecurityUtils.getUsername());
        sysMenuPo.setUpdateTime(new Date());
        if (sysMenuMapper.updateById(sysMenuPo) > 0){
            return true;
        }
        return false;
    }


    @Override
    public boolean deleteMenu(Long menuId) {
        if (sysMenuMapper.selectCount(Wrappers.lambdaQuery(SysMenuPo.class).eq(SysMenuPo::getParentId,menuId)) > 0){
            return false;
        }
        sysRoleMenuMapper.delete(Wrappers.lambdaQuery(SysRoleMenuPo.class).eq(SysRoleMenuPo::getMenuId,menuId));
        sysMenuMapper.deleteById(menuId);
        return true;
    }

    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenuVo> menus) {
        List<SysMenuVo> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> selectMenuListByRoleId(Long roleId)
    {
        SysRolePo role = sysRoleMapper.selectById(roleId);
        return sysMenuMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    }



    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByRoleId(Long roleId)
    {
        List<String> perms = sysMenuMapper.selectMenuPermsByRoleId(roleId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId)
    {
        List<String> perms = sysMenuMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }




    @Override
    public List<SysMenuVo> buildMenuTree(List<SysMenuVo> menus) {
        List<SysMenuVo> returnList = new ArrayList<SysMenuVo>();
        List<Long> tempList = menus.stream().map(SysMenuVo::getMenuId).collect(Collectors.toList());
        for (Iterator<SysMenuVo> iterator = menus.iterator(); iterator.hasNext();)
        {
            SysMenuVo menu = (SysMenuVo) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId()))
            {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = menus;
        }
        return returnList;
    }
    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t 子节点
     */
    private void recursionFn(List<SysMenuVo> list, SysMenuVo t)
    {
        // 得到子节点列表
        List<SysMenuVo> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenuVo tChild : childList)
        {
            if (getChildList(list, tChild).size() > 0)
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenuVo> getChildList(List<SysMenuVo> list, SysMenuVo t)
    {
        List<SysMenuVo> tlist = new ArrayList<SysMenuVo>();
        Iterator<SysMenuVo> it = list.iterator();
        while (it.hasNext())
        {
            SysMenuVo n = (SysMenuVo) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }




    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenuVo> getChildPerms(List<SysMenuVo> list, int parentId)
    {
        List<SysMenuVo> returnList = new ArrayList<SysMenuVo>();
        for (SysMenuVo sysMenuVo:list){
            if (sysMenuVo.getParentId() == parentId){
                returnList.add(sysMenuVo);
            }
        }
        if (returnList.size()>0){
            Stack<List<SysMenuVo>> stack = new Stack<>();
            stack.push(returnList);
            while (!stack.isEmpty()){
                List<SysMenuVo> sysMenuVoList = stack.pop();
                for (SysMenuVo sysMenuVo:sysMenuVoList){
                    List<SysMenuVo> cSysMenu = list.stream().filter(val-> val.getParentId().equals(sysMenuVo.getMenuId())).collect(Collectors.toList());
                    if (cSysMenu.size()>0){
                        sysMenuVo.setChildren(cSysMenu);
                        stack.push(cSysMenu);
                    }
                }
            }
        }
        return returnList;
    }


    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenuVo menu)
    {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu))
        {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenuVo menu)
    {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu))
        {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame()))
        {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu))
        {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenuVo menu)
    {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu))
        {
            component = menu.getComponent();
        }
        else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu))
        {
            component = UserConstants.INNER_LINK;
        }
        else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu))
        {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }
    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenuVo menu)
    {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }
    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenuVo menu)
    {
        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenuVo menu)
    {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return 替换后的内链域名
     */
    public String innerLinkReplaceEach(String path)
    {
        return StringUtils.replaceEach(path, new String[] { Constants.HTTP, Constants.HTTPS, Constants.WWW, "." },
                new String[] { "", "", "", "/" });
    }

}
