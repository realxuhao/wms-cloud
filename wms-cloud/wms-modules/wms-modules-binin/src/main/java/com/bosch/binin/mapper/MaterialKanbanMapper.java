package com.bosch.binin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.MaterialKanban;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("materialKanbanMapper")
public interface MaterialKanbanMapper extends BaseMapper<MaterialKanban> {
}
