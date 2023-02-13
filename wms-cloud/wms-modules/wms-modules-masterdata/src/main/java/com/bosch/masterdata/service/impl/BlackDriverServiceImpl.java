package com.bosch.masterdata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.BlackDriver;
import com.bosch.masterdata.mapper.BlackDriverMapper;
import com.bosch.masterdata.service.IBlackDriverService;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 司机黑名单Service业务层处理
 *
 * @author taojd
 * @date 2023-02-13
 */
@Service
public class BlackDriverServiceImpl extends ServiceImpl<BlackDriverMapper, BlackDriver> implements IBlackDriverService {

    @Autowired
    private BlackDriverMapper blackDriverMapper;

    @Override
    public BlackDriver selectBlackDriverById(String id) {
        return super.getById(id);
    }

    @Override
    public List<BlackDriver> selectBlackDriverList(BlackDriver blackDriver) {
        return blackDriverMapper.selectBlackDriverList(blackDriver);
    }

    @Override
    public boolean insertBlackDriver(BlackDriver blackDriver) {
        blackDriver.setCreateTime(DateUtils.getNowDate());
        blackDriver.setCreateBy(SecurityUtils.getUsername());
        return super.save(blackDriver);
    }

    @Override
    public boolean updateBlackDriver(BlackDriver blackDriver) {
        blackDriver.setUpdateTime(DateUtils.getNowDate());
        blackDriver.setUpdateBy(SecurityUtils.getUsername());
        return super.saveOrUpdate(blackDriver);
    }

    @Override
    public boolean deleteBlackDriverByIds(List<Long> ids) {
        return super.removeByIds(ids);
    }

    @Override
    public boolean deleteBlackDriverById(Long id) {
        return super.removeById(id);
    }
}
