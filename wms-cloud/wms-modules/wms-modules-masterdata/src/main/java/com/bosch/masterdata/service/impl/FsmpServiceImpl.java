package com.bosch.masterdata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.Fsmp;
import com.bosch.masterdata.api.domain.Fsmp;
import com.bosch.masterdata.api.domain.dto.FsmpDTO;
import com.bosch.masterdata.api.domain.vo.FsmpVO;
import com.bosch.masterdata.api.enumeration.FsmpClassificationEnum;
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
    private FsmpMapper FsmpMapper;

    @Override
    public List<FsmpVO> selectList(FsmpDTO fsmpDTO) {
        List<FsmpVO> FsmpVOS = FsmpMapper.selectList(fsmpDTO);
        return FsmpVOS;
    }


    @Override
    public FsmpVO selectFsmpById(Long id) {
        Fsmp Fsmp = FsmpMapper.selectById(id);
        FsmpVO conver = BeanConverUtil.conver(Fsmp, FsmpVO.class);
        return conver;
    }

    @Override
    public Integer insertFsmp(FsmpDTO fsmpDTO) {
        List<FsmpDTO> list=new ArrayList<>();
        list.add(fsmpDTO);
        validFsmpList(list);
        Fsmp Fsmp = BeanConverUtil.conver(fsmpDTO, Fsmp.class);
        int insert = FsmpMapper.insert(Fsmp);
        return insert;
    }

    @Override
    public Integer updateFsmp(FsmpDTO fsmpDTO) {
        return null;
    }

    @Override
    public Integer deleteFsmp(Long[] ids) {
        return FsmpMapper.deleteFsmp(ids);
    }


    @Override
    public boolean validFsmpList(List<FsmpDTO> FsmpDTOS) {
        return FsmpMapper.validateRecord(FsmpDTOS)>0;
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
                throw  new ServiceException("校验类别为空");
            }
            boolean checkClass = FsmpClassificationEnum.contain(r.getClassification());
            if (!checkClass){
                throw  new ServiceException("校验类别不规范");
            }
        });

        return true;
    }

    @Override
    public Fsmp getByMaterialNb(String materialNb) {
        LambdaQueryWrapper<Fsmp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Fsmp::getMaterialCode,materialNb);
        queryWrapper.eq(Fsmp::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        Fsmp Fsmp = FsmpMapper.selectOne(queryWrapper);
        return Fsmp;
    }
}




