package com.bosch.masterdata.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 物料信息对象 md_material
 * 
 * @author xuhao
 * @date 2022-09-22
 */
public class Material extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 物料类型代码 */
    @Excel(name = "物料类型代码")
    private String code;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String name;

    /** 最小包装数量 */
    @Excel(name = "最小包装数量")
    private Long minPackageNumber;

    /** 物料类型id */
    @Excel(name = "物料类型id")
    private Long materialTypeId;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

    /** 物料防错方式 */
    @Excel(name = "物料防错方式")
    private String errorProofingMethod;

    /** 是否带托盘 */
    @Excel(name = "是否带托盘")
    private Long hasPallet;

    /** 是否绑定托盘 */
    @Excel(name = "是否绑定托盘")
    private Long bindPallet;

    /** 包装重量 */
    @Excel(name = "包装重量")
    private Long packageWeight;

    /** 托盘重量 */
    @Excel(name = "托盘重量")
    private Long palletWeight;

    /** 来料总重量（每托） */
    @Excel(name = "来料总重量", readConverterExp = "每=托")
    private Long totalWeight;

    /** 最小包装重量(净重) */
    @Excel(name = "最小包装重量(净重)")
    private BigDecimal minPackageNetWeight;

    /** 允许负偏差比例【绝对值】 */
    @Excel(name = "允许负偏差比例【绝对值】")
    private BigDecimal lessDeviationRatio;

    /** 允许正偏差比例【绝对值】 */
    @Excel(name = "允许正偏差比例【绝对值】")
    private BigDecimal moreDeviationRatio;

    /** 状态（1：启用，0：停用） */
    @Excel(name = "状态", readConverterExp = "1=：启用，0：停用")
    private Long status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setMinPackageNumber(Long minPackageNumber) 
    {
        this.minPackageNumber = minPackageNumber;
    }

    public Long getMinPackageNumber() 
    {
        return minPackageNumber;
    }
    public void setMaterialTypeId(Long materialTypeId) 
    {
        this.materialTypeId = materialTypeId;
    }

    public Long getMaterialTypeId() 
    {
        return materialTypeId;
    }
    public void setUnit(String unit) 
    {
        this.unit = unit;
    }

    public String getUnit() 
    {
        return unit;
    }
    public void setErrorProofingMethod(String errorProofingMethod) 
    {
        this.errorProofingMethod = errorProofingMethod;
    }

    public String getErrorProofingMethod() 
    {
        return errorProofingMethod;
    }
    public void setHasPallet(Long hasPallet) 
    {
        this.hasPallet = hasPallet;
    }

    public Long getHasPallet() 
    {
        return hasPallet;
    }
    public void setBindPallet(Long bindPallet) 
    {
        this.bindPallet = bindPallet;
    }

    public Long getBindPallet() 
    {
        return bindPallet;
    }
    public void setPackageWeight(Long packageWeight) 
    {
        this.packageWeight = packageWeight;
    }

    public Long getPackageWeight() 
    {
        return packageWeight;
    }
    public void setPalletWeight(Long palletWeight) 
    {
        this.palletWeight = palletWeight;
    }

    public Long getPalletWeight() 
    {
        return palletWeight;
    }
    public void setTotalWeight(Long totalWeight) 
    {
        this.totalWeight = totalWeight;
    }

    public Long getTotalWeight() 
    {
        return totalWeight;
    }
    public void setMinPackageNetWeight(BigDecimal minPackageNetWeight) 
    {
        this.minPackageNetWeight = minPackageNetWeight;
    }

    public BigDecimal getMinPackageNetWeight() 
    {
        return minPackageNetWeight;
    }
    public void setLessDeviationRatio(BigDecimal lessDeviationRatio) 
    {
        this.lessDeviationRatio = lessDeviationRatio;
    }

    public BigDecimal getLessDeviationRatio() 
    {
        return lessDeviationRatio;
    }
    public void setMoreDeviationRatio(BigDecimal moreDeviationRatio) 
    {
        this.moreDeviationRatio = moreDeviationRatio;
    }

    public BigDecimal getMoreDeviationRatio() 
    {
        return moreDeviationRatio;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("name", getName())
            .append("minPackageNumber", getMinPackageNumber())
            .append("materialTypeId", getMaterialTypeId())
            .append("unit", getUnit())
            .append("errorProofingMethod", getErrorProofingMethod())
            .append("hasPallet", getHasPallet())
            .append("bindPallet", getBindPallet())
            .append("packageWeight", getPackageWeight())
            .append("palletWeight", getPalletWeight())
            .append("totalWeight", getTotalWeight())
            .append("minPackageNetWeight", getMinPackageNetWeight())
            .append("lessDeviationRatio", getLessDeviationRatio())
            .append("moreDeviationRatio", getMoreDeviationRatio())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("status", getStatus())
            .append("remark", getRemark())
            .toString();
    }
}
