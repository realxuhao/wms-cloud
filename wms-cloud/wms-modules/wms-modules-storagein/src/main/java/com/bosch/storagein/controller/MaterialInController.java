package com.bosch.storagein.controller;


import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.storagein.api.constants.*;
import com.bosch.storagein.api.domain.MaterialReceive;
import com.bosch.storagein.api.domain.dto.MaterialInCheckDTO;
import com.bosch.storagein.api.domain.dto.*;
import com.bosch.storagein.api.domain.vo.MaterialInCheckVO;
import com.bosch.storagein.api.domain.vo.*;
import com.bosch.storagein.api.enumeration.MaterialStatusEnum;
import com.bosch.storagein.service.IMaterialInService;
import com.bosch.storagein.service.IMaterialReceiveService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Api(tags = "原材料入库接口")
@RequestMapping("/material-in")
public class MaterialInController extends BaseController {

    @Autowired
    private IMaterialInService materialInService;

    @Autowired
    private IMaterialReceiveService materialReceiveService;

    @Autowired
    private RemoteMaterialService remoteMaterialService;

    /**
     * 根据mesBarCode查询物料校验信息
     */
    @GetMapping(value = "/getCheckByBarCode/{mesBarCode}")
    @Log(title = "获取入库校验信息", businessType = BusinessType.OTHER)
    @ApiOperation("根据mesBarCode查询物料校验信息")
    public R<MaterialInCheckVO> getCheckInfo(@PathVariable("mesBarCode") String mesBarCode) {

        R<MaterialVO> materialVORes = remoteMaterialService.getInfoByMaterialCode(MesBarCodeUtil.getMaterialNb(mesBarCode));


        if (StringUtils.isNull(materialVORes) || StringUtils.isNull(materialVORes.getData())) {
            return R.fail(null, ResponseConstants.MATERIAL_DATA_NOT_EXIST, "物料" + MesBarCodeUtil.getMaterialNb(mesBarCode) + "主数据不存在");
        }

        List<MaterialReceiveVO> materialReceiveVOs = materialReceiveService.selectSameBatchMaterial(mesBarCode);

        List<MaterialReceive> materialReceives = materialReceiveService.selectByMesBarCode(mesBarCode);
        if (CollectionUtils.isEmpty(materialReceives)) {
            return R.fail(null, "收货清单不存在此条数据：" + MesBarCodeUtil.getSSCC(mesBarCode));
        }

        List<Integer> collect = materialReceiveVOs.stream().filter(item -> 0 == item.getStatus()).map(MaterialReceiveVO::getStatus).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)){
            return R.fail(null, ResponseConstants.BATCH_HAS_IN, "物料号:"+MesBarCodeUtil.getMaterialNb(mesBarCode)+",批次号:"+MesBarCodeUtil.getBatchNb(mesBarCode)+"，无待入库任务");
        }
        return R.ok(materialInService.getMaterialCheckInfo(mesBarCode));
    }


    /**
     * 原材料入库校验接口
     */
    @PostMapping(value = "/check")
    @ApiOperation("原材料入库校验接口")
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

    @GetMapping(value = "getSameBatchList")
    @ApiOperation("获取同一批次的数据")
    public R<List<MaterialReceiveVO>> getSameBatchList(@RequestParam("materialNb") String materialNb,
                                                  @RequestParam("batchNb") String batchNb) {
        return R.ok(materialInService.getSameBatchList(materialNb,batchNb));
    }

    @GetMapping("/list")
    @ApiOperation("查询入库列表")
    public R<PageVO<MaterialInVO>> list(@Validated MaterialQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new MaterialQueryDTO();
        }
        if (StringUtils.isEmpty(queryDTO.getWareCode())) {
            queryDTO.setWareCode(SecurityUtils.getWareCode());
        }

        startPage();
        List<MaterialInVO> list = materialInService.selectMaterialInList(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @GetMapping("/currentUserData")
    @ApiOperation("获取当前用户的入库记录")
    public R<PageVO<MaterialInVO>> currentUserData() {
        MaterialQueryDTO queryDTO = new MaterialQueryDTO();
        queryDTO.setOperateUser(SecurityUtils.getUsername());
        queryDTO.setWareCode(SecurityUtils.getWareCode());
        startPage();
        List<MaterialInVO> list = materialInService.selectMaterialInList(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


}
