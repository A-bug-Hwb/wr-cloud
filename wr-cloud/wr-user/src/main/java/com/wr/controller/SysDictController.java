package com.wr.controller;


import com.wr.annotation.RequiresPermissions;
import com.wr.domain.SysDictPojo.SysDictDataPojo.AddDictDataDto;
import com.wr.domain.SysDictPojo.SysDictDataPojo.SysDictDataDto;
import com.wr.domain.SysDictPojo.SysDictDataPojo.SysDictDataVo;
import com.wr.domain.SysDictPojo.SysDictDataPojo.UpDictDataDto;
import com.wr.domain.SysDictPojo.SysDictTypePojo.AddDictTypeDto;
import com.wr.domain.SysDictPojo.SysDictTypePojo.SysDictTypeDto;
import com.wr.domain.SysDictPojo.SysDictTypePojo.UpDictTypeDto;
import com.wr.result.R;
import com.wr.service.ISysDictDataService;
import com.wr.service.ISysDictTypeService;
import com.wr.utils.bean.BeanUtils;
import com.wr.web.controller.BaseController;
import com.wr.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "字典类型管理")
@RestController
@RequestMapping("/sysDict")
public class SysDictController extends BaseController {

    @Autowired
    private ISysDictTypeService iSysDictTypeService;

    @Autowired
    private ISysDictDataService iSysDictDataService;


    @ApiOperation("查询字典类型列表")
    @GetMapping("/getDictTypeList")
    public TableDataInfo getDictTypeList(SysDictTypeDto sysDictTypeDto) {
        startPage();
        return getDataTable(iSysDictTypeService.getDictTypeList(sysDictTypeDto));
    }

    @ApiOperation("查询字典类型详细")
    @GetMapping("/getDictTypeInfo/{dictId}")
    public R getDictTypeInfo(@PathVariable Long dictId) {
        return R.ok(iSysDictTypeService.getDictTypeInfo(dictId));
    }

    @ApiOperation("新增字典类型")
    @PostMapping("/addType")
    public R addType(@RequestBody AddDictTypeDto addDictTypeDto) {
        if (iSysDictTypeService.addType(addDictTypeDto)) {
            return R.ok("新增字典类型成功");
        }
        return R.fail("新增字典类型失败");
    }

    @ApiOperation("修改字典类型")
    @RequiresPermissions("system:dict:edit")
    @PutMapping("/updateType")
    public R updateType(@RequestBody UpDictTypeDto upDictTypeDto) {
        if (iSysDictTypeService.updateType(upDictTypeDto)) {
            return R.ok("修改字典类型成功");
        }
        return R.fail("修改字典类型失败");
    }

    @ApiOperation("删除字典类型")
    @RequiresPermissions("system:dict:remove")
    @DeleteMapping("/delType/{dictIds}")
    public R delType(@PathVariable List<Long> dictIds) {
        if (iSysDictTypeService.removeByIds(dictIds)) {
            return R.ok("删除字典类型成功");
        }
        return R.fail("删除字典类型失败");
    }

    @ApiOperation("获取字典选择框列表")
    @GetMapping("/getDictTypeSelectList")
    public R getDictTypeSelectList() {
        return R.ok(iSysDictTypeService.getDictTypeList(new SysDictTypeDto()));
    }


    @ApiOperation("根据字典类型查询字典数据信息")
    @GetMapping("/getDictDataSelectList/{dictType}")
    public R getDictDataSelectList(@PathVariable String dictType) {
        return R.ok(iSysDictDataService.getDictDataSelectList(dictType));
    }

    @ApiOperation("获取字典数据列表")
    @GetMapping("/getDictDataList")
    public TableDataInfo getDictDataList(SysDictDataDto sysDictDataDto) {
        startPage();
        return getDataTable(iSysDictDataService.getDictDataList(sysDictDataDto));
    }

    @ApiOperation("添加字典数据")
    @PostMapping("/addData")
    public R addData(@RequestBody AddDictDataDto addDictDataDto) {
        if (iSysDictDataService.addData(addDictDataDto)) {
            return R.ok("添加成功");
        }
        return R.fail("添加失败");
    }

    @ApiOperation("获取字典数据详情")
    @GetMapping("/getData/{dictCode}")
    public R getData(@PathVariable Long dictCode) {
        return R.ok(BeanUtils.copyDataProp(iSysDictDataService.getById(dictCode),new SysDictDataVo()));
    }

    @ApiOperation("修改字典数据")
    @PutMapping("/updateData")
    public R updateData(@RequestBody UpDictDataDto upDictDataDto) {
        if (iSysDictDataService.updateData(upDictDataDto)) {
            return R.ok("修改成功");
        }
        return R.fail("修改失败");
    }

    @ApiOperation("删除字典数据")
    @DeleteMapping("/deleteData/{dictCode}")
    public R deleteData(@PathVariable List<Long> dictCode) {
        if (iSysDictDataService.removeByIds(dictCode)) {
            return R.ok("删除成功");
        }
        return R.fail("删除失败");
    }
}
