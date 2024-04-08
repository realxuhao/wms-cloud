package com.bosch.binin.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: taojd
 * @create: 2024-04-01 15:40
 **/
@Data
public class StockAdjustVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 工厂
     */
    @ApiModelProperty(value = "工厂")
    @Excel(name = "工厂")
    private String plantNb;

    /**
     * cell
     */
    @ApiModelProperty(value = "cell")
    @Excel(name = "cell")
    private String cell;

    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    @Excel(name = "仓库编码")
    private String wareCode;


    /**
     * 存储区编码
     */
    @ApiModelProperty(value = "存储区编码")
    @Excel(name = "存储区编码")
    private String areaCode;

    /**
     * SSCC码
     */
    @ApiModelProperty(value = "SSCC码")
    @Excel(name = "sscc")
    private String ssccNumber;

    /**
     * 跨编码
     */
    @ApiModelProperty(value = "跨编码")
    @Excel(name = "跨编码")
    private String frameCode;

    /**
     * 库位编码
     */
    @ApiModelProperty(value = "库位编码")
    @Excel(name = "库位编码")
    private String binCode;

    /**
     * 托盘编码
     */
//    @ApiModelProperty(value = "托盘编码")
//    @Excel(name = "托盘编码")
//    private String palletCode;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料编码")
    @Excel(name = "物料编码")
    private String materialNb;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    @Excel(name = "批次号")
    private String batchNb;

    /**
     * 质检状态
     */
    @ApiModelProperty(value = "质检状态")
    @Excel(name = "质检状态")
    private String qualityStatus;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "保质/有效期",dateFormat = "yyyy-MM-dd")
    private Date expireDate;

    /**
     * 总库存
     */
    @ApiModelProperty(value = "库存量")
    @Excel(name = "库存量")
    private Double totalStock;

    /**
     * 冻结库存
     */
    @ApiModelProperty(value = "冻结库存")
    @Excel(name = "冻结库存")
    private Double freezeStock;

    /**
     * 可用库存
     */
    @ApiModelProperty(value = "可用库存")
    @Excel(name = "可用库存")
    private Double availableStock;

    /**
     * 调整后总库存
     */
    @ApiModelProperty(value = "调整后总库存")
    @Excel(name = "调整后总库存")
    private Double adjustTotalStock;

    /**
     * 调整后冻结库存
     */
    @ApiModelProperty(value = "调整后冻结库存")
    @Excel(name = "调整后冻结库存")
    private Double adjustFreezeStock;

    /**
     * 调整后可用库存
     */
    @ApiModelProperty(value = "调整后可用库存")
    @Excel(name = "调整后可用库存")
    private Double adjustAvailableStock;

    /**
     * 类型 0:领用 1:报废 2:其它 3:玻璃瓶配送到产线
     */
    @ApiModelProperty(value = "调整类型")
    @Excel(name = "类型",readConverterExp = "0=领用,1=报废,2=其它,3=玻璃瓶配送到产线")
    private Integer type;

    /** 创建者 */
    @ApiModelProperty(value = "创建者")
    @Excel(name = "操作人")
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "操作时间",dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}
