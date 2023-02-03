package com.bosch.system.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_move_log")
public class SysMoveLog extends BaseEntity {

    /** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** ssccNumber */
    private String ssccNumber;

    /** moveType */
    private String moveType;

    /** deleteFlag */
    private String deleteFlag;

    /** status */
    private Integer status;
}
