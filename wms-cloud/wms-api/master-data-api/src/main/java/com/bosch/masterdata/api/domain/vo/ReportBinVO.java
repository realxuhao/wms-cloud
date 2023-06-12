package com.bosch.masterdata.api.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库位占用报表
 * @TableName report_bin
 */

@Data
public class ReportBinVO {


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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    private String newDate;
}