package com.bosch.masterdata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.Ecn;
import com.bosch.masterdata.api.domain.Fsmp;
import com.bosch.masterdata.api.domain.Fsmp;
import com.bosch.masterdata.api.domain.dto.FsmpDTO;
import com.bosch.masterdata.api.domain.vo.FsmpVO;
import com.bosch.masterdata.api.enumeration.FsmpClassificationEnum;
import com.bosch.masterdata.mapper.FsmpMapper;
import com.bosch.masterdata.mapper.FsmpMapper;
import com.bosch.masterdata.mapper.FsmpMapper;
import com.bosch.masterdata.service.IFsmpService;
import com.bosch.masterdata.service.IFsmpService;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author GUZ1CGD4
* @description 针对表【md_Fsmp(Fsmp主数据)】的数据库操作Service实现
* @createDate 2023-02-17 11:20:47
*/
@Service
public class FsmpServiceImpl extends ServiceImpl<FsmpMapper, Fsmp>
    implements IFsmpService {

    @Autowired
    private FsmpMapper fsmpMapper;

    @Override
    public List<FsmpVO> selectList(FsmpDTO fsmpDTO) {
        List<FsmpVO> FsmpVOS = fsmpMapper.selectList(fsmpDTO);
        return FsmpVOS;
    }


    @Override
    public FsmpVO selectFsmpById(Long id) {
        Fsmp Fsmp = fsmpMapper.selectById(id);
        FsmpVO conver = BeanConverUtil.conver(Fsmp, FsmpVO.class);
        return conver;
    }

    @Override
    public Integer insertFsmp(FsmpDTO fsmpDTO) {
        List<FsmpDTO> list=new ArrayList<>();
        list.add(fsmpDTO);
        if  (validFsmpList(list)){
            throw new ServiceException("存在重复物料号的数据");
        };
        Fsmp Fsmp = BeanConverUtil.conver(fsmpDTO, Fsmp.class);
        int insert = fsmpMapper.insert(Fsmp);
        return insert;
    }

    @Override
    public Integer updateFsmp(FsmpDTO fsmpDTO) {
        // 检查code是否重复
        LambdaQueryWrapper<Fsmp> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Fsmp::getMaterialCode,fsmpDTO.getMaterialCode());
        queryWrapper.eq(Fsmp::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
        Fsmp fsmp = fsmpMapper.selectOne(queryWrapper);
        if (fsmp != null && !fsmp.getId().equals(fsmpDTO.getId())) {
            // 如果code已存在且不是当前对象，则抛出异常
            throw new ServiceException("存在重复物料号的数据");
        }
        Fsmp conver = BeanConverUtil.conver(fsmpDTO, Fsmp.class);
        return fsmpMapper.updateById(conver);
    }

    @Override
    public Integer deleteFsmp(Long[] ids) {
        return fsmpMapper.deleteFsmp(ids);
    }


    @Override
    public boolean validFsmpList(List<FsmpDTO> FsmpDTOS) {
        return fsmpMapper.validateRecord(FsmpDTOS)>0;
    }

    @Override
    public boolean validData(List<FsmpDTO> fsmpDTOS) {
        List<String> collect = fsmpDTOS.stream().map(FsmpDTO::getMaterialCode).collect(Collectors.toList());

        HashSet<String> strings = new HashSet<>(collect);
        if(collect.size()!=strings.size()){
            throw  new ServiceException("存在重复的物料编码");
        }
        
        fsmpDTOS.forEach(r->{
            //校验类别
            if(r.getClassification()==null){
                throw  new ServiceException("未填写校验类别");
            }
            boolean checkClass = FsmpClassificationEnum.contain(r.getClassification());
            if (!checkClass){
                throw  new ServiceException("校验类别不规范:"+r.getClassification());
            }
        });

        return true;
    }

    @Override
    public Fsmp getByMaterialNb(String materialNb) {
        LambdaQueryWrapper<Fsmp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Fsmp::getMaterialCode,materialNb);
        queryWrapper.eq(Fsmp::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        Fsmp Fsmp = fsmpMapper.selectOne(queryWrapper);
        return Fsmp;
    }
}




