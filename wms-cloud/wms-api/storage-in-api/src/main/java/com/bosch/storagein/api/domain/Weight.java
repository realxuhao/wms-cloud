package com.bosch.storagein.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description: 称重数据
 * @author: xuhao
 * @create: 2023-02-17 09:39
 **/

@Data
@TableName("si_weight")
public class Weight {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String ip;
    private Integer port;
    private Double weight;
    private Date uploadTime;
    private int status;
    private String ssccNb;
    private Date useTime;

}
