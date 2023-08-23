package com.wr.domain.SysDictPojo.SysDictDataPojo;

import lombok.Data;

@Data
public class AddDictDataDto {

    private Long dictSort;
    private String dictLabel;
    private String dictValue;
    private String dictType;
    private String cssClass;
    private String listClass;
    private String isDefault;
    private String status;
    private String remark;
}
