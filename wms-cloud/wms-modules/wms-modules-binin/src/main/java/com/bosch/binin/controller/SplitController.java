package com.bosch.binin.controller;

import com.bosch.binin.api.domain.SplitRecord;
import com.bosch.binin.api.domain.dto.BinInDTO;
import com.bosch.binin.api.domain.dto.ManualTransQueryDTO;
import com.bosch.binin.api.domain.dto.SplitPalletDTO;
import com.bosch.binin.api.domain.dto.SplitQuertDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.ManualTransferOrderVO;
import com.bosch.binin.api.domain.vo.SplitRecordVO;
import com.bosch.binin.service.ISplitService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.SSCCLogVO;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.log.enums.MaterialType;
import com.ruoyi.common.log.enums.UserOperationType;
import com.ruoyi.common.log.service.IUserOperationLogService;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-01-17 09:50
 **/
@RestController
@Api(tags = "拆托接口")
@RequestMapping("/split")
public class SplitController extends BaseController {

    @Autowired
    private ISplitService splitService;



    @PostMapping(value = "add")
    @ApiOperation("普通拆托")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "PDA普通拆托", businessType = BusinessType.INSERT)
    @Synchronized
    public R<SSCCLogVO> splitPallet(@RequestBody SplitPalletDTO splitPallet) {
        splitService.add(splitPallet);
        SSCCLogVO ssccLogVO = new SSCCLogVO();
        ssccLogVO.setSsccNumber(splitPallet.getSourceSsccNb());
        ssccLogVO.setQuantity(splitPallet.getSplitQuantity());
        ssccLogVO.setNewSSCCNumber(MesBarCodeUtil.getSSCC(splitPallet.getNewMesBarCode()));

        return R.ok(ssccLogVO);
    }

    @GetMapping(value = "/list")
    @ApiOperation("拆托列表")
    public R<PageVO<SplitRecordVO>> list(SplitQuertDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new SplitQuertDTO();
        }
        if (!StringUtils.isEmpty(SecurityUtils.getWareCode())) {
            queryDTO.setWareCode(SecurityUtils.getWareCode());
        }
        startPage();
        List<SplitRecordVO> list = splitService.getList(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @GetMapping(value = "/getInfo/{mesBarCode}")
    @ApiOperation("获取单个拆托详情")
    public R<SplitRecordVO> list(@PathVariable("mesBarCode") String mesBarCode) {
       SplitQuertDTO dto= new SplitQuertDTO();
       dto.setSsccNb(MesBarCodeUtil.getSSCC(mesBarCode));

        List<SplitRecordVO> list = splitService.getList(dto);
        if (!CollectionUtils.isEmpty(list)){
            return R.ok(list.get(0));
        }
        return R.ok();
    }


    @GetMapping(value = "/allocateBin/{mesBarCode}")
    @ApiOperation("移库任务上架分配库位")
    @Transactional(rollbackFor = Exception.class)
    @Synchronized
    public R<BinInVO> allocateBin(@PathVariable("mesBarCode") String mesBarCode) {

        return R.ok(splitService.allocateBin(mesBarCode, SecurityUtils.getWareCode()));
    }


    @PostMapping(value = "/binIn")
    @ApiOperation("PDA普通拆托上架")
    @Log(title = "PDA普通拆托上架", businessType = BusinessType.INSERT)
    @Synchronized
    public R performBinIn(@RequestBody BinInDTO binInDTO) {
        splitService.performBinIn(binInDTO);
        return R.ok();
    }



}
