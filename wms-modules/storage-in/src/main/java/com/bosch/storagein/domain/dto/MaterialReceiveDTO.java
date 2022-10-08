package com.bosch.storagein.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MaterialReceiveDTO extends PageDomain {

//    /** id */
//    @ApiModelProperty(value = "主键id")
//    private Long id;

    /** SSCC码 */
    @ApiModelProperty(value = "SSCC码")
    private String ssccNumber;

    /** 批次号 */
    @ApiModelProperty(value = "批次号")
    private String batchNumber;

    /** 物料号 */
    @ApiModelProperty(value = "物料号")
    private String materialNb;




//    /** BBD(过期时间) */
//    @ApiModelProperty(value = "BBD过期时间")
//    private String expireDate;

    /** 来源PO号 */
    @ApiModelProperty(value = "来源PO号")
    private String fromPurchaseOrder;

//    /** PO行号 */
//    @ApiModelProperty(value = "PO行号")
//    private String poNumberItem;
//
//    /** 上传人 */
//    @ApiModelProperty(value = "上传人")
//    private String upload_user;
//
//    /** 上传时间 */
//    @ApiModelProperty(value = "上传时间")
//    private Date uploadTime;
//
//    /** 更新人 */
//    @ApiModelProperty(value = "更新人")
//    private String update_user;
//
//    /** 更新时间 */
//    @ApiModelProperty(value = "更新时间")
//    private Date update_time;
//
//    /** 删除标记1:可用，0:删除 */
//    @ApiModelProperty(value = "删除标记1:可用，0:删除")
//    private Integer deleteFlag;
//
    /** 物料流转状态:0:待入库，1：已入库 */
    @ApiModelProperty(value = "物料流转状态:0:待入库，1：已入库")
    private Integer status;
}
