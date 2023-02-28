
package com.bosch.binin.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bosch.binin.api.domain.dto.BinAllocationDTO;
import com.bosch.binin.api.domain.dto.IQCChangeStatusDTO;
import com.bosch.binin.api.domain.dto.IQCManagementQueryDTO;
import com.bosch.binin.api.domain.dto.StockQueryDTO;
import com.bosch.binin.api.domain.vo.BinAllocationVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.service.IStockService;
import com.bosch.binin.utils.BeanConverUtil;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.RemoteMesBarCodeService;
import com.bosch.masterdata.api.domain.Ecn;
import com.bosch.masterdata.api.domain.dto.EcnDTO;
import com.bosch.masterdata.api.domain.dto.IQCDTO;
import com.bosch.masterdata.api.domain.vo.MesBarCodeVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.ProgressBarUI;
import javax.validation.Valid;
import java.io.Console;
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
    public R validateStatus(@Valid  @RequestBody IQCChangeStatusDTO iqcChangeStatusDTO) {
        try {
            //校验勾选数据是否
            boolean b = stockService.validateStatus(iqcChangeStatusDTO.getId());
            if(b){
                return R.fail(400,"此SSCC做过质检，是否确认再次变更质量状态");
            }
            return R.ok();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }
    @PostMapping(value = "/changeStatus")
    @ApiOperation("修改质检状态")
    public R changeStatus(@Valid  @RequestBody IQCChangeStatusDTO iqcChangeStatusDTO) {
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

    public R importExcel(@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {

        //解析文件服务
        R result = fileService.IQCDataImport(file, ClassType.IQCDTO.getDesc());
        List<IQCDTO> resultList=new ArrayList<>();
        if (result.isSuccess()) {
            Object data = result.getData();
            List<IQCDTO> list = JSON.parseArray(JSON.toJSONString(data), IQCDTO.class);
            if (CollectionUtils.isNotEmpty(list)) {
                list.removeIf(o->!o.getSapProcessStatus().equals("O"));
                Map<String, List<IQCDTO>> collect = list.stream().collect(Collectors.groupingBy(IQCDTO::getSSCCNumber));
                collect.forEach((k,v)->{
                    IQCDTO iqcdto = v.stream().max(Comparator.comparing(IQCDTO::getIdentification)).get();
                    resultList.add(iqcdto);
                });

                List<IQCDTO> iqcdtos = stockService.excelChangeStatus(resultList);
                return R.ok(iqcdtos);

            } else {
                return R.fail("excel中无数据");
            }
        } else {
            return R.fail(result.getMsg());
        }
    }
}
