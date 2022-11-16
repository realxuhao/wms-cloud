package com.bosch.storagein.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MaterialReceiveDTO extends PageDomain {

//    /** id */
//    @ApiModelProperty(value = "主键id")
//    private Long id;


    @ApiModelProperty(value = "工厂")
    private String plantNb;



    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    private String materialNb;

    /**
     * sscc码
     */
    @ApiModelProperty(value = "sscc码")
    private String ssccNumber;

    @ApiModelProperty(value = "批次")
    private String batchNb;


    /**
     * 来源PO号
     */
    @ApiModelProperty(value = "来源PO号")
    private String fromPurchaseOrder;

    /**
     * BBD(过期时间)
     */
    @ApiModelProperty(value = "BBD过期开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startExpireDate;


    /**
     * BBD(过期时间)
     */
    @ApiModelProperty(value = "BBD过期结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endExpireDate;

    /** PO行号 */
    @ApiModelProperty(value = "PO行号")
    private String poNumberItem;
//
//    /** 上传人 */
//    @ApiModelProperty(value = "上传人")
//    private String upload_user;
//
    /** 上传时间 */
    @ApiModelProperty(value = "开始上传时间")
    private Date startUploadTime;

    /** 上传时间 */
    @ApiModelProperty(value = "结束上传时间")
    private Date endUploadTime;
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



    /**
     * 物料流转状态:0:待入库，1：已入库
     */
    @ApiModelProperty(value = "物料流转状态:0:待入库，1：已入库")
    private Integer status;

    @ApiModelProperty(value = "是否可编辑")
    private Integer editFlag;
}
