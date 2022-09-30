package com.bosch.masterdata.service.impl;

import java.util.List;

import com.bosch.masterdata.api.domain.dto.MoveTypeDTO;
import com.bosch.masterdata.api.domain.vo.MoveTypeVO;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.MoveTypeMapper;
import com.bosch.masterdata.api.domain.MoveType;
import com.bosch.masterdata.service.IMoveTypeService;

/**
 * 移动类型配置Service业务层处理
 *
 * @author xuhao
 * @date 2022-09-22
 */
@Service
public class MoveTypeServiceImpl implements IMoveTypeService {
    @Autowired
    private MoveTypeMapper moveTypeMapper;

    /**
     * 查询移动类型配置
     *
     * @param id 移动类型配置主键
     * @return 移动类型配置
     */
    @Override
    public MoveType selectMoveTypeById(Long id) {
        return moveTypeMapper.selectMoveTypeById(id);
    }

    /**
     * 查询移动类型配置列表
     *
     * @param moveTypeDTO 移动类型配置
     * @return 移动类型配置
     */
    @Override
    public List<MoveTypeVO> selectMoveTypeList(MoveTypeDTO moveTypeDTO) {
        MoveType moveType = BeanConverUtil.conver(moveTypeDTO, MoveType.class);
        return moveTypeMapper.selectMoveTypeList(moveType);
    }

    /**
     * 新增移动类型配置
     *
     * @param moveTypeDTO 移动类型配置
     * @return 结果
     */
    @Override
    public int insertMoveType(MoveTypeDTO moveTypeDTO) {
        MoveType moveType = BeanConverUtil.conver(moveTypeDTO, MoveType.class);
        moveType.setCreateTime(DateUtils.getNowDate());
        moveType.setCreateBy(SecurityUtils.getUsername());
        return moveTypeMapper.insertMoveType(moveType);
    }

    /**
     * 修改移动类型配置
     *
     * @param moveTypeDTO 移动类型配置
     * @return 结果
     */
    @Override
    public int updateMoveType(MoveTypeDTO moveTypeDTO) {
        MoveType moveType = BeanConverUtil.conver(moveTypeDTO, MoveType.class);
        moveType.setUpdateTime(DateUtils.getNowDate());
        moveType.setUpdateBy(SecurityUtils.getUsername());
        return moveTypeMapper.updateMoveType(moveType);
    }

    /**
     * 批量删除移动类型配置
     *
     * @param ids 需要删除的移动类型配置主键
     * @return 结果
     */
    @Override
    public int deleteMoveTypeByIds(Long[] ids) {
        return moveTypeMapper.deleteMoveTypeByIds(ids);
    }

    /**
     * 删除移动类型配置信息
     *
     * @param id 移动类型配置主键
     * @return 结果
     */
    @Override
    public int deleteMoveTypeById(Long id) {
        return moveTypeMapper.deleteMoveTypeById(id);
    }
}
