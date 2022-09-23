package com.bosch.masterdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.bosch.masterdata.domain.dto.SupplierInfoDTO;
import com.bosch.masterdata.domain.vo.MaterialVO;
import com.bosch.masterdata.domain.vo.PageVO;
import com.bosch.masterdata.domain.vo.SupplierInfoVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.bosch.masterdata.domain.SupplierInfo;
import com.bosch.masterdata.service.ISupplierInfoService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 供应商Controller
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@Api(tags = "供应商接口")
@RestController
@RequestMapping("/supplierInfo")
public class SupplierInfoController extends BaseController
{
    @Autowired
    private ISupplierInfoService supplierInfoService;

    /**
     * 查询供应商列表
     */
//    @RequiresPermissions("masterdata:info:list")
//    @GetMapping("/list")
//    public TableDataInfo list(SupplierInfo supplierInfo)
//    {
//        startPage();
//        List<SupplierInfo> list = supplierInfoService.selectSupplierInfoList(supplierInfo);
//        return getDataTable(list);
//    }

    /**
     * 导出供应商列表
     */
    @RequiresPermissions("masterdata:info:export")
    @Log(title = "供应商", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SupplierInfo supplierInfo)
    {
        List<SupplierInfo> list = supplierInfoService.selectSupplierInfoList(supplierInfo);
        ExcelUtil<SupplierInfo> util = new ExcelUtil<SupplierInfo>(SupplierInfo.class);
        util.exportExcel(response, list, "供应商数据");
    }

    /**
     * 获取供应商详细信息
     */
//    @RequiresPermissions("masterdata:info:query")
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id)
//    {
//        return AjaxResult.success(supplierInfoService.selectSupplierInfoById(id));
//    }

    /**
     * 新增供应商
     */
//    @RequiresPermissions("masterdata:info:add")
//    @Log(title = "供应商", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody SupplierInfo supplierInfo)
//    {
//        return toAjax(supplierInfoService.insertSupplierInfo(supplierInfo));
//    }



    /**
     * 删除供应商
     */
    //@RequiresPermissions("masterdata:info:remove")
    @ApiOperation("删除供应商")
    @Log(title = "供应商", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(supplierInfoService.deleteSupplierInfoByIds(ids));
    }

    /**
     * 新增供应商DTO
     */
    //@RequiresPermissions("masterdata:info:add")
    @ApiOperation("新增供应商")
    @Log(title = "供应商", businessType = BusinessType.INSERT)
    @PostMapping("/addSupplierInfo")
    public AjaxResult addSupplierInfo(@RequestBody SupplierInfoDTO supplierInfoDTO)
    {
        return toAjax(supplierInfoService.insertSupplierInfo(supplierInfoDTO));
    }

    /**
     * 修改供应商
     */
    //@RequiresPermissions("masterdata:info:edit")
    @ApiOperation("修改供应商")
    @Log(title = "供应商", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable("id") Long id,@RequestBody SupplierInfoDTO supplierInfoDTO)
    {
        supplierInfoDTO.setId(id);
        return toAjax(supplierInfoService.updateSupplierInfo(supplierInfoDTO));
    }

    /**
     * 查询供应商列表
     */
    //@RequiresPermissions("masterdata:info:list")
    @GetMapping("/list")
    @ApiOperation("查询供应商")
    public R<PageVO<SupplierInfoVO>> list(SupplierInfoDTO supplierInfoDTO)
    {
        startPage();
        List<SupplierInfoVO> list = supplierInfoService.selectSupplierInfoList(supplierInfoDTO);
        return R.ok(new PageVO<>(list,new PageInfo<>(list).getTotal()));
    }
}
