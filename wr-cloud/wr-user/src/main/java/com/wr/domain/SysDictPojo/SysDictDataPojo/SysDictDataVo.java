package com.wr.domain.SysDictPojo.SysDictDataPojo;

import com.wr.domain.BaseEntityVo;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SysDictDataVo extends BaseEntityVo {

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
