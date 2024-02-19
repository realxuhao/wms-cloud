package com.bosch.binin.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-01 14:52
 **/
@Data
@TableName("stock_adjust")
public class StockAdjust {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * SSCC码
     */
    private String ssccNumber;

    /**
     * 工厂
     */
    private String plantNb;

    /**
     * 仓库编码
     */
    private String wareCode;

    /**
     * 区域编码
     */
    private String areaCode;
    /**
     * 跨编码
     */
    private String frameCode;

    /**
     * 库位编码
     */
    private String binCode;

    /**
     * 物料号
     */
    private String materialNb;

    /**
     * 批次号
     */
    private String batchNb;

    /**
     * 过期时间
     */
    private Date expireDate;

    /**
     * 总库存
     */
    private Double totalStock;

    /**
     * 冻结库存
     */
    private Double freezeStock;

    /**
     * 可用库存
     */
    private Double availableStock;

    /**
     * 质检状态
     */
    private String qualityStatus;

    /**
     * 质检状态
     */
    private Integer deleteFlag;

    /**
     * 调整后总库存
     */
    private Double adjustTotalStock;

    /**
     * 调整后冻结库存
     */
    private Double adjustFreezeStock;

    /**
     * 调整后可用库存
     */
    private Double adjustAvailableStock;

    /**
     * typeList: [{
     * 						text: '领用',
     * 						value: 0
     * 					                    },
     *                    {
     * 						text: '报废',
     * 						value: 1
     *                    },
     *                    {
     * 						text: '其它',
     * 						value: 2
     *                    },
     *                    {
     * 						text: '玻璃瓶配送到产线',
     * 						value: 3
     *                    }
     * 				],
     */
    private Integer type;


}
