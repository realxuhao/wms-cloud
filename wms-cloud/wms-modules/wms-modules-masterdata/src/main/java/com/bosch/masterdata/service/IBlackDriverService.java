package com.bosch.masterdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.masterdata.api.domain.BlackDriver;

import java.util.List;

public interface IBlackDriverService extends IService<BlackDriver> {

    /**
     * 查询仓库
     *
     * @param id 仓库主键
     * @return 司机
     */
    public BlackDriver selectBlackDriverById(String id);

    /**
     * 查询司机列表
     *
     * @param blackDriver 司机
     * @return 司机集合
     */
    public List<BlackDriver> selectBlackDriverList(BlackDriver blackDriver);

    /**
     * 新增司机
     *
     * @param blackDriver 司机
     * @return 结果
     */
    public boolean insertBlackDriver(BlackDriver blackDriver);

    /**
     * 修改司机
     *
     * @param blackDriver 司机
     * @return 结果
     */
    public boolean updateBlackDriver(BlackDriver blackDriver);

    /**
     * 批量删除司机
     *
     * @param ids 需要删除的司机主键集合
     * @return 结果
     */
    public boolean deleteBlackDriverByIds(List<Long> ids);

    /**
     * 删除司机信息
     *
     * @param id 司机主键
     * @return 结果
     */
    public boolean deleteBlackDriverById(Long id);

}
