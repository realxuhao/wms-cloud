package com.bosch.product.api.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.opencsv.bean.CsvBindByName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 10:04
 **/

@Data
@TableName("bi_in_product")
public class ProductReceive extends BaseEntity {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ExcelProperty(value = "id")
    @CsvBindByName(column = "id")
    private Long id;

    /**
     * 仓库
     */
    @ExcelProperty(value = "PlantNb")
    @CsvBindByName(column = "\uFEFFPlantNb")
    private String plantNb;

    private String wareCode;

    /**
     * SSCC码
     */
    @ExcelProperty(value = "SSCCNumber")
    @CsvBindByName(column = "SSCCNumber")
    private String ssccNumber;

    /**
     * 物料号
     */
    @ExcelProperty(value = "MaterialNb")
    @CsvBindByName(column = "MaterialNb")
    private String materialNb;

    /**
     * 批次号
     */
    @ExcelProperty(value = "BatchNb")
    @CsvBindByName(column = "BatchNb")
    private String batchNb;



    /**
     * BBD(过期时间)
     */
    @ExcelProperty(value = "ExpiryDate")
    @CsvBindByName(column = "ExpiryDate")
    private String expireDate;


    /**
     * 生产批次号
     */

    private Date productionDate;

    /**
     * 数量
     */
    @ExcelProperty(value = "Quantity")
    @CsvBindByName(column = "Quantity")
    private Double quantity;

    /**
     * 单位
     */
    @ExcelProperty(value = "Unit")
    @CsvBindByName(column = "Unit")
    private String unit;



    /**
     * 来源PO号
     */
    @ExcelProperty(value = "FromProdOrder")
    @CsvBindByName(column = "FromProdOrder")
    private String fromProdOrder;

    /**
     * 删除标记1:可用，0:删除
     */
    @ExcelProperty(value = "deleteFlag")
    @CsvBindByName(column = "deleteFlag")
    private Integer deleteFlag;

    /**
     * 物料流转状态:0:待入库，1：已入库
     */
    @ExcelProperty(value = "status")
    @CsvBindByName(column = "status")
    private Integer status;

    /**
     * 关联的fileId
     */
    @ExcelProperty(value = "fileId")
    @CsvBindByName(column = "fileId")
    private String fileId;

    /**
     * 移动类型
     */
    private String moveType;
}
