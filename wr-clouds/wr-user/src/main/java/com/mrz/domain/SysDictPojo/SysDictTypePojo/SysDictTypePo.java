package com.wr.domain.SysDictPojo.SysDictTypePojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wr.domain.BaseEntityPo;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("sys_dict_type")
public class SysDictTypePo extends BaseEntityPo {

    @TableId(type = IdType.AUTO)
    private Long dictId;
    private String dictName;
    private String dictType;
    private String status;

}
