package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.MaterialReturn;
import com.bosch.binin.api.domain.dto.MaterialReturnDTO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.api.domain.vo.MaterialReturnVO;
import com.bosch.binin.service.IMaterialReturnService;
import com.bosch.binin.mapper.MaterialReturnMapper;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GUZ1CGD4
 * @description 针对表【material_return(退库表)】的数据库操作Service实现
 * @createDate 2022-12-12 11:09:13
 */
@Service
public class MaterialReturnServiceImpl extends ServiceImpl<MaterialReturnMapper, MaterialReturn>
        implements IMaterialReturnService {

    @Autowired
    private MaterialReturnMapper materialReturnMapper;

    @Override
    public IPage<MaterialReturnVO> getList(MaterialReturnDTO dto) {
        IPage<MaterialReturn> page = new Page<>();
        if (dto.getPageNum() != null && dto.getPageSize() != null) {
            page = new Page<>(dto.getPageNum(), dto.getPageSize());
        }
        LambdaQueryWrapper<MaterialReturn> qw = new LambdaQueryWrapper<>();
        qw.eq(MaterialReturn::getDeleteFlag, DeleteFlagStatus.FALSE);

        IPage<MaterialReturn> materialReturnIPage = materialReturnMapper.selectPage(page, qw);
        //mp提供了convert方法,将数据重新封装
        return materialReturnIPage.convert(u -> {
            MaterialReturnVO v = new MaterialReturnVO();
            BeanUtils.copyProperties(u, v);//拷贝
            return v;
        });
    }
}




