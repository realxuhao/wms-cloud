package com.bosch.product.api.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.util.Date;

@Data
public class ProductReceiveDTO {
    /**
     * id
     */
    @ExcelIgnore
    private Long id;

    /**
     * 仓库
     */
    @ExcelProperty(value = "PlantNb")
    private String plantNb;

    @ExcelIgnore
    private String wareCode;

    /**
     * SSCC码
     */
    @ExcelProperty(value = "SSCCNumber")
    private String ssccNumber;

    /**
     * 物料号
     */
    @ExcelProperty(value = "MaterialNb")
    private String materialNb;

    /**
     * 批次号
     */
    @ExcelIgnore
    private String batchNb;



    /**
     * BBD(过期时间)
     */
    @ExcelProperty(value = "ExpiryDate")
    private String expireDate;


    /**
     * 生产批次号
     */
    @ExcelIgnore
    private Date productionDate;

    /**
     * 数量
     */
    @ExcelProperty(value = "Quantity")
    private Double quantity;

    /**
     * 单位
     */
    @ExcelProperty(value = "Unit")
    private String unit;



    /**
     * 来源PO号
     */
    @ExcelProperty(value = "FromProdOrder")
    private String fromProdOrder;




    /**
     * 关联的fileId
     */
    @ExcelIgnore
    private String fileId;

    /**
     * 移动类型
     */
    @ExcelIgnore
    private String moveType;
}
