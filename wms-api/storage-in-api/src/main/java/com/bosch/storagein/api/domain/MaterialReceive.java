package com.bosch.storagein.api.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.opencsv.bean.CsvBindByName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 原材料收货对象 md_material_receive
 *
 * @author xuhao
 * @date 2022-09-29
 */

@Data
@TableName("si_material_receive")
public class MaterialReceive {

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @CsvBindByName(column = "id")
    private Long id;

    /**
     * 仓库
     */
    @ExcelProperty(value = "PlantNb")
    @CsvBindByName(column = "PlantNb")
    private String plantNb;

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
     * 供应商
     */
    @ExcelProperty(value = "SupplierNb")
    @CsvBindByName(column = "SupplierNb")
    private String supplierNb;

    /**
     * 存储区域
     */
    @ExcelProperty(value = "StorageLocation")
    @CsvBindByName(column = "StorageLocation")
    private String storageLocation;

    /**
     * BBD(过期时间)
     */
    @ExcelProperty(value = "ExpiryDate")
    @CsvBindByName(column = "ExpiryDate")
    private String expireDate;

    /**
     * 数量
     */
    @ExcelProperty(value = "Quantity")
    @CsvBindByName(column = "Quantity")
    private Integer quantity;

    /**
     * 单位
     */
    @ExcelProperty(value = "Unit")
    @CsvBindByName(column = "Unit")
    private String unit;

    /**
     * 来源区域
     */
    @ExcelProperty(value = "FromArea")
    @CsvBindByName(column = "FromArea")
    private String fromArea;

    /**
     * 来源PO号
     */
    @ExcelProperty(value = "FromPurchaseOrder")
    @CsvBindByName(column = "FromPurchaseOrder")
    private String fromPurchaseOrder;

    /**
     * 木器分拣区域
     */
    @ExcelProperty(value = "ToPickingArea")
    @CsvBindByName(column = "ToPickingArea")
    private String toPickingArea;

    /**
     * PO行号
     */
    @ExcelProperty(value = "PONumberItem")
    @CsvBindByName(column = "PONumberItem")
    private String poNumberItem;

    /**
     * 上传人
     */
    @ExcelProperty(value = "upload_user")
    @CsvBindByName(column = "upload_user")
    private String upload_user;

    /**
     * 上传时间
     */
    @ExcelProperty(value = "upload_time")
    @CsvBindByName(column = "upload_time")
    private Date upload_time;

    /**
     * 更新人
     */
    @ExcelProperty(value = "update_user")
    @CsvBindByName(column = "update_user")
    private String update_user;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "update_time")
    @CsvBindByName(column = "update_time")
    private Date update_time;

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
    private Integer fileId;
}
