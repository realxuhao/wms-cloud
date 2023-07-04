package com.bosch.binin.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description: 手工创建转储单
 * @author: xuhao
 * @create: 2022-12-20 12:54
 **/
@Data
@TableName("manual_transfer_order")
public class ManualTransferOrder extends BaseEntity {

    private Long id;
    private String sourcePlantNb;
    private String sourceWareCode;
    private String sourceAreaCode;
    private String sourceBinCode;
    private String moveType;
    private String ssccNb;
    private String materialNb;
    private String targetAreaCode;
    private String targetBinCode;
    private int type;
    private Integer status;

    private Integer deleteFlag;

    private int materialProductType;
}
