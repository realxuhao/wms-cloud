package com.bosch.binin.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-01-17 10:05
 **/
@Data
@TableName(value = "split_record")
public class SplitRecord extends BaseEntity {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 源sscc码
     */
    private String sourceSscc;

    /**
     * 新的sscc编码
     */
    private String newMesBarCode;

    /**
     * 拆托数量
     */
    private Double splitQuantity;

    /**
     * sscc编码
     */
    private String ssccNb;

    /**
     * 物料号
     */
    private String materialNb;

    /**
     * 删除标记
     */
    private Integer deleteFlag;

    /**
     * 源总库存
     */
    private Double sourceTotalStock;

    /**
     * 仓库
     */
    private String wareCode;
}
