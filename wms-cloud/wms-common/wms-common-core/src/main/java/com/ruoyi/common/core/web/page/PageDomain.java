package com.ruoyi.common.core.web.page;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.ruoyi.common.core.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分页数据
 *
 * @author ruoyi
 */
public class PageDomain {
    /**
     * 当前记录起始索引
     */
    @ApiModelProperty(value = "当前记录起始索引,仅分页查询")
    @ExcelIgnore
    private Integer pageNum;

    /**
     * 每页显示记录数
     */
    @ApiModelProperty(value = "每页显示记录数,仅分页查询")
    @ExcelIgnore
    private Integer pageSize;

    /**
     * 排序列
     */
    @ApiModelProperty(value = "排序列,仅分页查询")
    @ExcelIgnore
    private String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    @ApiModelProperty(value = "排序的方向desc或者asc,仅分页查询")
    @ExcelIgnore
    private String isAsc = "asc";

    /**
     * 分页参数合理化
     */
    @ApiModelProperty(value = "分页参数合理化,仅分页查询")
    @ExcelIgnore
    private Boolean reasonable = true;

    public String getOrderBy() {
        if (StringUtils.isEmpty(orderByColumn)) {
            return "";
        }
        return StringUtils.toUnderScoreCase(orderByColumn) + " " + isAsc;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderByColumn() {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    public String getIsAsc() {
        return isAsc;
    }

    public void setIsAsc(String isAsc) {
        if (StringUtils.isNotEmpty(isAsc)) {
            // 兼容前端排序类型
            if ("ascending".equals(isAsc)) {
                isAsc = "asc";
            } else if ("descending".equals(isAsc)) {
                isAsc = "desc";
            }
            this.isAsc = isAsc;
        }
    }

    public Boolean getReasonable() {
        if (StringUtils.isNull(reasonable)) {
            return Boolean.TRUE;
        }
        return reasonable;
    }

    public void setReasonable(Boolean reasonable) {
        this.reasonable = reasonable;
    }
}
