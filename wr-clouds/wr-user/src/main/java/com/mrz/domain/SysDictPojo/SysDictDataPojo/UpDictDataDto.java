package com.wr.domain.SysDictPojo.SysDictDataPojo;

import lombok.Data;

@Data
public class UpDictDataDto {


    private Long dictCode;
    private Long dictSort;
    private String dictLabel;
    private String dictValue;
    private String cssClass;
    private String listClass;
    private String isDefault;
    private String status;
    private String remark;
}
