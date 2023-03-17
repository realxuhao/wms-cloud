package com.bosch.product.controller;


import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.Ecn;
import com.bosch.masterdata.api.domain.dto.EcnDTO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.product.api.domain.ShippingPlan;
import com.bosch.product.api.domain.ShippingTask;
import com.bosch.product.api.domain.dto.ShippingPlanDTO;
import com.bosch.product.api.domain.dto.ShippingPlanVO;
import com.bosch.product.api.domain.dto.ShippingTaskDTO;
import com.bosch.product.api.domain.vo.ShippingTaskVO;
import com.bosch.product.service.IShippingPlanService;
import com.bosch.product.service.IShippingTaskService;
import com.bosch.product.service.impl.ShippingPlanServiceImpl;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@Api(tags = "成品打包接口")
@RequestMapping("/productPackaging")
public class ProductPackagingController extends BaseController {

    @Autowired
    private IShippingPlanService shippingPlanService;
    @Autowired
    private IShippingTaskService shippingTaskService;
    @Autowired
    private FileService fileService;

//    @PostMapping(value = "/lsit")
//    @ApiOperation("成品导入list")
//    public R<ShippingPlanDTO> allocate(@RequestBody ShippingPlanDTO dto) {
//        try {
//            return R.ok();
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return R.fail(e.getMessage());
//        }
//    }
    @PostMapping(value = "/lsit")
    @ApiOperation("获取打包任务")
    public R<PageVO<ShippingTaskVO>> getTask(@RequestBody ShippingTaskDTO dto) {
        try {
            List<ShippingTask> shippingTasks = shippingTaskService.selectAllByFields(dto);

            List<ShippingTaskVO> list = BeanConverUtil.converList(shippingTasks, ShippingTaskVO.class);
            return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }

    /**
     * 删除
     */
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    @DeleteMapping("/{ids}")
    public AjaxResult delete(@PathVariable Long[] ids) {
        return toAjax(shippingTaskService.deleteShippingTask(ids));
    }

    @PostMapping(value = "/genTask")
    @ApiOperation("生成打包任务")
    @Transactional(rollbackFor = Exception.class)
    public R<ShippingPlanDTO> genTask(@RequestBody ShippingPlanDTO dto) {
        try {

            List<ShippingPlan> list = shippingPlanService.getList(dto);
            List<ShippingPlanVO> vos = BeanConverUtil.converList(list, ShippingPlanVO.class);
            if (CollectionUtils.isEmpty(list)) {
                return R.fail("未获取到需要打包的任务");
            }
//            // 遍历列表中的每个元素，并将其 StockMovementDate 字段转换为 Date 类型
//            for (ShippingPlanVO vo : vos) {
//                if(StringUtils.isNotEmpty(vo.getStockMovementDate())){
//                    Date date =DateUtils.parseStringDate(vo.getStockMovementDate());
//                    // 将转换后的日期设置回 ShippingPlan 对象的 StockMovementDate 字段
//                    vo.setStockMovementDateConvert(date);
//                }
//
//            }

            // 按照 stockMovementDate 字段分组，并按照 etoPo 字段排序
            Map<String, List<ShippingPlan>> groupedByDate = list.stream()
                    .sorted(Comparator.comparing(ShippingPlan::getEtoPo)) // 按照 etoPo 字段排序
                    .collect(Collectors.groupingBy(ShippingPlan::getStockMovementDate)); // 按照stockMovementDate 字段分组
            // 对每个分组内的元素按照 shippingMark 和 prodOrder 字段进行排序
            Comparator<ShippingPlan> shippingMarkComparator = Comparator.comparing(ShippingPlan::getShippingMark);
            Comparator<ShippingPlan> prodOrderComparator = Comparator.comparing(ShippingPlan::getProdOrder);

            for (List<ShippingPlan> group : groupedByDate.values()) {
                group.sort(shippingMarkComparator.thenComparing(prodOrderComparator)); // 按照 shippingMark 和
                // prodOrder字段排序
            }

            // 将分组后的结果转换为一个列表
            List<ShippingPlan> result = groupedByDate.values().stream()
                    .flatMap(List::stream) // 将每个分组中的列表转换为一个流
                    .collect(Collectors.toList()); // 将所有元素收集到一个列表中

            ShippingPlanServiceImpl.Pair pair = shippingPlanService.converList(result);

            shippingTaskService.saveBatch(pair.shippingTasks);
            shippingPlanService.updateBatchById(pair.shippingPlans);
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
    @PostMapping(value = "/planImport", headers = "content-type=multipart/form-data")
    public R importExcel(@RequestPart(value = "file", required = true) MultipartFile file) throws Exception {


        //解析文件服务
        R result = fileService.packagingDataImport(file, ClassType.SHIPPINGPLANDTO.getDesc());
        if (result.isSuccess()) {
            Object data = result.getData();
            List<ShippingPlanDTO> dtoList = JSON.parseArray(JSON.toJSONString(data), ShippingPlanDTO.class);
            if (CollectionUtils.isNotEmpty(dtoList)) {
                List<ShippingPlanDTO> filteredShippingPlans = dtoList.stream()
                        .filter(plan -> plan.getEtoPo() != null) // 过滤器：过滤掉etoPo字段为空的数据
                        .collect(Collectors.toList()); // 收集器：将过滤后的结果收集到列表中

                List<ShippingPlan> shippingPlans = BeanConverUtil.converList(filteredShippingPlans, ShippingPlan.class);
                boolean b = shippingPlanService.saveBatch(shippingPlans);
            } else {
                return R.fail("excel中无数据");
            }
            return R.ok(dtoList);
        } else {
            return R.fail(result.getMsg());
        }
    }

    /**
     * 批量更新
     */
    @ApiOperation("批量更新")
    @PostMapping(value = "/saveBatch", headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R saveBatch(@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {

        try {
            //解析文件服务
            R result = fileService.packagingDataImport(file, ClassType.SHIPPINGPLANDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<ShippingPlanDTO> dtoList = JSON.parseArray(JSON.toJSONString(data), ShippingPlanDTO.class);
                if (CollectionUtils.isNotEmpty(dtoList)) {

                }
                return R.ok("导入成功");
            } else {
                return R.fail(result.getMsg());
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(e.getMessage());
        }

    }


}
