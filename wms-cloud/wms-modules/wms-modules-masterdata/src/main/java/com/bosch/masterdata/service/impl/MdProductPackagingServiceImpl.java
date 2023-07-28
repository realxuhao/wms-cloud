package com.bosch.masterdata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.ProductPackaging;
import com.bosch.masterdata.api.domain.dto.MdProductPackagingDTO;
import com.bosch.masterdata.api.domain.vo.MdProductPackagingVO;
import com.bosch.masterdata.service.IMdProductPackagingService;
import com.bosch.masterdata.mapper.MdProductPackagingMapper;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author GUZ1CGD4
* @description 针对表【md_product_packaging(成品包装量)】的数据库操作Service实现
* @createDate 2023-03-09 14:22:29
*/
@Service
public class MdProductPackagingServiceImpl extends ServiceImpl<MdProductPackagingMapper, ProductPackaging>
        implements IMdProductPackagingService {

    @Autowired
    private MdProductPackagingMapper mdProductPackagingMapper;

    @Override
    public List<MdProductPackagingVO> selectList(MdProductPackagingDTO dto) {
        List<MdProductPackagingVO> vos = mdProductPackagingMapper.selectListByDTO(dto);
        return vos;
    }

    @Override
    public MdProductPackagingVO selectMdProductPackagingById(Long id) {
        ProductPackaging packaging = mdProductPackagingMapper.selectById(id);
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
        ProductPackaging packaging = BeanConverUtil.conver(dto, ProductPackaging.class);
        int insert = mdProductPackagingMapper.insert(packaging);
        return insert;
    }

    @Override
    public Integer updateMdProductPackaging(MdProductPackagingDTO dto) {
        // Check if the productNo is duplicated
        LambdaQueryWrapper<ProductPackaging> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductPackaging::getProductNo, dto.getProductNo());
        queryWrapper.eq(ProductPackaging::getCell, dto.getCell());
        queryWrapper.eq(ProductPackaging::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        ProductPackaging packaging = mdProductPackagingMapper.selectOne(queryWrapper);
        if (packaging != null && !packaging.getId().equals(dto.getId())) {
            // If the productNo already exists and it's not the current object, throw an exception
            throw new ServiceException("存在重复成品料号和cell的数据");
        }
        ProductPackaging conver = BeanConverUtil.conver(dto, ProductPackaging.class);
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
            String text="Excel中料号："+r.getProductNo()+" 的 ";
            // Check product No
            if (StringUtils.isEmpty(r.getProductNo())) {
                throw new ServiceException("成品料号 不能为空");
            }
            // Check cell
            if (StringUtils.isEmpty(r.getCell())) {
                throw new ServiceException(text+"Cell 不能为空");
            }

            // Check 名称
            if (StringUtils.isEmpty(r.getProductName())) {
                throw new ServiceException(text+"名称 不能为空");
            }
            // Check 运输单位(Tr)
            if (StringUtils.isEmpty(r.getTransportUnit())) {
                throw new ServiceException(text+"运输单位(Tr) 不能为空");
            }
            // Check 箱 Tr 对应包装规格
            if (r.getBoxSpecification()==null) {
                throw new ServiceException(text+"箱 Tr 对应包装规格 不能为空");
            }
            // Check 标准 Tr/托
            if (StringUtils.isEmpty(r.getStandardUnits())) {
                throw new ServiceException(text+"标准 Tr/托 不能为空");
            }
            // Check 重量(Tr)
            if (StringUtils.isEmpty(r.getWeight())) {
                throw new ServiceException(text+"重量(Tr) 不能为空");
            }
            // Check 体积 (Tr)
            if (StringUtils.isEmpty(r.getVolume())) {
                throw new ServiceException(text+"体积 (Tr) 不能为空");
            }
        });

        return true;
    }

    @Override
    public List<String> getNotExistCodeList(List<String> codeList) {
        if (CollectionUtils.isEmpty(codeList)){
            throw new ServiceException("参数不能为空");
        }
        LambdaQueryWrapper<ProductPackaging> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ProductPackaging::getProductNo,codeList);
        queryWrapper.eq(ProductPackaging::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
        List<ProductPackaging> list = this.list(queryWrapper);
        List<String> existCodeList = list.stream().map(ProductPackaging::getProductNo).collect(Collectors.toList());
        codeList.removeAll(existCodeList);
        return codeList;
    }
}






