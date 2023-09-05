package com.bosch.binin.controller;

import com.bosch.binin.api.domain.IQCSamplePlan;
import com.bosch.binin.api.domain.MaterialReturn;
import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.MaterialCallVO;
import com.bosch.binin.api.domain.vo.MaterialReturnVO;
import com.bosch.binin.service.IMaterialReturnService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.SSCCLogVO;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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


    @PostMapping(value = "/confirm")
    @ApiOperation("批量确认退库任务接口")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "批量确认退库任务", businessType = BusinessType.UPDATE)
    public R confirmJob(@RequestBody MaterialReturnConfirmDTO confirmDTO) {
        materialReturnService.issueJob(confirmDTO);
        return R.ok("下发成功");
    }

//    @PostMapping(value = "/comfirmOne/{mesBarCode}")
//    @ApiOperation("批量确认退库任务接口")
//    @Transactional(rollbackFor = Exception.class)
//    public R comfirmOne(@PathVariable("mesBarCode") String mesBarCode) {
//        MaterialReturnConfirmDTO materialReturnConfirmDTO = new MaterialReturnConfirmDTO();
//        materialReturnConfirmDTO.setSsccNumbers(Arrays.asList(MesBarCodeUtil.getSSCC(mesBarCode)));
//        materialReturnConfirmDTO.setWareCode(SecurityUtils.getWareCode());
//        materialReturnService.issueJob(confirmDTO);
//        return R.ok("下发成功");
//    }

    @ApiOperation("获取单个退库信息")
    @GetMapping(value = "/getOne/{mesBarCode}")
    public R<MaterialReturnVO> getOne(@PathVariable("mesBarCode") String mesBarCode){
        MaterialReturnQueryDTO materialReturnQueryDTO = new MaterialReturnQueryDTO();
        materialReturnQueryDTO.setSsccNb(MesBarCodeUtil.getSSCC(mesBarCode));
        List<MaterialReturnVO> list = materialReturnService.list(materialReturnQueryDTO);
        if (!CollectionUtils.isEmpty(list)){
            return R.ok(list.get(0));
        }
        return R.ok();
    }



    @GetMapping(value = "/list")
    @ApiOperation("获取退料列表")
    public R<PageVO<MaterialReturnVO>> list(MaterialReturnQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new MaterialReturnQueryDTO();
        }
//        if (!StringUtils.isEmpty(SecurityUtils.getWareCode())) {
//            queryDTO.setWareCode(SecurityUtils.getWareCode());
//        }
        startPage();

        List<MaterialReturnVO> list = materialReturnService.list(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));

    }

    @PostMapping(value = "/addMaterialReturn")
    @Log(title = "新增退库", businessType = BusinessType.INSERT)
    @ApiOperation("新增退库")
    public R save(@RequestBody MaterialReturnDTO materialReturnDTO) {

        if (Objects.isNull(materialReturnDTO)) {
            throw new ServiceException("请扫描MesBarCode");
        }
        boolean save = materialReturnService.addMaterialReturn(materialReturnDTO);
        SSCCLogVO ssccLogVO = new SSCCLogVO();
        ssccLogVO.setSsccNumber(materialReturnDTO.getSsccNb());
        ssccLogVO.setQuantity(materialReturnDTO.getQuantity());
        return R.ok(ssccLogVO);

    }

    @GetMapping(value = "/allocateBin/{mesBarCode}")
    @ApiOperation("退库上架分配库位")
    @Log(title = "退库上架分配库位", businessType = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    @Synchronized
    public R<BinInVO> allocateBin(@PathVariable("mesBarCode") String mesBarCode) {

        return R.ok(materialReturnService.allocateBin(mesBarCode, SecurityUtils.getWareCode()));
    }

    @PostMapping(value = "/in")
    @ApiOperation("退库任务上架")
    @Log(title = "退库任务上架", businessType = BusinessType.INSERT)
    @Synchronized
    public R in(@RequestBody ManualBinInDTO binInDTO) {
        MaterialReturn materialReturn = materialReturnService.performBinIn(binInDTO);
        SSCCLogVO ssccLogVO = new SSCCLogVO();
        ssccLogVO.setSsccNumber(materialReturn.getSsccNumber());
        ssccLogVO.setQuantity(materialReturn.getQuantity());
        return R.ok(ssccLogVO);
    }

    @PostMapping(value = "/modifyQuantity")
    @ApiOperation("修改退货数量")
    @Log(title = "修改退货数量", businessType = BusinessType.UPDATE)
    public R modifyQuantity(@RequestBody MaterialReturnDTO dto) {
        materialReturnService.modifyQuantity(dto);
        return R.ok();
    }


    /**
     * 导出叫料需求列表
     */
    @PostMapping("/export")
    @ApiOperation("退库记录导出")
    @Log(title = "退库记录导出", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, @RequestBody MaterialReturnQueryDTO queryDTO) {
        List<MaterialReturnVO> materialReturnVOS = materialReturnService.list(queryDTO);
        ExcelUtil<MaterialReturnVO> util = new ExcelUtil<>(MaterialReturnVO.class);
        util.exportExcel(response, materialReturnVOS, "退库记录");
    }

    @PutMapping(value = "/cancel/{id}")
    @Log(title = "退库记录取消", businessType = BusinessType.DELETE)
    @ApiOperation("退库记录取消")
    @Synchronized
    public R<SSCCLogVO> binDown(@PathVariable("id") Long id) {
        MaterialReturn materialReturn = materialReturnService.cancel(id);
        SSCCLogVO ssccLogVO = new SSCCLogVO();
        ssccLogVO.setSsccNumber(materialReturn.getSsccNumber());
        ssccLogVO.setQuantity(null);

        return R.ok();
    }

}
