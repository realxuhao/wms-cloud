package com.bosch.product.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.product.api.domain.RmComparison;
import com.bosch.product.api.domain.ShippingPlan;
import com.bosch.product.api.domain.StockTakePlan;
import com.bosch.product.api.domain.dto.*;
import com.bosch.product.api.domain.dto.ShippingPlanVO;
import com.bosch.product.api.domain.vo.RmComparisonVO;
import com.bosch.product.api.domain.vo.StockTakePlanVO;
import com.bosch.product.service.IRmComparisonService;
import com.bosch.product.service.IStockTakePlanService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.core.web.controller.BaseController;
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

    /**
     * 批量上传
     */
    @ApiOperation("批量上传")
    @PostMapping(value = "/import", headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R saveBatch(@RequestPart(value = "file", required = true) MultipartFile file) throws Exception {
        try {
            List<RmComparison> resultList=new ArrayList<>();
            //解析文件服务
            R result = fileService.productDataImport(file, ClassType.RMCOMPARISONEXCELDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<RmComparisonDTO> dtoList = JSON.parseArray(JSON.toJSONString(data), RmComparisonDTO.class);
                if (CollectionUtils.isNotEmpty(dtoList)) {

                    //保存数据
                    List<RmComparison> rmComparisons = BeanConverUtil.converList(dtoList, RmComparison.class);
                    resultList = rmComparisonService.insertRmComparison(rmComparisons);

                } else {
                    return R.fail("excel中无数据");
                }
                return R.ok(resultList);
            } else {
                return R.fail(result.getMsg());
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(e.getMessage());
        }

    }

}
