package com.bosch.storagein.controller;


import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.storagein.api.constants.*;
import com.bosch.storagein.api.domain.dto.MaterialInCheckDTO;
import com.bosch.storagein.api.domain.dto.*;
import com.bosch.storagein.api.domain.vo.MaterialInCheckVO;
import com.bosch.storagein.api.domain.vo.*;
import com.bosch.storagein.api.enumeration.MaterialStatusEnum;
import com.bosch.storagein.service.IMaterialInService;
import com.bosch.storagein.service.IMaterialReceiveService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "原材料入库接口")
@RequestMapping("/material-in")
public class MaterialInController extends BaseController {

    @Autowired
    private IMaterialInService materialInService;

    @Autowired
    private IMaterialReceiveService materialReceiveService;

    /**
     * 根据mesBarCode查询物料校验信息
     */
    @GetMapping(value = "/getCheckByBarCode/{mesBarCode}")
    @ApiOperation("根据mesBarCode查询物料校验信息")
    public R<MaterialInCheckVO> getCheckInfo(@PathVariable("mesBarCode") String mesBarCode) {
        List<MaterialReceiveVO> materialReceiveVOs = materialReceiveService.selectByMesBarCode(mesBarCode);

        List<Integer> collect = materialReceiveVOs.stream().map(MaterialReceiveVO::getStatus).collect(Collectors.toList());
        if (collect.contains(MaterialStatusEnum.IN.getCode())) {
            return R.fail(null, ResponseConstants.BATCH_HAS_IN, "该批次已有入库");
        }
        return R.ok(materialInService.getMaterialCheckInfo(mesBarCode));
    }


    /**
     * 原材料入库校验接口
     */
    @PostMapping(value = "/check")
    @ApiOperation("根据mesBarCode查询物料校验信息")
    @Log(title = "入库校验", businessType = BusinessType.INSERT)
    public R<MaterialCheckResultVO> check(@RequestBody MaterialInCheckDTO materialInCheckDTO) {
        MaterialInVO materialInVO = materialInService.selectByMesBarCode(materialInCheckDTO.getMesBarCode());
        if (materialInVO != null) {
            return R.fail(null, ResponseConstants.HAS_IN, "重复入库");
        }
        MaterialCheckResultVO checkResultVO = materialInService.check(materialInCheckDTO);
        if (checkResultVO != null && checkResultVO.getResponseCode() != null && checkResultVO.getResponseCode() == ResponseConstants.QUANTITY_INVALID) {
            return R.fail(null, ResponseConstants.QUANTITY_INVALID, "抽样件数不符合");

        }

        return R.ok(checkResultVO);
    }

    @GetMapping(value = "/getInfo/{id}")
    @ApiOperation("获取入库详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(materialInService.selectById(id));
    }

    @GetMapping(value = "/getByMesBarCode/{mesBarCode}")
    @ApiOperation("扫码获取入库详细信息")
    public R<MaterialInVO> getByMesBarCode(@PathVariable("mesBarCode") String mesBarCode) {
        return R.ok(materialInService.selectByMesBarCode(mesBarCode));
    }

    @GetMapping("/list")
    @ApiOperation("查询入库列表")
    public R<PageVO<MaterialInVO>> list(@RequestBody MaterialInDTO materialInDTO) {
        startPage();
        List<MaterialInVO> list = materialInService.selectMaterialInList(materialInDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @GetMapping("/currentUserData")
    @ApiOperation("获取当前用户的入库记录")
    public R<PageVO<MaterialInVO>> currentUserData() {
        MaterialInDTO materialInDTO = new MaterialInDTO();
        materialInDTO.setOperateUser(SecurityUtils.getUsername());
        startPage();
        List<MaterialInVO> list = materialInService.selectMaterialInList(materialInDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


}
