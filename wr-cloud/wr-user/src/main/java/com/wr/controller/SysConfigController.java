package com.wr.controller;

import com.wr.domain.SysConfigPojo.AddSysConfigDto;
import com.wr.domain.SysConfigPojo.SysConfigDto;
import com.wr.domain.SysConfigPojo.UpSysConfigDto;
import com.wr.result.R;
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
    public R getInfo(@PathVariable Long configId){
        return R.ok(iSysConfigService.selectConfigById(configId));
    }

    @ApiOperation("根据参数键名查询参数值")
    @GetMapping("/configKey/{configKey}")
    public R configKey(@PathVariable String configKey){
        return R.ok(iSysConfigService.selectConfigByKey(configKey));
    }

    @ApiOperation("添加系统参数")
    @PostMapping("/add")
    public R add(@RequestBody AddSysConfigDto addSysConfigDto){
        if (iSysConfigService.install(addSysConfigDto)){
            return R.ok("添加成功");
        }
        return R.fail("添加失败");
    }

    @ApiOperation("修改系统参数")
    @PutMapping("/update")
    public R update(@RequestBody UpSysConfigDto upSysConfigDto){
        return R.ok(iSysConfigService.updateInfo(upSysConfigDto));
    }

    @ApiOperation("删除系统参数")
    @DeleteMapping("/{configId}")
    public R delete(@PathVariable List<Long> configId){
        return R.ok(iSysConfigService.removeByIds(configId));
    }

    /**
     * 刷新参数缓存
     */
    @ApiOperation("刷新参数缓存")
    @GetMapping("/refreshCache")
    public R refreshCache()
    {
        iSysConfigService.resetConfigCache();
        return R.ok();
    }
}
