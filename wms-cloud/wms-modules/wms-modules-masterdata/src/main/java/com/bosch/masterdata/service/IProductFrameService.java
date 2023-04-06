package com.bosch.masterdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.masterdata.api.domain.MaterialBin;
import com.bosch.masterdata.api.domain.ProductFrame;
import com.bosch.masterdata.api.domain.dto.MaterialBinDTO;
import com.bosch.masterdata.api.domain.dto.ProductFrameDTO;
import com.bosch.masterdata.api.domain.vo.ProductFrameVO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-03 13:34
 **/
public interface IProductFrameService extends IService<ProductFrame> {

    List<ProductFrameVO> getBinInRule(String materialCode, String wareCode);

    public boolean validOne(ProductFrameDTO dto);

    /**
     * 新增物料库位分配策略
     *
     * @param
     * @return 结果
     */
    public int insertProductFrame(ProductFrame productFrame);
    /**
     * 查询物料库位分配策略
     *
     * @param id 物料库位分配策略主键
     * @return 物料库位分配策略
     */
    public ProductFrameVO selectProductFrameById(Long id);

    /**
     * 查询物料库位分配策略列表
     *
     * @param productFrameDTO 物料库位分配策略
     * @return 物料库位分配策略集合
     */
    public List<ProductFrameVO> selectProductFrameList(ProductFrameDTO productFrameDTO);


    /**
     * 修改物料库位分配策略
     *
     * @param productFrame 物料库位分配策略
     * @return 结果
     */
    public int updateProductFrame(ProductFrame productFrame);

    /**
     * 批量删除物料库位分配策略
     *
     * @param ids 需要删除的物料库位分配策略主键集合
     * @return 结果
     */
    public int deleteProductFrameByIds(Long[] ids);

    /**
     * 重复校验
     * @param
     * @return
     */
    public boolean validList(List<ProductFrameDTO> dtos);

    /**
     * 赋值
     * @param dtos
     * @return
     */
    public List<ProductFrameDTO> setValue(List<ProductFrameDTO> dtos);
}
