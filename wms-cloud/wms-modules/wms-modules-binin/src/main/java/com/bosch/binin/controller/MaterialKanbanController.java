package com.bosch.binin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.dto.BinInQueryDTO;
import com.bosch.binin.api.domain.dto.MaterialKanbanDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.service.IMaterialKanbanService;
import com.bosch.binin.service.impl.MaterialKanbanServiceImpl;
import com.bosch.masterdata.api.domain.Bin;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ruoyi.common.core.utils.PageUtils.startPage;

@RestController
@Api(tags = "kanban接口")
@RequestMapping("/materialKanban")
public class MaterialKanbanController {

    @Autowired
    private IMaterialKanbanService materialKanbanService;

    @PostMapping(value = "/list")
    @ApiOperation("查询kanban列表")
    public R<PageVO<MaterialKanbanVO>> list(@RequestBody MaterialKanbanDTO materialKanbanDTO) {
        if (materialKanbanDTO == null) {
            materialKanbanDTO = new MaterialKanbanDTO();
            materialKanbanDTO.setPageNum(0);
            materialKanbanDTO.setPageSize(0);
        }

        //startPage();

        IPage<MaterialKanbanVO> materialKanbanIPage = materialKanbanService.pageList(materialKanbanDTO);

        return R.ok(new PageVO<>(materialKanbanIPage.getRecords(), new PageInfo<>(materialKanbanIPage.getRecords()).getTotal()));
    }
}
