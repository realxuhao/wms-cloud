package com.bosch.binin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.dto.MaterialCalJobRequestDTO;
import com.bosch.binin.api.domain.dto.MaterialCallDTO;
import com.bosch.binin.api.domain.dto.MaterialCallQueryDTO;
import com.bosch.binin.api.domain.dto.StockQueryDTO;
import com.bosch.binin.api.domain.vo.MaterialCallCheckResultVO;
import com.bosch.binin.api.domain.vo.MaterialCallVO;
import com.bosch.binin.api.domain.vo.RequirementResultVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.masterdata.api.domain.Frame;

import java.util.List;
import java.util.Map;

/**
 * @program: wms-cloud
 * @description: 叫料service
 * @author: xuhao
 * @create: 2022-11-09 14:45
 **/
public interface IMaterialCallService extends IService<MaterialCall> {


    List<MaterialCall> getMaterialCallList(MaterialCallQueryDTO queryDTO);

    public boolean validList( List<MaterialCallDTO> dtos) ;

    RequirementResultVO converToRequirement(List<MaterialCall> dos,boolean continueFlag);

    RequirementResultVO systemGenerateJob(MaterialCalJobRequestDTO.SystemGenerateJob systemGenerateJob);

    int updateCallQuantity(MaterialCallDTO callDTO);

    MaterialCallCheckResultVO systemGenerateJobCheck(MaterialCalJobRequestDTO.SystemGenerateJob systemGenerateJob);
}
