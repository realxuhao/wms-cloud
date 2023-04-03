package com.bosch.binin.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

@Data
@TableName("transhipment_order")
public class TranshipmentOrder extends BaseEntity {

    private Long id;

    /**
     * 转运单号
     */
    private String orderNumber;

    /**
     * SSCC码
     */
    private String ssccNumber;

    /**
     * 料号
     */
    private String materialCode;

    /**
     * 状态:状态：0:未执行，1:已执行
     *
     */
    private Integer status;
    /**
     * deleteFlag
     */
    private Integer deleteFlag;

    private Long wareShiftId;

    private String carNb;

    private Long productWareShiftId;
}
