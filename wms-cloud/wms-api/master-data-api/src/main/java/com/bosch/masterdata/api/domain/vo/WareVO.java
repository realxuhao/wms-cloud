package com.bosch.masterdata.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class WareVO {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 工厂编码 */
    @ApiModelProperty(name = "工厂编码")
    private String factoryCode;

    /** 工厂名称 */
    @ApiModelProperty(name = "工厂名称")
    private String factoryName;

    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    private String code;

    /**
     * 仓库名称
     */
    @ApiModelProperty(value = "仓库名称")
    private String name;

    /**
     * 仓库地址
     */
    @ApiModelProperty(value = "仓库地址")
    private String location;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 仓库联系人
     */
    @ApiModelProperty(value = "仓库联系人")
    private String wareUser;

    /**
     * 仓库联系人电话
     */
    @ApiModelProperty(value = "仓库联系人电话")
    private String wareUserPhone;

    /**
     * 道口数量
     */
    @ApiModelProperty(value = "道口数量")
    private  Long dockNum;


}
