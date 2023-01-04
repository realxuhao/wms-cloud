package com.bosch.binin.mapper;

import com.bosch.binin.api.domain.MaterialReturn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.dto.MaterialReturnQueryDTO;
import com.bosch.binin.api.domain.dto.WareShiftQueryDTO;
import com.bosch.binin.api.domain.vo.MaterialReturnVO;
import com.bosch.binin.api.domain.vo.WareShiftVO;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【material_return(退库表)】的数据库操作Mapper
* @createDate 2022-12-12 11:09:13
* @Entity com.bosch.binin.api.domain.MaterialReturn
*/
public interface MaterialReturnMapper extends BaseMapper<MaterialReturn> {

    List<MaterialReturnVO> list(MaterialReturnQueryDTO queryDTO);


}




