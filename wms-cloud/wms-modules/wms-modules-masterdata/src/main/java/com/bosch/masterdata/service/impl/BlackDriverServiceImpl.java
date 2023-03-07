package com.bosch.masterdata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.BlackDriver;
import com.bosch.masterdata.mapper.BlackDriverMapper;
import com.bosch.masterdata.service.IBlackDriverService;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public BlackDriver selectBlackDriverById(Long id) {
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

    @Override
    public List<BlackDriver> selectBlackDriverByWechatId(String wechatId, boolean isBlack) {
        QueryWrapper<BlackDriver> wrapper = new QueryWrapper<>();
        wrapper.eq("wechat_id", wechatId);
        if (isBlack) {
            wrapper.eq("status", 1);
        }
        List<BlackDriver> lst = blackDriverMapper.selectList(wrapper);
        return lst;
    }

    @Override
    public boolean saveBlackDriver(BlackDriver blackDriver) {
        QueryWrapper<BlackDriver> wrapper = new QueryWrapper<>();
        wrapper.eq("wechat_id", blackDriver.getWechatId());
        Optional<BlackDriver> first = blackDriverMapper.selectList(wrapper).stream().findFirst();
        if (first.isPresent()) {
            blackDriver.setDriverId(first.get().getDriverId());
            blackDriver.setStatus(first.get().getStatus());
            blackDriver.setUpdateTime(DateUtils.getNowDate());
            blackDriver.setUpdateBy(SecurityUtils.getUsername());
            return super.saveOrUpdate(blackDriver);
        } else {
            blackDriver.setStatus(0);
            blackDriver.setCreateTime(DateUtils.getNowDate());
            blackDriver.setCreateBy(SecurityUtils.getUsername());
            return super.save(blackDriver);
        }
    }
}
