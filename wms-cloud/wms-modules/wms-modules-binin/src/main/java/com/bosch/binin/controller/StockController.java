package com.bosch.binin.controller;

import com.alibaba.fastjson2.JSON;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.InitStockDTO;
import com.bosch.binin.api.domain.dto.StockEditDTO;
import com.bosch.binin.api.domain.dto.StockQueryDTO;
import com.bosch.binin.api.domain.dto.WareShiftQueryDTO;
import com.bosch.binin.api.domain.vo.JobVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.api.domain.vo.WareShiftVO;
import com.bosch.binin.service.IJobService;
import com.bosch.binin.service.IStockService;
import com.bosch.binin.utils.BeanConverUtil;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.RemoteMesBarCodeService;
import com.bosch.masterdata.api.RemoteProductService;
import com.bosch.masterdata.api.domain.dto.IQCDTO;
import com.bosch.masterdata.api.domain.vo.MesBarCodeVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.product.api.RemoteProductStockService;
import com.bosch.product.api.domain.dto.ProductStockQueryDTO;
import com.bosch.product.api.domain.vo.ProductStockVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import feign.Headers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.ProgressBarUI;
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
@Api(tags = "库存接口")
@RequestMapping("/stock")
public class StockController extends BaseController {

    @Autowired
    private IStockService stockService;

    @Autowired
    private RemoteMesBarCodeService remoteMesBarCodeService;

    @Autowired
    private RemoteProductStockService remoteProductStockService;

    @Autowired
    private FileService fileService;

    @Autowired
    @Lazy
    private IJobService jobService;


