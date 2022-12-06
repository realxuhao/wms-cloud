package com.bosch.binin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.dto.MaterialCallQueryDTO;
import com.bosch.binin.api.domain.vo.MaterialCallVO;
import com.bosch.masterdata.api.domain.Material;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-10 14:34
 **/
@Mapper
@Repository("materialCallMapper")
public interface MaterialCallMapper extends BaseMapper<MaterialCall> {


    public List<MaterialCallVO> getMaterialCallVOs(MaterialCallQueryDTO queryDTO);


}
