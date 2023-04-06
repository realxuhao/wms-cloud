package com.bosch.masterdata.mapper;

import com.bosch.masterdata.api.domain.ProductPackaging;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.masterdata.api.domain.dto.MdProductPackagingDTO;
import com.bosch.masterdata.api.domain.vo.MdProductPackagingVO;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【md_product_packaging(成品包装量)】的数据库操作Mapper
* @createDate 2023-03-09 14:22:29
* @Entity com.bosch.masterdata.api.domain.MdProductPackaging
*/
public interface MdProductPackagingMapper extends BaseMapper<ProductPackaging> {

    /**
     * 查询list
     * @param
     * @return
     */
    public List<MdProductPackagingVO> selectListByDTO(MdProductPackagingDTO dto );


    /**
     * 更新
     * @param
     * @return
     */
    public Integer updateMdProductPackaging(MdProductPackagingDTO dto);

    /**
     * 删除
     * @param ids
     * @return
     */
    public Integer deleteMdProductPackaging(Long[] ids);

    /**
     * 验证是否重复
     * @param list
     * @return
     */
    public Integer validateRecord(List<MdProductPackagingDTO> list);
}




