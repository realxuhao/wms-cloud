package com.bosch.maindata.service;

import java.util.List;
import com.bosch.maindata.domain.MoveTypeConfig;

/**
 * 移动类型配置Service接口
 * 
 * @author xuhao
 * @date 2022-09-21
 */
public interface IMoveTypeConfigService 
{
    /**
     * 查询移动类型配置
     * 
     * @param id 移动类型配置主键
     * @return 移动类型配置
     */
    public MoveTypeConfig selectMoveTypeConfigById(Long id);

    /**
     * 查询移动类型配置列表
     * 
     * @param moveTypeConfig 移动类型配置
     * @return 移动类型配置集合
     */
    public List<MoveTypeConfig> selectMoveTypeConfigList(MoveTypeConfig moveTypeConfig);

    /**
     * 新增移动类型配置
     * 
     * @param moveTypeConfig 移动类型配置
     * @return 结果
     */
    public int insertMoveTypeConfig(MoveTypeConfig moveTypeConfig);

    /**
     * 修改移动类型配置
     * 
     * @param moveTypeConfig 移动类型配置
     * @return 结果
     */
    public int updateMoveTypeConfig(MoveTypeConfig moveTypeConfig);

    /**
     * 批量删除移动类型配置
     * 
     * @param ids 需要删除的移动类型配置主键集合
     * @return 结果
     */
    public int deleteMoveTypeConfigByIds(Long[] ids);

    /**
     * 删除移动类型配置信息
     * 
     * @param id 移动类型配置主键
     * @return 结果
     */
    public int deleteMoveTypeConfigById(Long id);
}
