package com.wr.domain.SysDictPojo.SysDictTypePojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("添加字典类型")
@Data
public class AddDictTypeDto {


    @ApiModelProperty(value = "字典类型名称", required = true)
    @NotNull
    @NotBlank(message = "字典类型名称不能为空")
    private String dictName;
    @ApiModelProperty(value = "字典类型键值", required = true)
    @NotNull
    @NotBlank(message = "字典类型键值不能为空")
    private String dictType;
    @ApiModelProperty(value = "字典类型状态", required = true)
    @NotNull
    @NotBlank(message = "请选择字典类型状态")
    private String status;
    @ApiModelProperty(value = "备注", required = true)
    private String remark;
}