    @GetMapping(value = "/list")
    @ApiOperation("库存列表")
    public R<PageVO<StockVO>> list(StockQueryDTO stockQuerySTO) {
        startPage();
        List<StockVO> list = stockService.selectStockVOList(stockQuerySTO);
        list.stream().forEach(stockVO -> {
            if (stockVO.getFreezeStock()>0){
                JobVO jobDescBySSCC = jobService.getJobDescBySSCC(stockVO.getSsccNumber());
                stockVO.setJobStatus(jobDescBySSCC.getStatusDesc());
                stockVO.setJobDesc(jobDescBySSCC.getJobDesc());
            }
        });
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @PostMapping(value = "/listBySSCC")
    @ApiOperation("库存列表")
    public R<List<StockVO>> listBySSCC(@RequestBody List<String> ssccList) {

        List<StockVO> list = stockService.selectStockVOListBySSCC(ssccList);
        return R.ok(list);
    }
    @GetMapping(value = "/getByMesBarCode/{mesBarCode}")
    @ApiOperation("扫码查询某个物料的库存信息")
    public R<StockVO> getByMesBarCode(@PathVariable("mesBarCode") String mesBarCode) {
        if (mesBarCode.length()>=44 && mesBarCode.length()<=60){
//        mesBarCode.length() == 50||mesBarCode.contains(".")) {
            String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
            R<StockVO> stockVOR = getBySscc(sscc);
            if (!stockVOR.isSuccess() || stockVOR.getData() == null) {
                R<MesBarCodeVO> mesBarCodeVOR = remoteMesBarCodeService.parseMesBarCode(mesBarCode);
                if (mesBarCodeVOR.isSuccess()) {
                    MesBarCodeVO mesBarCodeVO = mesBarCodeVOR.getData();
                    StockVO stockVO = new StockVO();
                    stockVO.setSsccNumber(mesBarCodeVO.getSsccNb());
                    stockVO.setMaterialNb(mesBarCodeVO.getMaterialNb());
                    stockVO.setMaterialName(mesBarCodeVO.getMaterialName());
                    stockVO.setExpireDate(mesBarCodeVO.getExpireDate());
                    stockVO.setBatchNb(mesBarCodeVO.getBatchNb());
                    JobVO jobDescBySSCC = jobService.getJobDescBySSCC(sscc);
                    stockVO.setJobDesc(jobDescBySSCC.getJobDesc());
                    stockVO.setJobStatus(jobDescBySSCC.getStatusDesc());


                    stockVOR.setData(stockVO);

                }
            }else {
                StockVO stockVO = stockVOR.getData();
                JobVO jobDescBySSCC = jobService.getJobDescBySSCC(sscc);
                stockVO.setJobDesc(jobDescBySSCC.getJobDesc());
                stockVO.setJobStatus(jobDescBySSCC.getStatusDesc());
                stockVOR.setData(stockVO);
            }

            return stockVOR;
        } else if (mesBarCode.length() == 71) {
            R<ProductStockVO> productStockVOR = remoteProductStockService.getByBarCode(mesBarCode);
            if (productStockVOR.isSuccess() && productStockVOR.getData() != null) {
                ProductStockVO productStockVO = productStockVOR.getData();
                StockVO stockVO = BeanConverUtil.conver(productStockVO, StockVO.class);
                return R.ok(stockVO);
            }
        }
        return R.fail();

    }

    @GetMapping(value = "/getBySscc/{sscc}")
    @ApiOperation("扫码查询某个物料的库存信息")
    public R<StockVO> getBySscc(@PathVariable("sscc") String sscc) {
        StockQueryDTO queryDTO = new StockQueryDTO();
        queryDTO.setSsccNumber(sscc);

        List<StockVO> list = stockService.selectStockVOList(queryDTO);
        if (CollectionUtils.isEmpty(list)) {
            return R.ok(null);
        }

        return R.ok(list.get(0));
    }


    @GetMapping(value = "/getByBinCode/{binCode}")
    @ApiOperation("查询某个库位的当前库存信息")
    public R<List<StockVO>> getByBinCode(@PathVariable("binCode") String binCode) {
        StockQueryDTO queryDTO = new StockQueryDTO();
        queryDTO.setBinCode(binCode);

        List<StockVO> list = stockService.selectStockVOList(queryDTO);
        if (!CollectionUtils.isEmpty(list)) {
            return R.ok(list);
        } else {
            R<List<ProductStockVO>> productStockVOR = remoteProductStockService.listByBinCode(binCode);
            if (productStockVOR.isSuccess() && productStockVOR.getData() != null) {
                List<ProductStockVO> stockVOList = productStockVOR.getData();
                if (!CollectionUtils.isEmpty(stockVOList)) {
                    List<StockVO> stockVOS = BeanConverUtil.converList(stockVOList, StockVO.class);
                    return R.ok(stockVOS);
                }
            }
        }

        return R.fail("该库位暂无库存信息");
    }

    @GetMapping(value = "/getBinStockLog/{binCode}")
    @ApiOperation("查询某个库位的历史库存信息")
    public R<PageVO<StockVO>> getBinStockLog(@PathVariable("binCode") String binCode) {
        startPage();
        List<StockVO> list = stockService.getBinStockLog(binCode);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @GetMapping(value = "/listByRule")
    @ApiOperation("根据规则查询某个物料的库存")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "物料号", name = "materialNb", dataType = "String"),
            @ApiImplicitParam(value = "排序类型,0基于有效期，1、基于先主库后外库", name = "sortType", dataType = "Integer")
    })
    public R<PageVO<StockVO>> listByRule(StockQueryDTO stockQuerySTO) {
        startPage();
        List<StockVO> list = stockService.selectStockVOBySortType(stockQuerySTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @PostMapping(value = "/countAvailableStock")
    @ApiOperation("批量传入id，计算总可用库存量")
    public R<Map> countAvailableStock(@RequestBody StockQueryDTO stockQuerySTO) {
        Double count = stockService.countAvailableStock(stockQuerySTO);
        Map<String, Double> map = new HashMap<>();
        map.put("count", count);
        return R.ok(map);
    }


    /**
     * 批量上传
     */
    @ApiOperation("批量上传")
    @PostMapping(value = "/import", headers = "content-type=multipart/form-data")
    public R importExcel(@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {
        //解析文件服务
        R result = fileService.InitStockImport(file, ClassType.INITSTOCKDTO.getDesc());
        List<InitStockDTO> resultList = new ArrayList<>();
        if (result.isSuccess()) {
            Object data = result.getData();
            List<InitStockDTO> list = JSON.parseArray(JSON.toJSONString(data), InitStockDTO.class);
            if (com.alibaba.nacos.common.utils.CollectionUtils.isNotEmpty(list)) {
                stockService.initStock(list);
                return R.ok();

            } else {
                return R.fail("excel中无数据");
            }
        } else {
            return R.fail(result.getMsg());
        }
    }

    @PostMapping(value = "/editStock")
    @ApiOperation("修改库存")
    @Log(title = "库存调整修改库存", businessType = BusinessType.INSERT)
    public R editStock(@RequestBody StockEditDTO stockEditDTO) {
        stockService.editStock(stockEditDTO);
        return R.ok();
    }

    /**
     * 导出列表
     */
    @PostMapping("/exportExcel")
    @ApiOperation("库存列表导出")
    @Log(title = "库存列表导出", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, @RequestBody StockQueryDTO queryDTO) {
        List<StockVO> stockVOList = stockService.selectStockVOList(queryDTO);
        ExcelUtil<StockVO> util = new ExcelUtil<>(StockVO.class);
        util.exportExcel(response, stockVOList, "移库任务列表");
    }


}
