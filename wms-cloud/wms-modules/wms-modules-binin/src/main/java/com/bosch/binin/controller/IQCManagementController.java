
package com.bosch.binin.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bosch.binin.api.domain.dto.IQCChangeStatusDTO;
import com.bosch.binin.api.domain.dto.IQCManagementQueryDTO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.service.IStockService;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.dto.IQCDTO;
import com.bosch.masterdata.api.domain.vo.IQCVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private FileService fileService;

    @GetMapping(value = "/list")
    @ApiOperation("质检管理列表")
    public R<PageVO<StockVO>> list(IQCManagementQueryDTO iqcManagementQueryDTO) {
        startPage();
        List<StockVO> list = stockService.selectIQCManagementList(iqcManagementQueryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @PostMapping(value = "/validateStatus")
    @ApiOperation("校验质检状态")
    public R validateStatus(@Valid @RequestBody IQCChangeStatusDTO iqcChangeStatusDTO) {
        try {
            //校验勾选数据是否
            boolean b = stockService.validateStatus(iqcChangeStatusDTO.getId());
            if (b) {
                return R.fail(400, "此SSCC做过质检，是否确认再次变更质量状态");
            }
            return R.ok();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }

    @PostMapping(value = "/changeStatus")
    @Log(title = "修改质检状态", businessType = BusinessType.UPDATE)
    @ApiOperation("修改质检状态")
    public R changeStatus(@Valid @RequestBody IQCChangeStatusDTO iqcChangeStatusDTO) {
        try {

            Integer integer = stockService.changeStatus(iqcChangeStatusDTO);

            return R.ok();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }

    /**
     * 批量上传
     */
    @ApiOperation("批量上传")
    @PostMapping(value = "/import", headers = "content-type=multipart/form-data")
    @Log(title = "IQC批量上传质检状态", businessType = BusinessType.INSERT)

    public R importExcel(@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {
        //解析文件服务
        R result = fileService.IQCDataImport(file, ClassType.IQCDTO.getDesc());
        List<IQCDTO> resultList = new ArrayList<>();
        if (result.isSuccess()) {
            Object data = result.getData();
            List<IQCDTO> list = JSON.parseArray(JSON.toJSONString(data), IQCDTO.class);
            if (CollectionUtils.isNotEmpty(list)) {
                list.removeIf(o -> !o.getSapProcessStatus().equals("O"));
                Map<String, List<IQCDTO>> collect = list.stream().collect(Collectors.groupingBy(IQCDTO::getSsccnumber));
                collect.forEach((k, v) -> {
                    IQCDTO iqcdto = v.stream().max(Comparator.comparing(IQCDTO::getIdentification)).get();
                    resultList.add(iqcdto);
                });
                List<IQCDTO> iqcdtos = stockService.iqcDTOSToMaterial(resultList);
                return R.ok(iqcdtos);

            } else {
                return R.fail("excel中无数据");
            }
        } else {
            return R.fail(result.getMsg());
        }
    }


    @PostMapping(value = "/excelChangeStatus")
    @ApiOperation("上传完excel修改质检状态")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "IQC上传完excel修改质检状态", businessType = BusinessType.UPDATE)

    public R<List<IQCVO>> excelChangeStatus(@RequestBody List<IQCDTO> iqcdtos) {
        try {
            List<IQCVO> result = stockService.excelChangeStatus(iqcdtos);

            return R.ok(result);

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }
}
