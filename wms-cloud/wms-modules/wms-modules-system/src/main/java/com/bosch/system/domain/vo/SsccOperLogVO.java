package com.bosch.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-08-09 15:12
 **/
@Data
public class SsccOperLogVO {

    /** 操作模块 */
    private String title;

    /** 操作人员 */
    private String operName;



    /** 请求参数 */
    private String operParam;

    /** 返回参数 */
    private String jsonResult;

    /** 操作状态（0正常 1异常） */
    private Integer status;

    /** 错误消息 */
    private String errorMsg;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    /** 操作时间 */
    private Integer methodReturnCode;

    private String ssccNumber;

    private Double quantity;
}
