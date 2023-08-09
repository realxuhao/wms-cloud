package com.bosch.product.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.product.api.domain.ProductReceive;
import com.bosch.product.api.domain.dto.ProductReceiveDTO;
import com.bosch.product.api.domain.dto.ProductReceiveQueryDTO;
import com.bosch.product.api.domain.dto.ShippingPlanDTO;
import com.bosch.product.api.domain.vo.ProductReceiveVO;
import com.bosch.product.service.IProductReceiveService;
import com.bosch.storagein.api.domain.MaterialReceive;
import com.bosch.system.api.domain.UserOperationLog;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.log.enums.MaterialType;
import com.ruoyi.common.log.enums.UserOperationType;
import com.ruoyi.common.log.service.IProductStockOperationService;
import com.ruoyi.common.log.service.IUserOperationLogService;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 10:56
 **/
@RestController
@Api(tags = "成品入库接口")
@RequestMapping("/product-receive")
public class ProductReceiveController extends BaseController {
    @Autowired
    private FileService fileService;

    @Autowired
    private IProductReceiveService receiveService;

    @Autowired
    private IUserOperationLogService userOperationLogService;


    @GetMapping(value = "/list")
    @ApiOperation("成品收货列表")
    public R<PageVO<ProductReceiveVO>> list(ProductReceiveQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new ProductReceiveQueryDTO();
        }

        if (!StringUtils.isEmpty(SecurityUtils.getWareCode())) {
            queryDTO.setWareCode(SecurityUtils.getWareCode());
        }
        startPage();
        List<ProductReceiveVO> list = receiveService.list(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @GetMapping(value = "/getOne/{qrCode}")
    @ApiOperation("获取单个收货信息")
    public R<ProductReceiveVO> getOne(@PathVariable("qrCode") String qrCode) {
        String sscc = ProductQRCodeUtil.getSSCC(qrCode);
        ProductReceiveQueryDTO queryDTO = new ProductReceiveQueryDTO();
        queryDTO.setSsccNumber(sscc);
        List<ProductReceiveVO> list = receiveService.list(queryDTO);
        if (CollectionUtils.isEmpty(list)) {
            return R.fail("没有该sscc:" + sscc + "对应的入库任务");
        }
        return R.ok(list.get(0));
    }

    @PutMapping(value = "/receive/{qrCode}")
    @ApiOperation("PDA成品收货")
    @Transactional(rollbackFor = Exception.class)
    @Synchronized
    @Log(title = "PDA成品收货", businessType = BusinessType.INSERT)
    public R receive(@PathVariable("qrCode") String qrCode, @RequestParam("quantity")Double quantity) {
        receiveService.receive(qrCode,quantity);
        return R.ok();
    }


    @DeleteMapping(value = "/{id}")
    @ApiOperation("删除")
    @Log(title = "PDA成品收货取消", businessType = BusinessType.UPDATE)
    public R receive(@PathVariable("id") Long id) {
        receiveService.delete(id);
        return R.ok();
    }



    /**
     * 批量上传
     */
    @ApiOperation("批量上传")
    @Log(title = "PDA成品收货上传文件", businessType = BusinessType.IMPORT)
    @PostMapping(value = "/import", headers = "content-type=multipart/form-data")
    public R importExcel(@RequestPart(value = "file", required = true) MultipartFile file) throws Exception {


        //解析文件服务
        R result = fileService.productDataImport(file, ClassType.PRODUCTRECEIVEDTO.getDesc());
        if (result.isSuccess()) {
            Object data = result.getData();
            List<ProductReceiveDTO> dtoList = JSON.parseArray(JSON.toJSONString(data), ProductReceiveDTO.class);
            if (!CollectionUtils.isEmpty(dtoList)) {
                List<ProductReceiveDTO> dtos = dtoList.stream()
                        .filter(x ->!"ST".equals(x.getUnit()) ) // 过滤器：过滤掉unit字段为ST的数据
                        .collect(Collectors.toList()); // 收集器：将过滤后的结果收集到列表中
                boolean valid = receiveService.validList(dtos);
                if (valid) {
                    return R.fail(400, "存在重复数据");
                } else {
                    List<ProductReceive> dos = BeanConverUtil.converList(dtos, ProductReceive.class);

                    boolean b = receiveService.saveBatch(dos);

                    List<UserOperationLog> userOperationLogList = new ArrayList<>();

                    dtos.stream().forEach(dto->{
                        UserOperationLog userOperationLog = new UserOperationLog();
                        userOperationLog.setOperationType(UserOperationType.Import.getCode());
                        userOperationLog.setCode(dto.getMaterialNb());
                        userOperationLog.setSsccNumber(dto.getSsccNumber());
                        userOperationLogList.add(userOperationLog);
                    });

                    userOperationLogService.insertUserOperationLog(MaterialType.PRODUCT.getCode(),null,SecurityUtils.getUsername(),UserOperationType.PRODUCT_IMPORT.getCode(),userOperationLogList);

                }
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
    @PostMapping(value = "/saveBatch" , headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "PDA成品收货上传文件批量更新", businessType = BusinessType.UPDATE)
    public R saveBatch(@RequestPart(value = "file" , required = true) MultipartFile file) throws IOException {

        try {
            //解析文件服务
            R result = fileService.productDataImport(file, ClassType.PRODUCTRECEIVEDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<ProductReceiveDTO> dtoList = JSON.parseArray(JSON.toJSONString(data), ProductReceiveDTO.class);
                if (!CollectionUtils.isEmpty(dtoList)) {
                    List<ProductReceiveDTO> dtos = dtoList.stream()
                            .filter(x ->!"ST".equals(x.getUnit()) ) // 过滤器：过滤掉unit字段为ST的数据
                            .collect(Collectors.toList()); // 收集器：将过滤后的结果收集到列表中

                    List<ProductReceive> dos = BeanConverUtil.converList(dtos, ProductReceive.class);
                    dos.forEach(r->{
                        LambdaUpdateWrapper<ProductReceive> wrapper=new LambdaUpdateWrapper<ProductReceive>();
                        wrapper.eq(ProductReceive::getSsccNumber,r.getSsccNumber());
                        wrapper.eq(ProductReceive::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
                        boolean update = receiveService.update(r, wrapper);
                        if (!update){
                            receiveService.save(r);
                        }
                    });

                    List<UserOperationLog> userOperationLogList = new ArrayList<>();

                    dtos.stream().forEach(dto->{
                        UserOperationLog userOperationLog = new UserOperationLog();
                        userOperationLog.setOperationType(UserOperationType.Import.getCode());
                        userOperationLog.setCode(dto.getMaterialNb());
                        userOperationLog.setSsccNumber(dto.getSsccNumber());
                        userOperationLogList.add(userOperationLog);
                    });

                    userOperationLogService.insertUserOperationLog(MaterialType.PRODUCT.getCode(),null,SecurityUtils.getUsername(),UserOperationType.PRODUCT_IMPORT.getCode(),userOperationLogList);


                }
                return R.ok("导入成功");
            }else {
                return R.fail(result.getMsg());
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(e.getMessage());
        }

    }
}
