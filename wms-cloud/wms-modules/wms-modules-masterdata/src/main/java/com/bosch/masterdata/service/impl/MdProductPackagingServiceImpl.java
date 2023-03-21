package com.bosch.masterdata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.MdProductPackaging;
import com.bosch.masterdata.api.domain.dto.MdProductPackagingDTO;
import com.bosch.masterdata.api.domain.vo.EcnVO;
import com.bosch.masterdata.api.domain.vo.MdProductPackagingVO;
import com.bosch.masterdata.service.IMdProductPackagingService;
import com.bosch.masterdata.mapper.MdProductPackagingMapper;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【md_product_packaging(成品包装量)】的数据库操作Service实现
* @createDate 2023-03-09 14:22:29
*/
@Service
public class MdProductPackagingServiceImpl extends ServiceImpl<MdProductPackagingMapper, MdProductPackaging>
        implements IMdProductPackagingService {

    @Autowired
    private MdProductPackagingMapper mdProductPackagingMapper;

    @Override
    public List<MdProductPackagingVO> selectList(MdProductPackagingDTO dto) {
        List<MdProductPackagingVO> vos = mdProductPackagingMapper.selectList(dto);
        return vos;
    }

    @Override
    public MdProductPackagingVO selectMdProductPackagingById(Long id) {
        MdProductPackaging packaging = mdProductPackagingMapper.selectById(id);
        MdProductPackagingVO vo = BeanConverUtil.conver(packaging, MdProductPackagingVO.class);
        return vo;
    }

    @Override
    public Integer insertMdProductPackaging(MdProductPackagingDTO dto) {
        List<MdProductPackagingDTO> list = new ArrayList<>();
        list.add(dto);
        if (validMdProductPackagingList(list)) {
            throw new ServiceException("存在重复产品编号的数据");
        }
        ;
        MdProductPackaging packaging = BeanConverUtil.conver(dto, MdProductPackaging.class);
        int insert = mdProductPackagingMapper.insert(packaging);
        return insert;
    }

    @Override
    public Integer updateMdProductPackaging(MdProductPackagingDTO dto) {
        // Check if the productNo is duplicated
        LambdaQueryWrapper<MdProductPackaging> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MdProductPackaging::getProductNo, dto.getProductNo());
        queryWrapper.eq(MdProductPackaging::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        MdProductPackaging packaging = mdProductPackagingMapper.selectOne(queryWrapper);
        if (packaging != null && !packaging.getId().equals(dto.getId())) {
            // If the productNo already exists and it's not the current object, throw an exception
            throw new ServiceException("存在重复产品编号的数据");
        }
        MdProductPackaging conver = BeanConverUtil.conver(dto, MdProductPackaging.class);
        return mdProductPackagingMapper.updateById(conver);
    }

    @Override
    public Integer deleteMdProductPackaging(Long[] ids) {
        return mdProductPackagingMapper.deleteMdProductPackaging(ids);
    }

    @Override
    public boolean validMdProductPackagingList(List<MdProductPackagingDTO> dtos) {
        return mdProductPackagingMapper.validateRecord(dtos) > 0;
    }

    @Override
    public boolean validData(List<MdProductPackagingDTO> dtos) {
        dtos.forEach(r -> {
            // Check cell
            if (StringUtils.isEmpty(r.getCell())) {
                throw new ServiceException("产品Cell不能为空");
            }

            // Check product No
            if (StringUtils.isEmpty(r.getProductNo())) {
                throw new ServiceException("产品编号不能为空");
            }

            // Check 最小包装数量/托
            if (StringUtils.isEmpty(r.getMinQuantity())) {
                throw new ServiceException("最小包装数量/托不能为空");
            }
            // Check 总数量/托
            if (StringUtils.isEmpty(r.getTotalQuantity())) {
                throw new ServiceException("总数量/托不能为空");
            }
        });

        return true;
    }
}





