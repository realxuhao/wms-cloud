package com.bosch.product.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: taojd
 * @create: 2024-04-03 16:11
 **/
@Data
public class ProductStockAdjustVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * cell
     */
    @ApiModelProperty(value = "cell")
    @Excel(name = "cell")
    private String cell;

    /**
     * 工厂
     */
    @ApiModelProperty(value = "工厂")
    @Excel(name = "工厂")
    private String plantNb;

    /**
     * SSCC码
     */
    @ApiModelProperty(value = "SSCC码")
    @Excel(name = "SSCC")
    private String ssccNumber;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    @Excel(name = "物料号")
    private String materialNb;

    /**
     * 物料类型
     */
    @ApiModelProperty(value = "物料类型")
    @Excel(name = "物料类型")
    private String materialType;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    @Excel(name = "物料名称")
    private String materialName;

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
    @Excel(name = "库存量",cellType = Excel.ColumnType.NUMERIC)
    private Double totalStock;

    /**
     * 冻结库存
     */
    @ApiModelProperty(value = "冻结库存")
    @Excel(name = "冻结库存",cellType = Excel.ColumnType.NUMERIC)
    private Double freezeStock;

    /**
     * 可用库存
     */
    @ApiModelProperty(value = "可用库存")
    @Excel(name = "可用库存",cellType = Excel.ColumnType.NUMERIC)
    private Double availableStock;

    /**
     * 箱 Tr 对应包装规格
     */
    @ApiModelProperty(value = "箱 Tr 对应包装规格")
    @Excel(name = "箱 Tr 对应包装规格",cellType = Excel.ColumnType.NUMERIC)
    private String boxSpecification;

    /**
     * 总库存
     */
    @ApiModelProperty(value = "库存量PCS")
    @Excel(name = "库存量PCS",cellType = Excel.ColumnType.NUMERIC)
    private Double pcsTotalStock;

    /**
     * 冻结库存
     */
    @ApiModelProperty(value = "冻结库存PCS")
    @Excel(name = "冻结库存PCS",cellType = Excel.ColumnType.NUMERIC)
    private Double pcsFreezeStock;

    /**
     * 可用库存
     */
    @ApiModelProperty(value = "可用库存PCS")
    @Excel(name = "可用库存PCS",cellType = Excel.ColumnType.NUMERIC)
    private Double pcsAvailableStock;

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
     * 质检状态
     */
    @ApiModelProperty(value = "质检状态")
    @Excel(name = "质检状态")
    private String qualityStatus;

    /**
     * 类型
     */
    @ApiModelProperty(value = "调整类型")
    @Excel(name = "类型",readConverterExp = "0=质检取样,1=取样,2=报废,3=整托出库,4=其它,5=库存恢复")
    private Integer type;

    /**
     * 领用理由
     */
    @ApiModelProperty(value = "领用理由")
    @Excel(name = "领用理由")
    private String useReason;

    /** 创建者 */
    @ApiModelProperty(value = "创建者")
    @Excel(name = "创建者")
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间",dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
