package com.bosch.storagein.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MaterialReceiveVO {

    /**
     * id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * SSCC码
     */
    @ApiModelProperty(value = "SSCC码")
    private String ssccNumber;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    private String materialNb;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNb;

    /**
     * BBD(过期时间)
     */
    @ApiModelProperty(value = "BBD过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer quantity;

    /**
     * 来源PO号
     */
    @ApiModelProperty(value = "来源PO号")
    private String fromPurchaseOrder;

    /**
     * PO行号
     */
    @ApiModelProperty(value = "PO行号")
    private String poNumberItem;

    /**
     * 上传人
     */
    @ApiModelProperty(value = "上传人")
    private String uploadUser;

    /**
     * 上传时间
     */
    @ApiModelProperty(value = "上传时间")
    private Date uploadTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateUser;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 删除标记1:可用，0:删除
     */
    @ApiModelProperty(value = "删除标记1:可用，0:删除")
    private Integer deleteFlag;

    /**
     * 物料流转状态:0:待入库，1：已入库
     */
    @ApiModelProperty(value = "物料流转状态:0:待入库，1：已入库")
    private Integer status;
}
