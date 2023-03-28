package com.bosch.binin.controller;

import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.MaterialCallVO;
import com.bosch.binin.api.domain.vo.MaterialReturnVO;
import com.bosch.binin.service.IMaterialReturnService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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


    @PutMapping(value = "/confirm/{ssccNumbers}")
    @ApiOperation("批量确认退库任务接口")
    @Transactional(rollbackFor = Exception.class)
    public R confirmJob(@RequestBody MaterialReturnConfirmDTO confirmDTO) {
        materialReturnService.issueJob(confirmDTO);
        return R.ok("下发成功");
    }

    @GetMapping(value = "/list")
    @ApiOperation("获取退料列表")
    public R<PageVO<MaterialReturnVO>> list(MaterialReturnQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new MaterialReturnQueryDTO();
        }
        if (!StringUtils.isEmpty(SecurityUtils.getWareCode())) {
            queryDTO.setWareCode(SecurityUtils.getWareCode());
        }
        startPage();

        List<MaterialReturnVO> list = materialReturnService.list(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));

    }

    @PostMapping(value = "/addMaterialReturn")
    @ApiOperation("新增退库")
    public R save(@RequestBody MaterialReturnDTO materialReturnDTO) {

        if (Objects.isNull(materialReturnDTO)) {
            throw new ServiceException("请扫描MesBarCode");
        }
        boolean save = materialReturnService.addMaterialReturn(materialReturnDTO);
        return R.ok(save);

    }

    @GetMapping(value = "/allocateBin/{mesBarCode}")
    @ApiOperation("退库上架分配库位")
    @Transactional(rollbackFor = Exception.class)
    public R<BinInVO> allocateBin(@PathVariable("mesBarCode") String mesBarCode) {

        return R.ok(materialReturnService.allocateBin(mesBarCode, SecurityUtils.getWareCode()));
    }

    @PostMapping(value = "/in")
    @ApiOperation("退库任务上架")
    public R<BinInVO> in(@RequestBody ManualBinInDTO binInDTO) {

        return R.ok(materialReturnService.performBinIn(binInDTO));
    }


    /**
     * 导出叫料需求列表
     */
    @Log(title = "叫料需求", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("叫料需求列表导出")
    public void export(HttpServletResponse response, @RequestBody MaterialReturnQueryDTO queryDTO) {
        List<MaterialReturnVO> materialReturnVOS = materialReturnService.list(queryDTO);
        ExcelUtil<MaterialReturnVO> util = new ExcelUtil<>(MaterialReturnVO.class);
        util.exportExcel(response, materialReturnVOS, "退库记录");
    }

}
