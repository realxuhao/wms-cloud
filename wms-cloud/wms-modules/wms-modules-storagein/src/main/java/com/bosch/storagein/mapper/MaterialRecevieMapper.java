package com.bosch.storagein.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.storagein.api.domain.MaterialReceive;
import com.bosch.storagein.api.domain.dto.MaterialReceiveDTO;
import com.bosch.storagein.api.domain.vo.MaterialReceiveVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 物料收货信息Mapper接口
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@Mapper
@Repository("materialRecevieMapper")
public interface MaterialRecevieMapper extends BaseMapper<MaterialReceive>
{
    /**
     * 查询物料收货信息
     *
     * @param id 物料收货信息主键
     * @return 物料信息
     */
    public MaterialReceiveVO selectMaterialReceiveVOById(Long id);

    /**
     * 查询物料信息列表
     * 
     * @param materialReceiveDTO 物料收货信息
     * @return 物料信息集合
     */
    public List<MaterialReceiveVO> selectMaterialReceiveVOList( MaterialReceiveDTO materialReceiveDTO);


    /**
     * 根据SNCC码查询收货信息
     *
     * @param snccNumber SNCC
     * @return 物料信息集合
     */
    public MaterialReceiveVO selectMaterialReceiveVOBySncc(String snccNumber);

    /**
     * 删除物料信息
     * 
     * @param id 物料信息主键
     * @return 结果
     */
    public int deleteMaterialReceiveVOById(Long id);

    /**
     * 批量删除物料收货信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMaterialReceiveVOByIds(Long[] ids);


    public void updateStatusBySscc(@Param("ssccNumber") String ssccNumber, @Param("status") Integer status);

    public int updateEditFlag(@Param("ids") List<Long> ids, @Param("editFlag") Integer editFlag);


    public List<MaterialReceiveVO> selectSameBatchMaterialReceiveVO(@Param("materialNb")String materialNb,@Param("batchNb")String batchNb);


    void batchUpdateStatus(@Param("materialNb") String materialNb, @Param("batchNb") String batchNb, @Param("code") Integer code, @Param("updateUser") String updateUser, @Param("updateTime")Date updateTime);

    public int updateBatch(MaterialReceive materialReceive);
}
