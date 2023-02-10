package com.bosch.binin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.dto.MaterialCalJobRequestDTO;
import com.bosch.binin.api.domain.dto.MaterialCallDTO;
import com.bosch.binin.api.domain.dto.MaterialCallQueryDTO;
import com.bosch.binin.api.domain.dto.StockQueryDTO;
import com.bosch.binin.api.domain.vo.MaterialCallCheckResultVO;
import com.bosch.binin.api.domain.vo.MaterialCallVO;
import com.bosch.binin.api.domain.vo.RequirementResultVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.masterdata.api.domain.Frame;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @program: wms-cloud
 * @description: 叫料service
 * @author: xuhao
 * @create: 2022-11-09 14:45
 **/
public interface IMaterialCallService extends IService<MaterialCall> {


    List<MaterialCallVO> getMaterialCallList(MaterialCallQueryDTO queryDTO);

    public boolean validList(List<MaterialCallDTO> dtos);

    RequirementResultVO converToRequirement(List<MaterialCall> dos, boolean continueFlag);

    RequirementResultVO systemGenerateJob(MaterialCalJobRequestDTO.SystemGenerateJob systemGenerateJob);

    int updateCallQuantity(MaterialCallDTO callDTO);

    MaterialCallCheckResultVO systemGenerateJobCheck(MaterialCalJobRequestDTO.SystemGenerateJob systemGenerateJob);

    //<需求状态>变为：已下发或部分下发
    int updateCallStatus(MaterialCall materialCall);

    int updateCallStatus(MaterialCall materialCall,MaterialKanban materialKanban);

    void deleteRequirement(List<Long> ids);

    /**
     * 回滚取消的数据
     * @param materialKanban
     * @return
     */
    int updateCallQuantity(MaterialKanban materialKanban);

    void cancelCall(Long id);
}
