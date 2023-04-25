package com.bosch.product.controller;


import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.Ecn;
import com.bosch.masterdata.api.domain.dto.EcnDTO;
import com.bosch.masterdata.api.domain.dto.MdProductPackagingDTO;
import com.bosch.masterdata.api.domain.dto.PalletDTO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.domain.vo.PalletVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.product.api.domain.ShippingHistory;
import com.bosch.product.api.domain.ShippingPlan;
import com.bosch.product.api.domain.ShippingTask;
import com.bosch.product.api.domain.dto.*;
import com.bosch.product.api.domain.dto.ShippingPlanVO;
import com.bosch.product.api.domain.vo.ShippingTaskVO;
import com.bosch.product.api.domain.vo.ShippingVO;
import com.bosch.product.api.enumeration.TaskStatusEnum;
import com.bosch.product.service.IShippingHistoryService;
import com.bosch.product.service.IShippingPlanService;
import com.bosch.product.service.IShippingTaskService;
import com.bosch.product.service.impl.ShippingPlanServiceImpl;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
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
import org.apache.poi.ss.formula.functions.T;
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
    private IShippingHistoryService shippingHistoryService;

    @Autowired
    private FileService fileService;

    @PostMapping(value = "/addPackageHistory")
    @ApiOperation("添加打包历史记录")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addPackageHistory(@RequestBody ShippingHistoryDTO dto) {
        try {
            ShippingHistory conver = BeanConverUtil.conver(dto, ShippingHistory.class);
            //保存记录
            final boolean a = shippingHistoryService.save(conver);
            boolean b = false;
            //修改任务为进行中
            if (new Integer(0).equals(dto.getLastOne())) {
                b = shippingTaskService.updateStatus(new Long[]{dto.getShippingTaskId()},
                        TaskStatusEnum.UNDEREXECUTION.getCode());
            } else if (new Integer(1).equals(dto.getLastOne())) {
                //最后一托进行校验
                boolean checkHistory = shippingTaskService.checkHistory(dto.getShippingTaskId());
                if (!checkHistory) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
                    return AjaxResult.error("未通过校验");
                }
                //修改任务为已完成
                b = shippingTaskService.updateStatus(new Long[]{dto.getShippingTaskId()},
                        TaskStatusEnum.EXECUTED.getCode());
            }
            if (!(a && b)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            }
            return toAjax(a && b);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除
     */
    @Log(title = "删除单条打包记录", businessType = BusinessType.DELETE)
    @ApiOperation("删除单条打包记录")
    @DeleteMapping("/deletePackageHistory/{id}")
    public AjaxResult deletePackageHistory(@PathVariable Long id) {
        return toAjax(shippingHistoryService.removeById(id));
    }

    /**
     * 获取打包历史记录
     */
    //@RequiresPermissions("masterdata:pallet:list")
    @GetMapping(value = "/getHistoryRecord")
    @ApiOperation("获取打包历史记录")
    public R<PageVO<ShippingVO>> getHistoryRecord( ShippingDTO dto) {
        try {
            startPage();
            List<ShippingVO> shippingVOS = shippingHistoryService.selectShipping(dto);
            return R.ok(new PageVO<>(shippingVOS, new PageInfo<>(shippingVOS).getTotal()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }

    /**
     * 删除
     */
    @Log(title = "根据TaskId删除多条打包记录", businessType = BusinessType.DELETE)
    @ApiOperation("根据TaskId删除多条打包记录")
    @DeleteMapping("/deleteMultiPackageHistory/{id}")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteMultiPackageHistory(@PathVariable Long id) {
        Integer a =0;
        boolean b =false;
        try {
             a = shippingHistoryService.deleteHistoryByTaskId(id);
            //修改任务为已完成
             b = shippingTaskService.updateStatus(new Long[]{id},
                    TaskStatusEnum.UNEXECUTED.getCode());
             if (a==0){
                 throw new ServiceException("无可清空记录");
             }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return AjaxResult.error(e.getMessage());
        }
        return toAjax(a>0&&b);
    }

    @GetMapping(value = "/list")
    @ApiOperation("获取打包任务")
    public R<PageVO<ShippingTaskVO>> getTask( ShippingTaskDTO dto) {
        try {
            startPage();
            List<ShippingTask> shippingTasks = shippingTaskService.selectAllByFields(dto);

            List<ShippingTaskVO> list = BeanConverUtil.converList(shippingTasks, ShippingTaskVO.class);
            return R.ok(new PageVO<>(list, new PageInfo<>(shippingTasks).getTotal()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }
    @GetMapping(value = "/allPlanList")
    @ApiOperation("获取未生成打包任务的打包计划")
    public R<PageVO<ShippingPlanVO>> allPlanList(ShippingPlanDTO dto) {
        try {
            startPage();
            List<ShippingPlan> list = shippingPlanService.getList(dto);
            List<ShippingPlanVO> shippingPlanVOS = BeanConverUtil.converList(list, ShippingPlanVO.class);
            return R.ok(new PageVO<>(shippingPlanVOS, new PageInfo<>(list).getTotal()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }

    @GetMapping(value = "/planList/{id}")
    @ApiOperation("根据打包任务id获取打包计划")
    public R<List<ShippingPlanVO>> planList(@PathVariable("id") Long id) {
        try {

            List<ShippingPlanVO> planList = shippingPlanService.getPlanList(id);

            return R.ok(planList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }
    @GetMapping(value = "/getDashboard")
    @ApiOperation("获取成品打包可视化大屏")
    public R<PageVO<ShippingTaskVO>> getDashboard(ShippingTaskDTO dto) {
        try {
            startPage();
            List<ShippingTaskVO> dashboard = shippingTaskService.getDashboard(dto);
            dashboard.forEach(item -> {
                if (item.getStatus().equals(TaskStatusEnum.EXECUTED.getCode())){
                    item.setRateOfProgress(100.00);
                }
            });
            return R.ok(new PageVO<>(dashboard, new PageInfo<>(dashboard).getTotal()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }

    /**
     * 删除
     */
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @ApiOperation("删除任务")
    @DeleteMapping("/deleteTask/{ids}")
    public AjaxResult deleteTask(@PathVariable Long[] ids) {
        return toAjax(shippingTaskService.deleteShippingTask(ids));
    }
    /**
     * 完成
     */
    @Log(title = "完成", businessType = BusinessType.DELETE)
    @ApiOperation("完成任务")
    @PutMapping("/complete/{ids}")
    public AjaxResult complete(@PathVariable Long[] ids) {
        boolean b =false;
        try {
            //修改任务为已完成
             b = shippingTaskService.updateStatus(ids,
                    TaskStatusEnum.EXECUTED.getCode());

        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
        return b?AjaxResult.success():AjaxResult.error();
    }
    @PostMapping(value = "/genTask")
    @ApiOperation("生成打包任务")
    @Transactional(rollbackFor = Exception.class)
    public R<ShippingPlanDTO> genTask(ShippingPlanDTO dto) {
        try {
            dto.setStatus(TaskStatusEnum.UNEXECUTED.getCode());
            List<ShippingPlan> list = shippingPlanService.getList(dto);
            List<ShippingPlanVO> vos = BeanConverUtil.converList(list, ShippingPlanVO.class);
            if (CollectionUtils.isEmpty(list)) {
                return R.fail("未获取到需要打包的任务");
            }

            ListPair listPair = shippingTaskService.genTask(list);

            shippingTaskService.saveBatch(listPair.shippingTasks);
            shippingPlanService.updateBatchById(listPair.shippingPlans);
            return R.ok();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
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
                        .map(plan -> {
                            if ("#N/A".equals(plan.getPalletQuantity())) {
                                plan.setPalletQuantity("1");
                            }
                            return plan;
                        })//N/A替换为1
                        .collect(Collectors.toList()); // 收集器：将过滤后的结果收集到列表中

//                //用validate校验filteredShippingPlans
//                boolean validate = shippingPlanService.validate(filteredShippingPlans);
//                //如果validate为true，说明excel中数据有重复,返回400和 错误信息“excel中数据有重复”
//                if (validate) {
//                    return R.fail(400, "excel中数据有重复");
//                }
                List<ShippingPlan> repeatPlan = shippingPlanService.getRepeatPlan(filteredShippingPlans);
                //如果repeatPlan不为空，说明excel中数据有重复,返回400和 错误信息“excel中数据有重复”
                if (CollectionUtils.isNotEmpty(repeatPlan)) {
                    //把repeatPlan的etoPo字段和shippingmark字段拼接成一个字符串：“etopo： shippingmark：”放到新的String list中
                    List<String> repeatList = repeatPlan.stream().map(plan -> {
                        return " shippingmark：" + plan.getShippingMark()+"etopo：" + plan.getEtoPo() +";";
                    }).collect(Collectors.toList());
                    //把repeatList中的字符串用逗号拼接成一个字符串
                    String repeat = String.join(",", repeatList);
                    return R.fail(400, "excel中数据有重复："+repeat);
                }

                //如果validate为false，说明excel中数据没有重复，将filteredShippingPlans转换为ShippingPlan对象，批量插入数据库
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
     * 批量上传
     */
    @ApiOperation("批量更新")
    @PostMapping(value = "/saveBatch", headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R saveBatch(@RequestPart(value = "file", required = true) MultipartFile file) throws Exception {
        try {
            //解析文件服务
            R result = fileService.packagingDataImport(file, ClassType.SHIPPINGPLANDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<ShippingPlanDTO> dtoList = JSON.parseArray(JSON.toJSONString(data), ShippingPlanDTO.class);
                if (CollectionUtils.isNotEmpty(dtoList)) {
                    List<ShippingPlanDTO> filteredShippingPlans = dtoList.stream()
                            .filter(plan -> plan.getEtoPo() != null) // 过滤器：过滤掉etoPo字段为空的数据
                            .map(plan -> {
                                if ("#N/A".equals(plan.getPalletQuantity())) {
                                    plan.setPalletQuantity("1");
                                }
                                return plan;
                            })//N/A替换为1
                            .collect(Collectors.toList()); // 收集器：将过滤后的结果收集到列表中


                    //删除excel中重复数据
                    boolean b1 = shippingPlanService.deleteRepeatPlan(filteredShippingPlans);

                    //如果validate为false，说明excel中数据没有重复，将filteredShippingPlans转换为ShippingPlan对象，批量插入数据库
                    List<ShippingPlan> shippingPlans = BeanConverUtil.converList(filteredShippingPlans, ShippingPlan.class);
                    boolean b = shippingPlanService.saveBatch(shippingPlans);

                } else {
                    return R.fail("excel中无数据");
                }
                return R.ok(dtoList);
            } else {
                return R.fail(result.getMsg());
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(e.getMessage());
        }

    }

    /**
     * 修改
     */
    @Log(title = "修改计划", businessType = BusinessType.UPDATE)
    @ApiOperation("修改计划")
    @PutMapping
    public AjaxResult edit(@RequestBody MdProductPackagingDTO dto) {

        return toAjax(shippingPlanService.updatePlan(dto));
    }

    /**
     * 删除
     */
    @Log(title = "删除计划", businessType = BusinessType.DELETE)
    @ApiOperation("删除计划")
    @DeleteMapping("/deletePlan/{ids}")
    public AjaxResult deletePlan(@PathVariable Long[] ids) {
        return toAjax(shippingPlanService.deletePlan(ids));
    }


}
