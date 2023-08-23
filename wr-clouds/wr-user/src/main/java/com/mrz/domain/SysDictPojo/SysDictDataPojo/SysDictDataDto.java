package com.wr.domain.SysDictPojo.SysDictDataPojo;


import com.wr.domain.BaseEntityDto;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SysDictDataDto extends BaseEntityDto {

    private String dictLabel;
    private String dictType;
    private String status;

}
