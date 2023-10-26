package com.wr.controller;

import com.wr.domain.SysMenuPojo.AddSysMenuDto;
import com.wr.domain.SysMenuPojo.SysMenuDto;
import com.wr.domain.SysMenuPojo.SysMenuVo;
import com.wr.domain.SysMenuPojo.UpSysMenuDto;
import com.wr.result.R;
import com.wr.service.ISysMenuService;
import com.wr.utils.SecurityUtils;
import com.wr.utils.bean.BeanUtils;
import com.wr.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/sysMenu")
public class SysMenuController extends BaseController {

    @Autowired
    private ISysMenuService iSysMenuService;

    @ApiOperation("获取菜单路由信息")
    @GetMapping("/getRouters")
    public R getRouters()
    {
        List<SysMenuVo> menus = iSysMenuService.selectMenuTreeByUserId(SecurityUtils.getUserId());
        return R.ok(iSysMenuService.buildMenus(menus));
    }

    @ApiOperation("获取菜单列表")
    @GetMapping("/list")
    public R list(SysMenuDto sysMenuDto)
    {
        return R.ok(iSysMenuService.selectMenuList(sysMenuDto, SecurityUtils.getUserId()));
    }

    @ApiOperation("菜单添加")
    @PostMapping("/add")
    public R add(@RequestBody AddSysMenuDto addSysMenuDto)
    {
        if (iSysMenuService.install(addSysMenuDto)){
            return R.ok("添加成功");
        }
        return R.fail("添加失败");
    }

    @ApiOperation("根据菜单id获取菜单详情")
    @GetMapping("/{menuId}")
    public R getInfo(@PathVariable Long menuId)
    {
        return R.ok(BeanUtils.copyDataProp(iSysMenuService.getById(menuId),new SysMenuVo()));
    }

    @ApiOperation("菜单修改")
    @PutMapping("/edit")
    public R edit(@RequestBody UpSysMenuDto upSysMenuDto)
    {
        if (iSysMenuService.update(upSysMenuDto)){
            return R.ok("修改成功");
        }
        return R.fail("修改失败");
    }

    @ApiOperation("菜单删除")
    @DeleteMapping("/delete/{menuId}")
    public R delete(@PathVariable Long menuId)
    {
        if (iSysMenuService.deleteMenu(menuId)){
            return R.ok("删除成功");
        }
        return R.fail("当前菜单下有子菜单，不允许删除");
    }

    /**
     * 获取菜单下拉树列表
     */
    @ApiOperation("获取菜单下拉树列表")
    @GetMapping("/treeselect")
    public R treeselect(SysMenuDto sysMenuDto)
    {
        List<SysMenuVo> menus = iSysMenuService.selectMenuList(sysMenuDto, SecurityUtils.getUserId());
        return R.ok(iSysMenuService.buildMenuTreeSelect(menus));
    }

    @ApiOperation("根据角色ID查询菜单下拉树结构")
    @GetMapping("/roleMenuTreeselect/{roleId}")
    public R roleMenuTreeselect(@PathVariable Long roleId)
    {
        List<SysMenuVo> menus = iSysMenuService.selectMenuList(new SysMenuDto(),SecurityUtils.getUserId());
        Map<String, Object> map = new HashMap<>(2);
        map.put("checkedKeys", iSysMenuService.selectMenuListByRoleId(roleId));
        map.put("menus", iSysMenuService.buildMenuTreeSelect(menus));
        return R.ok(map);
    }
}
