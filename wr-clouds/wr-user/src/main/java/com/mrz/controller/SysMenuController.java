package com.wr.controller;

import com.wr.domain.SysMenuPojo.AddSysMenuDto;
import com.wr.domain.SysMenuPojo.SysMenuDto;
import com.wr.domain.SysMenuPojo.SysMenuVo;
import com.wr.domain.SysMenuPojo.UpSysMenuDto;
import com.wr.result.AjaxResult;
import com.wr.service.ISysMenuService;
import com.wr.utils.BeanUtil;
import com.wr.utils.SecurityUtils;
import com.wr.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/sysMenu")
public class SysMenuController extends BaseController {

    @Autowired
    private ISysMenuService iSysMenuService;

    @ApiOperation("获取菜单路由信息")
    @GetMapping("/getRouters")
    public AjaxResult getRouters()
    {
        List<SysMenuVo> menus = iSysMenuService.selectMenuTreeByUserId(SecurityUtils.getUserId());
        return success(iSysMenuService.buildMenus(menus));
    }

    @ApiOperation("获取菜单列表")
    @GetMapping("/list")
    public AjaxResult list(SysMenuDto sysMenuDto)
    {
        return success(iSysMenuService.selectMenuList(sysMenuDto, SecurityUtils.getUserId()));
    }

    @ApiOperation("菜单添加")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody AddSysMenuDto addSysMenuDto)
    {
        if (iSysMenuService.install(addSysMenuDto)){
            return success("添加成功");
        }
        return error("添加失败");
    }

    @ApiOperation("根据菜单id获取菜单详情")
    @GetMapping("/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId)
    {
        return success(BeanUtil.beanToBean(iSysMenuService.getById(menuId),new SysMenuVo()));
    }

    @ApiOperation("菜单修改")
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody UpSysMenuDto upSysMenuDto)
    {
        if (iSysMenuService.update(upSysMenuDto)){
            return success("修改成功");
        }
        return error("修改失败");
    }

    @ApiOperation("菜单删除")
    @DeleteMapping("/delete/{menuId}")
    public AjaxResult delete(@PathVariable Long menuId)
    {
        if (iSysMenuService.deleteMenu(menuId)){
            return success("删除成功");
        }
        return error("当前菜单下有子菜单，不允许删除");
    }

    /**
     * 获取菜单下拉树列表
     */
    @ApiOperation("获取菜单下拉树列表")
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysMenuDto sysMenuDto)
    {
        List<SysMenuVo> menus = iSysMenuService.selectMenuList(sysMenuDto, SecurityUtils.getUserId());
        return success(iSysMenuService.buildMenuTreeSelect(menus));
    }

    @ApiOperation("根据角色ID查询菜单下拉树结构")
    @GetMapping("/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable Long roleId)
    {
        List<SysMenuVo> menus = iSysMenuService.selectMenuList(new SysMenuDto(),SecurityUtils.getUserId());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", iSysMenuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", iSysMenuService.buildMenuTreeSelect(menus));
        return ajax;
    }
}
