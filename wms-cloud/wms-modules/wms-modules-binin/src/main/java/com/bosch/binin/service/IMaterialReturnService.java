package com.bosch.binin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bosch.binin.api.domain.MaterialReturn;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.dto.ManualBinInDTO;
import com.bosch.binin.api.domain.dto.MaterialReturnDTO;
import com.bosch.binin.api.domain.dto.MaterialReturnQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.MaterialReturnVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【material_return(退库表)】的数据库操作Service
* @createDate 2022-12-12 11:09:13
*/
@Service
public interface IMaterialReturnService extends IService<MaterialReturn> {

    List<MaterialReturnVO> list(MaterialReturnQueryDTO queryDTO);

    void issueJob(String[] ssccNumbers);

    boolean addMaterialReturn(MaterialReturnDTO materialReturnDTO);

    BinInVO allocateBin(String mesBarCode, String wareCode);

    BinInVO performBinIn(ManualBinInDTO binInDTO);
}
