package com.wr.domain.SysConfigPojo;

import lombok.Data;

@Data
public class UpSysConfigDto {

    private Long configId;
    /** 参数名称 */
    private String configName;

    /** 参数键名 */
    private String configKey;

    /** 参数键值 */
    private String configValue;

    /** 系统内置（Y是 N否） */
    private String configType;

    private String remark;
}
