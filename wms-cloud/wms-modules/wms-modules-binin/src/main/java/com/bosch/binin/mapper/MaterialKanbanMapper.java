package com.bosch.binin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.MaterialKanban;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("materialKanbanMapper")
public interface MaterialKanbanMapper extends BaseMapper<MaterialKanban> {

    /**
     * 批量删除
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteByIds(Long[] ids);
}
