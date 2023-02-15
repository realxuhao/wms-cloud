
package com.bosch.binin.controller;

import com.bosch.binin.api.domain.dto.BinAllocationDTO;
import com.bosch.binin.api.domain.dto.IQCChangeStatusDTO;
import com.bosch.binin.api.domain.dto.IQCManagementQueryDTO;
import com.bosch.binin.api.domain.dto.StockQueryDTO;
import com.bosch.binin.api.domain.vo.BinAllocationVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.service.IStockService;
import com.bosch.binin.utils.BeanConverUtil;
import com.bosch.masterdata.api.RemoteMesBarCodeService;
import com.bosch.masterdata.api.domain.vo.MesBarCodeVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.ProgressBarUI;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: wms-cloud
 * @description: 库存Controller
 * @author: xuhao
 * @create: 2022-11-02 14:43
 **/
@RestController
@Api(tags = "质检管理")
@RequestMapping("/IQCManagement")
public class IQCManagementController extends BaseController {

    @Autowired
    private IStockService stockService;



    @GetMapping(value = "/list")
    @ApiOperation("质检管理列表")
    public R<PageVO<StockVO>> list(IQCManagementQueryDTO iqcManagementQueryDTO) {
        startPage();
        List<StockVO> list = stockService.selectIQCManagementList(iqcManagementQueryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @PostMapping(value = "/changeStatus")
    @ApiOperation("修改质检状态")
    public R allocate(@Valid  @RequestBody IQCChangeStatusDTO iqcChangeStatusDTO) {

        try {

            //校验勾选数据是否
            boolean b = stockService.validateStatus(iqcChangeStatusDTO.getId());
            if(b){

            }
            return R.ok();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }


    }
}
