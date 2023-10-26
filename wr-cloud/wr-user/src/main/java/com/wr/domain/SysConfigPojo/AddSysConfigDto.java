package com.wr.domain.SysConfigPojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 郝文彬
 */
@Data
@ApiModel("参数添加实体")
public class AddSysConfigDto {


    /** 参数名称 */
    @ApiModelProperty(value = "参数名称",required = true)
    @NotBlank(message = "参数名称不能为空")
    private String configName;

    /** 参数键名 */
    @ApiModelProperty(value = "参数键名",required = true)
    @NotBlank(message = "参数键名不能为空")
    private String configKey;

    /** 参数键值 */
    @ApiModelProperty(value = "参数键值",required = true)
    @NotBlank(message = "参数键值不能为空")
    private String configValue;

    /** 系统内置（Y是 N否） */
    @ApiModelProperty(value = "系统内置（Y是 N否）",required = true)
    @NotBlank(message = "系统内置不能为空")
    private String configType;

    @ApiModelProperty(value = "备注",required = true)
    private String remark;
}
