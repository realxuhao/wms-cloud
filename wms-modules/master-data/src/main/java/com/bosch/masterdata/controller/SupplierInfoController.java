package com.bosch.masterdata.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.dto.SupplierInfoDTO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.bosch.masterdata.api.domain.SupplierInfo;
import com.bosch.masterdata.service.ISupplierInfoService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;

/**
 * 供应商Controller
 *
 * @author xuhao
 * @date 2022-09-22
 */
@Api(tags = "供应商接口")
@RestController
@RequestMapping("/supplierInfo")
public class SupplierInfoController extends BaseController {
    @Autowired
    private ISupplierInfoService supplierInfoService;


    @Autowired
    private FileService fileService;

    /**
     * 导出供应商列表
     */
    @RequiresPermissions("masterdata:info:export")
    @Log(title = "供应商", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SupplierInfo supplierInfo) {
        List<SupplierInfo> list = supplierInfoService.selectSupplierInfoList(supplierInfo);
        ExcelUtil<SupplierInfo> util = new ExcelUtil<SupplierInfo>(SupplierInfo.class);
        util.exportExcel(response, list, "供应商数据");
    }


    /**
     * 删除供应商
     */
    //@RequiresPermissions("masterdata:info:remove")
    @ApiOperation("删除供应商")
    @Log(title = "供应商", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(supplierInfoService.deleteSupplierInfoByIds(ids));
    }

    /**
     * 新增供应商DTO
     */
    //@RequiresPermissions("masterdata:info:add")
    @ApiOperation("新增供应商")
    @Log(title = "供应商", businessType = BusinessType.INSERT)
    @PostMapping("/addSupplierInfo")
    public AjaxResult addSupplierInfo(@RequestBody SupplierInfoDTO supplierInfoDTO) {
        return toAjax(supplierInfoService.insertSupplierInfo(supplierInfoDTO));
    }

    /**
     * 修改供应商
     */
    //@RequiresPermissions("masterdata:info:edit")
    @ApiOperation("修改供应商")
    @Log(title = "供应商", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable("id") Long id, @RequestBody SupplierInfoDTO supplierInfoDTO) {
        supplierInfoDTO.setId(id);
        return toAjax(supplierInfoService.updateSupplierInfo(supplierInfoDTO));
    }

    /**
     * 查询供应商列表
     */
    //@RequiresPermissions("masterdata:info:list")
    @GetMapping("/list")
    @ApiOperation("查询供应商")
    public R<PageVO<SupplierInfo>> list(SupplierInfoDTO supplierInfoDTO) {
        startPage();
        List<SupplierInfo> list = supplierInfoService.selectSupplierInfoList(supplierInfoDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    /**
     * 获取供应商详细信息
     */
    //@RequiresPermissions("masterdata:info:query")
    @ApiOperation("查询供应商详情")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(supplierInfoService.selectSupplierInfoById(id));
    }

    /**
     * 批量上传供应商明细
     */
    @ApiOperation("批量上传供应商明细")
    @PostMapping(value = "/import", headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R importExcel(@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {
        try {
            //解析文件服务
            R result = fileService.masterDataImport(file, ClassType.SUPPLIERINFODTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<SupplierInfoDTO> dtos = JSON.parseArray(JSON.toJSONString(data), SupplierInfoDTO.class);
                if (CollectionUtils.isNotEmpty(dtos)) {
                    List<String> collect = dtos.stream().map(SupplierInfoDTO::getCode).collect(Collectors.toList());

                    boolean valid = supplierInfoService.validList(collect);
                    if (valid) {
                        return R.fail(400, "存在重复数据");
                    } else {
                        //添加
                        List<SupplierInfo> supplierInfos = BeanConverUtil.converList(dtos, SupplierInfo.class);
                        supplierInfoService.saveBatch(supplierInfos);
                    }
                }
                return R.ok("解析成功");
            } else {
                return R.fail(result.getMsg());
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(e.getMessage());
        }

    }

    /**
     * 批量更新供应商明细
     */
    @ApiOperation("批量更新供应商明细")
    @PostMapping(value = "/saveBatch", headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R saveBatch(@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {

        try {
            //解析文件服务
            R result = fileService.masterDataImport(file, ClassType.SUPPLIERINFODTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<SupplierInfoDTO> dtos = JSON.parseArray(JSON.toJSONString(data), SupplierInfoDTO.class);
                if (CollectionUtils.isNotEmpty(dtos)) {
                    List<String> collect = dtos.stream().map(SupplierInfoDTO::getCode).collect(Collectors.toList());
                    //转换DO
                    List<SupplierInfo> dos = BeanConverUtil.converList(dtos, SupplierInfo.class);
                    dos.forEach(r -> {
                        LambdaUpdateWrapper<SupplierInfo> wrapper = new LambdaUpdateWrapper<SupplierInfo>();
                        wrapper.eq(SupplierInfo::getCode, r.getCode());
                        boolean update = supplierInfoService.update(r, wrapper);
                        if (!update) {
                            r.setCreateBy(SecurityUtils.getUsername());
                            r.setCreateTime(DateUtils.getNowDate());
                            supplierInfoService.save(r);
                        }
                    });
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
