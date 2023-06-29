package com.bosch.masterdata.api.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class WorkloadVO {
    /**
     *
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "create_by")
    private String createBy;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "update_by")
    private String updateBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "update_time")
    private Date updateTime;



    /**
     * 物料、成品号
     */
    @ApiModelProperty(value = "code")
    private String code;

    /**
     *
     */
    @ApiModelProperty(value = "sscc_number")
    private String ssccNumber;

    /**
     *
     */
    @ApiModelProperty(value = "operation_type")
    private String operationType;

    /**
     * 上架
     */
    @ApiModelProperty(value = "上架")
    private Integer binIn;

    /**
     * 拣配下架
     */
    @ApiModelProperty(value = "拣配下架")
    private Integer binOut;

    /**
     * 其他下架
     */
    @ApiModelProperty(value = "其他下架")
    private Integer binOutOther;

    /**
     * 拆托
     */
    @ApiModelProperty(value = "拆托")
    private Integer palletSplit;

    /**
     * 翻托
     */
    @ApiModelProperty(value = "翻托")
    private Integer palletTurnover;

}
