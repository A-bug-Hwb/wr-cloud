package com.wr.domain.SysDictPojo.SysDictDataPojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wr.domain.BaseEntityPo;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("sys_dict_data")
public class SysDictDataPo extends BaseEntityPo {

    @TableId(type = IdType.AUTO)
    private Long dictCode;
    private Long dictSort;
    private String dictLabel;
    private String dictValue;
    private String dictType;
    private String cssClass;
    private String listClass;
    private String isDefault;
    private String status;

}
