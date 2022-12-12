package com.bosch.binin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bosch.binin.api.domain.MaterialReturn;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.dto.MaterialReturnDTO;
import com.bosch.binin.api.domain.vo.MaterialReturnVO;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【material_return(退库表)】的数据库操作Service
* @createDate 2022-12-12 11:09:13
*/
public interface IMaterialReturnService extends IService<MaterialReturn> {

    IPage<MaterialReturnVO> getList(MaterialReturnDTO materialReturnDTO);

}
