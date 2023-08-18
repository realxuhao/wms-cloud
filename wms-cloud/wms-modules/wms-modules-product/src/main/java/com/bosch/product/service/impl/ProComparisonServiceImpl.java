package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.product.api.domain.ProComparison;
import com.bosch.product.api.domain.RmComparison;
import com.bosch.product.api.domain.dto.ProComparisonDTO;
import com.bosch.product.api.domain.enumeration.ComparisonEnum;
import com.bosch.product.api.domain.vo.ProComparisonVO;
import com.bosch.product.service.IProComparisonService;
import com.bosch.product.mapper.ProComparisonMapper;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【pro_comparison(成品对比表)】的数据库操作Service实现
* @createDate 2023-05-12 13:16:48
*/
@Service
public class ProComparisonServiceImpl extends ServiceImpl<ProComparisonMapper, ProComparison>
    implements IProComparisonService {


    @Autowired
    private ProComparisonMapper proComparisonMapper;
    @Override
    public List<ProComparisonVO> getList(ProComparisonDTO dto) {
        //根据dto查询数据

        List<ProComparisonVO> proComparisons = proComparisonMapper.getList(dto,SecurityUtils.getUsername());

        return proComparisons;
    }

    @Override
    public boolean updateByIdList(List<String> ids) {
        //根据ids更新status为已调整
        LambdaUpdateWrapper<ProComparison> lambdaUpdateWrapper =new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.in(ProComparison::getId,ids);
        lambdaUpdateWrapper.eq(ProComparison::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
        lambdaUpdateWrapper.eq(ProComparison::getStatus, ComparisonEnum.DIFF.code());
        lambdaUpdateWrapper.set(ProComparison::getStatus,ComparisonEnum.CHANGED.code());
        //根据登陆人查询
        lambdaUpdateWrapper.eq(ProComparison::getCreateBy, SecurityUtils.getUsername());
        lambdaUpdateWrapper.set(ProComparison::getUpdateBy, SecurityUtils.getUsername());
        lambdaUpdateWrapper.set(ProComparison::getUpdateTime, DateUtils.getNowDate()) ;
        boolean update = this.update(lambdaUpdateWrapper);
        return update;
    }

    @Override
    public boolean deleteByIdList(List<String> ids) {
        //根据ids更新status为已调整
        LambdaQueryWrapper<ProComparison> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(ProComparison::getId,ids);
        lambdaQueryWrapper.eq(ProComparison::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
        lambdaQueryWrapper.eq(ProComparison::getStatus, ComparisonEnum.DIFF.code());

        //根据登陆人查询
        lambdaQueryWrapper.eq(ProComparison::getCreateBy, SecurityUtils.getUsername());

        boolean remove = this.remove(lambdaQueryWrapper);
        return remove;
    }

    @Override
    public boolean deleteProComparisonByCreat() {
        String username = SecurityUtils.getUsername();
        QueryWrapper<ProComparison> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_by", username);
        int delete = proComparisonMapper.delete(queryWrapper);
        return delete >= 0;
    }
}




