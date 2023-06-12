package com.bosch.masterdata.api.domain.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

/**
 * 库位占用报表dto
 * @TableName report_bin
 */
@Data
public class ReportBinDTO  {

    private Long id;

    /**
     * 0:日 1：月
     */
    private Integer type;
    /**
     * 物料占用库位
     */
    private Integer materialOccupyBin;

    /**
     * 成品占用库位
     */
    private Integer productOccupyBin;

    /**
     * 总库位
     */
    private Integer totalBin;

    /**
     * 占用率
     */
    private BigDecimal percent;

    /**
     * 仓库code
     */
    private String wareCode;

    /**
     * 仓库id
     */
    private Long wareId;


    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建时间起
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;
    /**
     * 创建时间止
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;
    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 状态（1：启用，0：停用）
     */
    private Integer status;


    /**
     * 删除标记1：删除，0:可用
     */
    private Integer deleteFlag;

}