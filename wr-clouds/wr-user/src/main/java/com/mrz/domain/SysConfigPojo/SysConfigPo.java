package com.wr.domain.SysConfigPojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wr.domain.BaseEntityPo;
import lombok.Data;
import lombok.ToString;

@Data
@TableName("sys_config")
@ToString
public class SysConfigPo extends BaseEntityPo {


    @TableId(type = IdType.AUTO)
    private Long configId;

    /** 参数名称 */
    private String configName;

    /** 参数键名 */
    private String configKey;

    /** 参数键值 */
    private String configValue;

    /** 系统内置（Y是 N否） */
    private String configType;
}
