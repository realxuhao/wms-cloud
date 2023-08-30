package com.bosch.masterdata.api.domain.vo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 20:43
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportWareShiftVO  {

    @ApiModelProperty(value = "移库单号")
    @Excel(name = "移库单号")
    private String orderNb;


    @ApiModelProperty(value = "车牌号")
    @Excel(name = "车牌号")
    private String carNb;

    @ApiModelProperty(value = "运量")
    @Excel(name = "运量")
    private Integer number;


    /**
     * 源仓库
     */
    @ApiModelProperty(value = "源仓库")
    @Excel(name = "源仓库")
    private String sourceWareCode;


    /**
     * 目的仓库
     */
    @ApiModelProperty(value = "目的仓库")
    @Excel(name = "目的仓库")
    private String targetWareCode;


    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "装车时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "装车时间",dateFormat = "yyyy-MM-dd")
    private Date createTime;
}

