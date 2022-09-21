package com.bosch.maindata.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.maindata.mapper.MoveTypeConfigMapper;
import com.bosch.maindata.domain.MoveTypeConfig;
import com.bosch.maindata.service.IMoveTypeConfigService;

/**
 * 移动类型配置Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@Service
public class MoveTypeConfigServiceImpl implements IMoveTypeConfigService 
{
    @Autowired
    private MoveTypeConfigMapper moveTypeConfigMapper;

    /**
     * 查询移动类型配置
     * 
     * @param id 移动类型配置主键
     * @return 移动类型配置
     */
    @Override
    public MoveTypeConfig selectMoveTypeConfigById(Long id)
    {
        return moveTypeConfigMapper.selectMoveTypeConfigById(id);
    }

    /**
     * 查询移动类型配置列表
     * 
     * @param moveTypeConfig 移动类型配置
     * @return 移动类型配置
     */
    @Override
    public List<MoveTypeConfig> selectMoveTypeConfigList(MoveTypeConfig moveTypeConfig)
    {
        return moveTypeConfigMapper.selectMoveTypeConfigList(moveTypeConfig);
    }

    /**
     * 新增移动类型配置
     * 
     * @param moveTypeConfig 移动类型配置
     * @return 结果
     */
    @Override
    public int insertMoveTypeConfig(MoveTypeConfig moveTypeConfig)
    {
        moveTypeConfig.setCreateTime(DateUtils.getNowDate());
        return moveTypeConfigMapper.insertMoveTypeConfig(moveTypeConfig);
    }

    /**
     * 修改移动类型配置
     * 
     * @param moveTypeConfig 移动类型配置
     * @return 结果
     */
    @Override
    public int updateMoveTypeConfig(MoveTypeConfig moveTypeConfig)
    {
        moveTypeConfig.setUpdateTime(DateUtils.getNowDate());
        return moveTypeConfigMapper.updateMoveTypeConfig(moveTypeConfig);
    }

    /**
     * 批量删除移动类型配置
     * 
     * @param ids 需要删除的移动类型配置主键
     * @return 结果
     */
    @Override
    public int deleteMoveTypeConfigByIds(Long[] ids)
    {
        return moveTypeConfigMapper.deleteMoveTypeConfigByIds(ids);
    }

    /**
     * 删除移动类型配置信息
     * 
     * @param id 移动类型配置主键
     * @return 结果
     */
    @Override
    public int deleteMoveTypeConfigById(Long id)
    {
        return moveTypeConfigMapper.deleteMoveTypeConfigById(id);
    }
}
