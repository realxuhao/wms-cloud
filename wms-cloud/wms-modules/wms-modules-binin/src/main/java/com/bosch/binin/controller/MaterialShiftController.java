package com.bosch.binin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bosch.binin.api.domain.dto.MaterialKanbanDTO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.service.IMaterialShiftService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 21:15
 **/
@RestController
@Api(tags = "移库")
@RequestMapping("/material-shift")
public class MaterialShiftController {

    @Autowired
    private IMaterialShiftService shiftService;

    @PostMapping(value = "/addShiftTask")
    @ApiOperation("查询kanban列表")
    public R<String> list(@RequestBody MaterialKanbanDTO materialKanbanDTO) {

        return null;
    }


}
