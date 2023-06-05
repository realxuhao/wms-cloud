package com.bosch.masterdata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.Nmd;
import com.bosch.masterdata.api.domain.dto.NmdDTO;
import com.bosch.masterdata.api.domain.vo.NmdVO;
import com.bosch.masterdata.api.enumeration.NmdClassificationEnum;
import com.bosch.masterdata.api.enumeration.NmdLevelEnum;
import com.bosch.masterdata.api.enumeration.NmdPlanEnum;
import com.bosch.masterdata.service.INmdService;
import com.bosch.masterdata.mapper.NmdMapper;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
* @author GUZ1CGD4
* @description 针对表【md_nmd(nmd主数据)】的数据库操作Service实现
* @createDate 2023-02-07 09:55:28
*/
@Service
public class NmdServiceImpl extends ServiceImpl<NmdMapper, Nmd>
    implements INmdService {

    @Autowired
    private NmdMapper nmdMapper;

    @Override
    public List<NmdVO> selectList(NmdDTO nmdDTO) {
        List<NmdVO> nmdVOS = nmdMapper.selectList(nmdDTO);
        return nmdVOS;
    }

    @Override
    public NmdVO selectNmdById(Long id) {
        Nmd nmd = nmdMapper.selectById(id);
        NmdVO nmdVO = BeanConverUtil.conver(nmd, NmdVO.class);

        return nmdVO;
    }

    @Override
    public Integer insertNmd(NmdDTO nmdDTO) {
        List<NmdDTO> list=new ArrayList<>();
        list.add(nmdDTO);
        if  (validNmdList(list)){
            throw new ServiceException("存在重复物料号的数据");
        };
        Nmd nmd = BeanConverUtil.conver(nmdDTO, Nmd.class);
        int insert = nmdMapper.insert(nmd);
        return insert;
    }

    @Override
    public Integer updateNmd(NmdDTO nmdDTO) {

        // 检查code是否重复
        LambdaQueryWrapper<Nmd> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Nmd::getMaterialCode,nmdDTO.getMaterialCode());
        queryWrapper.eq(Nmd::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
        Nmd nmd = nmdMapper.selectOne(queryWrapper);
        if (nmd != null && !nmd.getId().equals(nmdDTO.getId())) {
            // 如果code已存在且不是当前对象，则抛出异常
            throw new ServiceException("存在重复物料号的数据");
        }
        Nmd conver = BeanConverUtil.conver(nmdDTO, Nmd.class);
        return nmdMapper.updateById(conver);
    }

    @Override
    public Integer deleteNmd(Long[] ids) {
        return nmdMapper.deleteNmd(ids);
    }

    @Override
    public boolean validNmdList(List<NmdDTO> nmdDTOS) {
        return nmdMapper.validateRecord(nmdDTOS)>0;
    }

    @Override
    public boolean validData(List<NmdDTO> nmdDTOS) {

        nmdDTOS.forEach(r->{
            //校验类别
            if(r.getClassification()==null){
                throw  new ServiceException(r.getMaterialCode()+" "+"校验类别为空");
            }
            boolean checkClass = NmdClassificationEnum.contain(r.getClassification());
            if (!checkClass){
                throw  new ServiceException(r.getMaterialCode()+" "+"校验类别不规范:"+r.getClassification());
            }

//            if(r.getClassification()== NmdClassificationEnum.A.getCode()){
//                if(StringUtils.isEmpty(r.getLevel())|| Objects.isNull(r.getPlan())){
//                    throw new ServiceException(r.getMaterialCode()+" "+"类型为A时，检验水平级别和校验抽样方案不可为空。");
//                }
//                boolean checkLevel = NmdLevelEnum.contain(r.getLevel().toString());
//                if (!checkLevel){
//                    throw  new ServiceException(r.getMaterialCode()+" "+"校验检验水平级别不规范");
//                }
//                boolean checkPlan = NmdPlanEnum.contain(r.getPlan().toString());
//                if (!checkPlan){
//                    throw  new ServiceException(r.getMaterialCode()+" "+"校验抽样方案不规范");
//                }
//            }


        });

        return true;
    }

    @Override
    public Nmd getByMaterialNb(String materialNb) {

        LambdaQueryWrapper<Nmd> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Nmd::getMaterialCode,materialNb);
        queryWrapper.eq(Nmd::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        Nmd nmd = nmdMapper.selectOne(queryWrapper);
        return nmd;
    }
}




