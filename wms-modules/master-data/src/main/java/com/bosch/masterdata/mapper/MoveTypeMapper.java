package com.bosch.masterdata.mapper;

import java.util.List;
import com.bosch.masterdata.api.domain.MoveType;
import com.bosch.masterdata.api.domain.vo.MoveTypeVO;

/**
 * 移动类型配置Mapper接口
 * 
 * @author xuhao
 * @date 2022-09-22
 */
public interface MoveTypeMapper 
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
     * @param moveType 移动类型配置
     * @return 移动类型配置集合
     */
    public List<MoveTypeVO> selectMoveTypeList(MoveType moveType);

    /**
     * 新增移动类型配置
     * 
     * @param moveType 移动类型配置
     * @return 结果
     */
    public int insertMoveType(MoveType moveType);

    /**
     * 修改移动类型配置
     * 
     * @param moveType 移动类型配置
     * @return 结果
     */
    public int updateMoveType(MoveType moveType);

    /**
     * 删除移动类型配置
     * 
     * @param id 移动类型配置主键
     * @return 结果
     */
    public int deleteMoveTypeById(Long id);

    /**
     * 批量删除移动类型配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMoveTypeByIds(Long[] ids);
}
