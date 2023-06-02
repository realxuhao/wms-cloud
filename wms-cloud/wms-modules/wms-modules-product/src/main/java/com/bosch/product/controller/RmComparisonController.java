package com.bosch.product.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.product.api.domain.ProComparison;
import com.bosch.product.api.domain.RmComparison;
import com.bosch.product.api.domain.ShippingPlan;
import com.bosch.product.api.domain.StockTakePlan;
import com.bosch.product.api.domain.dto.*;
import com.bosch.product.api.domain.dto.ShippingPlanVO;
import com.bosch.product.api.domain.vo.ProComparisonVO;
import com.bosch.product.api.domain.vo.RmComparisonVO;
import com.bosch.product.api.domain.vo.StockTakePlanVO;
import com.bosch.product.service.IProComparisonService;
import com.bosch.product.service.IRmComparisonService;
import com.bosch.product.service.IStockTakePlanService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-17 13:55
 **/
@RestController
@Api(tags = "库存盘点计划")
@RequestMapping("/rm_comparison")
public class RmComparisonController extends BaseController {

    @Autowired
    private FileService fileService;

    @Autowired
    private IRmComparisonService rmComparisonService;

    @Autowired
    private IProComparisonService proComparisonService;

    @GetMapping(value = "/list")
    @ApiOperation("获取列表")
    public R<PageVO<RmComparisonVO>> getList(RmComparisonDTO dto) {
        try {
            startPage();
            List<RmComparison> list = rmComparisonService.getRmComparisonVOList(dto);
            List<RmComparisonVO> vos = BeanConverUtil.converList(list, RmComparisonVO.class);
            return R.ok(new PageVO<>(vos, new PageInfo<>(list).getTotal()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }

    @GetMapping(value = "/proList")
    @ApiOperation("获取列表")
    public R<PageVO<ProComparisonVO>> getProList(ProComparisonDTO dto) {
        try {
            startPage();
            List<ProComparison> list = proComparisonService.getList(dto);
            List<ProComparisonVO> vos = BeanConverUtil.converList(list, ProComparisonVO.class);
            return R.ok(new PageVO<>(vos, new PageInfo<>(list).getTotal()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }

    /**
     * 通过sscclist修改库存对比表信息
     *
     * @param List<String> ssccList
     * @return
     * @throws Exception
     */
    @ApiOperation("通过sscclist修改库存对比表信息")
    @PostMapping(value = "/updateBySsccList")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateBySsccList(@RequestBody List<String> ssccList) throws Exception {
        try {
            if (CollectionUtils.isEmpty(ssccList)) {
                throw new Exception("请勾选数据");
            }
            boolean b = rmComparisonService.updateBySsccList(ssccList);
            return toAjax(b);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }


    /**
     * 批量上传
     */
    @ApiOperation("批量上传")
    @PostMapping(value = "/import", headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R saveBatch(@RequestPart(value = "file", required = true) MultipartFile file) throws Exception {
        try {
            List<RmComparison> resultList = new ArrayList<>();
            //解析文件服务
            R result = fileService.productDataImport(file, ClassType.RMCOMPARISONEXCELDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<RmComparisonDTO> dtoList = JSON.parseArray(JSON.toJSONString(data), RmComparisonDTO.class);
                if (CollectionUtils.isNotEmpty(dtoList)) {

                    //保存数据
                    List<RmComparison> rmComparisons = BeanConverUtil.converList(dtoList, RmComparison.class);
                    //删除当前人上传的数据
                    boolean b = rmComparisonService.deleteRmComparisonByCreat();
                    if (!b) {
                        return R.fail("删除当前人上传的数据失败");
                    }
                    resultList = rmComparisonService.insertRmComparison(rmComparisons);

                    boolean b1 = rmComparisonService.saveBatch(resultList);
                } else {
                    return R.fail("excel中无数据");
                }
                return R.ok(resultList);
            } else {
                return R.fail(result.getMsg());
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(e.getMessage());
        }

    }

    /**
     * 批量上传
     */
    @ApiOperation("批量上传")
    @PostMapping(value = "/importPro", headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R saveBatchPro(@RequestPart(value = "file", required = true) MultipartFile file) throws Exception {
        try {
            List<ProComparison> proComparisonList = new ArrayList<>();
            //解析文件服务
            R result = fileService.productDataImport(file, ClassType.PROCOMPARISONDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<ProComparisonDTO> dtoList = JSON.parseArray(JSON.toJSONString(data), ProComparisonDTO.class);
                if (CollectionUtils.isNotEmpty(dtoList)) {

                    //保存数据
                    List<ProComparison> proComparisons = BeanConverUtil.converList(dtoList, ProComparison.class);
                    proComparisonList = rmComparisonService.insertProComparison(proComparisons);

                } else {
                    return R.fail("excel中无数据");
                }
                return R.ok(proComparisonList);
            } else {
                return R.fail(result.getMsg());
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(e.getMessage());
        }

    }

}
