package com.bosch.storagein.controller;


import com.bosch.storagein.constants.ResponseConstants;
import com.bosch.storagein.domain.dto.MaterialInCheckDTO;
import com.bosch.storagein.domain.vo.MaterialInCheckVO;
import com.bosch.storagein.domain.vo.MaterialReceiveVO;
import com.bosch.storagein.service.IMaterialInService;
import com.bosch.storagein.service.IMaterialReceiveService;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "原材料入库接口")
@RequestMapping("/material-in")
public class MaterialInController {

    @Autowired
    private IMaterialInService materialInService;

    @Autowired
    private IMaterialReceiveService materialReceiveService;

    /**
     * 根据mesBarCode查询物料校验信息
     */
    @GetMapping(value = "/{mesBarCode}")
    @ApiOperation("根据mesBarCode查询物料校验信息")
    public R<MaterialInCheckVO> getInfo(@PathVariable("mesBarCode") String mesBarCode) {
        MaterialReceiveVO materialReceiveVO = materialReceiveService.selectByMesBarCode(mesBarCode);
        if (materialReceiveVO == null) {
            return R.fail(null, ResponseConstants.MES_BARCODE_NOT_EXIST, "该条码不存在");
        }
        if (materialReceiveVO.getStatus() == 1) {
            return R.fail(null, ResponseConstants.HAS_IN, "重复入库");
        }
        return R.ok(materialInService.getMaterialSampleInfo(mesBarCode));
    }


    /**
     * 原材料入库校验接口
     */
    @PostMapping(value = "/check")
    @ApiOperation("根据mesBarCode查询物料校验信息")
    public R<Boolean> check(@RequestBody MaterialInCheckDTO materialInCheckDTO) {

        return R.ok(materialInService.check(materialInCheckDTO));
    }


}
