package com.bosch.masterdata.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class FsmpDTO extends PageDomain {

    /**
     * id
     */
    private Long id;

    /**
     * 状态（1：启用，0：停用）
     */
    @ApiModelProperty(value = "状态（1：启用，0：停用）")
    private Integer status;

    /**
     * 物料code
     */
    @ExcelProperty(value = "料号")
    @ApiModelProperty(value = "料号")
    private String materialCode;

    /**
     *
     */
    @ExcelProperty(value = "分类")
    @ApiModelProperty(value = "类别")
    private String classification;



    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}