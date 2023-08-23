package com.wr.controller;

import com.wr.domain.SysConfigPojo.AddSysConfigDto;
import com.wr.domain.SysConfigPojo.SysConfigDto;
import com.wr.domain.SysConfigPojo.UpSysConfigDto;
import com.wr.result.AjaxResult;
import com.wr.service.ISysConfigService;
import com.wr.web.controller.BaseController;
import com.wr.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "配置管理")
@RestController
@RequestMapping("/sysConfig")
public class SysConfigController extends BaseController {

    @Autowired
    private ISysConfigService iSysConfigService;

    @ApiOperation("系统参数列表")
    @GetMapping("/list")
    public TableDataInfo list(SysConfigDto sysConfigDto){
        startPage();
        return getDataTable(iSysConfigService.selectConfigList(sysConfigDto));
    }

    @ApiOperation("获取系统参数详情")
    @GetMapping("/{configId}")
    public AjaxResult add(@PathVariable Long configId){
        return success(iSysConfigService.selectConfigById(configId));
    }

    @ApiOperation("根据参数键名查询参数值")
    @GetMapping("/configKey/{configKey}")
    public AjaxResult configKey(@PathVariable String configKey){
        return success(iSysConfigService.selectConfigByKey(configKey));
    }

    @ApiOperation("添加系统参数")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody AddSysConfigDto addSysConfigDto){
        if (iSysConfigService.install(addSysConfigDto)){
            return success("添加成功");
        }
        return error("添加失败");
    }

    @ApiOperation("修改系统参数")
    @PutMapping("/update")
    public AjaxResult update(@RequestBody UpSysConfigDto upSysConfigDto){
        return success(iSysConfigService.updateInfo(upSysConfigDto));
    }

    @ApiOperation("删除系统参数")
    @DeleteMapping("/{configId}")
    public AjaxResult delete(@PathVariable List<Long> configId){
        return success(iSysConfigService.removeByIds(configId));
    }

    /**
     * 刷新参数缓存
     */
    @ApiOperation("刷新参数缓存")
    @GetMapping("/refreshCache")
    public AjaxResult refreshCache()
    {
        iSysConfigService.resetConfigCache();
        return success();
    }
}
