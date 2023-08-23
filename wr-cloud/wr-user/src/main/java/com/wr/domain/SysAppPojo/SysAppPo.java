package com.wr.domain.SysAppPojo;

import com.wr.domain.BaseEntityPo;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SysAppPo extends BaseEntityPo {

    private Long appId;
    private String appName;
    private String status;
}
