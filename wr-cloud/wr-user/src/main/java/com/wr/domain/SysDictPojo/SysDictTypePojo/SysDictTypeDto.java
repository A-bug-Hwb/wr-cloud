package com.wr.domain.SysDictPojo.SysDictTypePojo;

import com.wr.domain.BaseEntityDto;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SysDictTypeDto extends BaseEntityDto {

    private String dictName;
    private String dictType;
    private String status;

}
