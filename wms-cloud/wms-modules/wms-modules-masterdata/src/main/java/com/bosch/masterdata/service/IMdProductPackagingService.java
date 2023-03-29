package com.bosch.masterdata.service;

import com.bosch.masterdata.api.domain.ProductPackaging;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.masterdata.api.domain.dto.MdProductPackagingDTO;
import com.bosch.masterdata.api.domain.vo.MdProductPackagingVO;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【md_product_packaging(成品包装量)】的数据库操作Service
* @createDate 2023-03-09 14:22:29
*/
public interface IMdProductPackagingService extends IService<ProductPackaging> {
    /**
     * 查询VOlist
     * @param dto
     * @return
     */
    List<MdProductPackagingVO> selectList(MdProductPackagingDTO dto);

    /**
     * 查询单个信息
     * @param id
     * @return
     */
    MdProductPackagingVO selectMdProductPackagingById(Long id);

    /**
     * 插入
     * @param dto
     * @return
     */
    Integer insertMdProductPackaging(MdProductPackagingDTO dto);

    /**
     * 更新
     * @param dto
     * @return
     */
    Integer updateMdProductPackaging(MdProductPackagingDTO dto);

    /**
     * 删除EcnVO
     * @param ids
     * @return
     */
    Integer deleteMdProductPackaging(Long[] ids);

    /**
     * 查询信息是否重复
     *
     * @param dtos
     * @return
     */
    boolean validMdProductPackagingList(List<MdProductPackagingDTO> dtos);

    /**
     * 查询导入数据是否规范
     *
     * @param dtos
     * @return
     */
    boolean validData(List<MdProductPackagingDTO> dtos);

    //MdProductPackaging getByProductNo(String productNo);
}
