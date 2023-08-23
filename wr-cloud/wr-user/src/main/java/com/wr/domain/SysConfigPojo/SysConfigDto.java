package com.wr.domain.SysConfigPojo;


import com.wr.domain.BaseEntityDto;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SysConfigDto extends BaseEntityDto {


    /** 参数名称 */
    private String configName;

    /** 参数键名 */
    private String configKey;

    /** 参数键值 */
    private String configValue;

    /** 系统内置（Y是 N否） */
    private String configType;

}
