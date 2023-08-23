package com.wr.domain.SysDictPojo.SysDictTypePojo;

import com.wr.domain.BaseEntityVo;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SysDictTypeVo extends BaseEntityVo {

    private Long dictId;
    private String dictName;
    private String dictType;
    private String status;

}
