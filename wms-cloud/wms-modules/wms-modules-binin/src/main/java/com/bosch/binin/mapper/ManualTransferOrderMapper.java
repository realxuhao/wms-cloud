package com.bosch.binin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.ManualTransferOrder;
import com.bosch.binin.api.domain.dto.ManualTransQueryDTO;
import com.bosch.binin.api.domain.vo.ManualTransferOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-20 13:12
 **/
@Mapper
@Repository("manualTransferOrderMapper")
public interface ManualTransferOrderMapper extends BaseMapper<ManualTransferOrder> {

    List<ManualTransferOrderVO> list(ManualTransQueryDTO queryDTO);

}
