package com.bosch.storagein.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-10-31 19:55
 **/
@Data
public class MaterialQueryDTO extends PageDomain {

    /**
     * SSCC码
     */
    @ApiModelProperty(value = "SSCC码")
    private String ssccNumber;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNb;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    private String materialNb;

    /**
     * 检查类型
     */
    @ApiModelProperty(value = "检验类型，0：称重，1：数数，2：免检，3：该批次已检")
    private Integer checkType;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operateUser;

    /** 上传时间 */
    @ApiModelProperty(value = "开始操作时间")
    private Date startOperateTime;

    /** 上传时间 */
    @ApiModelProperty(value = "结束操作时间")
    private Date endOperateTime;


    /**
     * 工厂
     */
    @ApiModelProperty(value = "仓库号")
    private String plantNb;

    /**
     * 仓库号
     */
    @ApiModelProperty(value = "仓库号")
    private String wareCode;

    @ApiModelProperty(value = "排序字段")
    private String orderField;

    @ApiModelProperty(value = "排序类型,desc,asc")
    @Pattern(regexp="asc|desc|ASC|DESC")
    private String orderType;

}
