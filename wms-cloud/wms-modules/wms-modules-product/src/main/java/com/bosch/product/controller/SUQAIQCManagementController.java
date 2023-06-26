package com.bosch.product.controller;

import com.bosch.binin.api.domain.dto.IQCChangeStatusDTO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.product.api.domain.dto.ProductIQCManagementQueryDTO;
import com.bosch.product.api.domain.vo.ProductStockVO;
import com.bosch.product.service.IProductStockService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-21 14:51
 **/
@RestController
@Api(tags = "SUQA质检管理")
@RequestMapping("/SUQAIQCManagement")
public class SUQAIQCManagementController extends BaseController {


    @Autowired
    private IProductStockService stockService;

    @GetMapping(value = "/list")
    @ApiOperation("SUQA质检管理列表")
    public R<PageVO<ProductStockVO>> list(ProductIQCManagementQueryDTO queryDTO) {
        startPage();
        List<ProductStockVO> list = stockService.selectSUQAIQCManagementList(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @PostMapping(value = "/validateSUQAStatus")
    @ApiOperation("SUQA校验质检状态")
    public R validateStatus(@Valid @RequestBody IQCChangeStatusDTO iqcChangeStatusDTO) {
        try {
            //校验勾选数据是否
            boolean b = stockService.validateSUQAStatus(iqcChangeStatusDTO.getId());
            if (b) {
                return R.fail(400, "此SSCC做过质检，是否确认再次变更质量状态");
            }
            return R.ok();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }

    @PostMapping(value = "/changeSUQAStatus")
    @ApiOperation("修改质检状态")
    public R changeStatus(@Valid @RequestBody IQCChangeStatusDTO iqcChangeStatusDTO) {
        try {

            Integer integer = stockService.changeSUQAStatus(iqcChangeStatusDTO);

            return R.ok();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }



}
