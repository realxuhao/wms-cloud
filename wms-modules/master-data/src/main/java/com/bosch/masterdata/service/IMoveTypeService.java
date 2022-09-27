package com.bosch.masterdata.service;

import java.util.List;
import com.bosch.masterdata.domain.MoveType;
import com.bosch.masterdata.domain.dto.MoveTypeDTO;
import com.bosch.masterdata.domain.vo.MoveTypeVO;

/**
 * 移动类型配置Service接口
 * 
 * @author xuhao
 * @date 2022-09-22
 */
public interface IMoveTypeService 
{
    /**
     * 查询移动类型配置
     * 
     * @param id 移动类型配置主键
     * @return 移动类型配置
     */
    public MoveType selectMoveTypeById(Long id);

    /**
     * 查询移动类型配置列表
     * 
     * @param moveTypeDTO 移动类型配置
     * @return 移动类型配置集合
     */
    public List<MoveTypeVO> selectMoveTypeList(MoveTypeDTO moveTypeDTO);

    /**
     * 新增移动类型配置
     * 
     * @param moveType 移动类型配置
     * @return 结果
     */
    public int insertMoveType(MoveTypeDTO moveType);

    /**
     * 修改移动类型配置
     * 
     * @param moveTypeDTO 移动类型配置
     * @return 结果
     */
    public int updateMoveType(MoveTypeDTO moveTypeDTO);

    /**
     * 批量删除移动类型配置
     * 
     * @param ids 需要删除的移动类型配置主键集合
     * @return 结果
     */
    public int deleteMoveTypeByIds(Long[] ids);

    /**
     * 删除移动类型配置信息
     * 
     * @param id 移动类型配置主键
     * @return 结果
     */
    public int deleteMoveTypeById(Long id);
}
