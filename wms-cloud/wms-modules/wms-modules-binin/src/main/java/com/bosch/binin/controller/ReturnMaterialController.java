package com.bosch.binin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bosch.binin.api.domain.dto.AddShiftTaskDTO;
import com.bosch.binin.api.domain.dto.MaterialReturnDTO;
import com.bosch.binin.api.domain.dto.WareShiftQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.api.domain.vo.MaterialReturnVO;
import com.bosch.binin.api.domain.vo.WareShiftVO;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.service.IMaterialReturnService;
import com.bosch.binin.service.IWareShiftService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ruoyi.common.core.utils.PageUtils.startPage;


/**
 * 退库
 */
@RestController
@Api(tags = "退库")
@RequestMapping("/returnMaterial")
public class ReturnMaterialController extends BaseController {

    @Autowired
    private IMaterialReturnService materialReturnService;

    @PostMapping(value = "/list")
    @ApiOperation("获取退料待确认列表")
    public R<PageVO<MaterialReturnVO>> list(MaterialReturnDTO materialReturnDTO) {
        try {

            IPage<MaterialReturnVO> list = materialReturnService.getList(materialReturnDTO);
            return R.ok(new PageVO<>(list.getRecords(), list.getTotal()));
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }

    }

    @PostMapping(value = "/materialReturn")
    @ApiOperation("新增退库")
    public R save(@RequestBody MaterialReturnDTO materialReturnDTO, @RequestParam("mesBarCode") String mesBarCode) {
        try {
            if (StringUtils.isEmpty(mesBarCode)) {
                throw  new ServiceException("请扫描sscc");
            }

            String batchNb = MesBarCodeUtil.getBatchNb(mesBarCode);
            String sscc = MesBarCodeUtil.getSSCC(mesBarCode);

            return R.ok();
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }

    }

}
