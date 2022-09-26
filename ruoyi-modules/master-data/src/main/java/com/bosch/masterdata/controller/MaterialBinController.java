package com.bosch.masterdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.bosch.masterdata.domain.dto.MaterialBinDTO;
import com.bosch.masterdata.domain.vo.MaterialBinVO;
import com.bosch.masterdata.domain.vo.PageVO;
import com.bosch.masterdata.utils.BeanConverUtil;
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
import com.bosch.masterdata.domain.MaterialBin;
import com.bosch.masterdata.service.IMaterialBinService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 物料库位分配策略Controller
 *
 * @author xuhao
 * @date 2022-09-22
 */
@RestController
@RequestMapping("/materialBin")
@Api(tags = "物料库位分配规则接口")
public class MaterialBinController extends BaseController {
    @Autowired
    private IMaterialBinService materialBinService;

    /**
     * 查询物料库位分配策略列表
     */
    @RequiresPermissions("masterdata:bin:list")
    @GetMapping("/list")
    @ApiOperation("查询物料库位规则列表")
    public R<PageVO<MaterialBinVO>> list(MaterialBinDTO materialBinDTO) {
        startPage();
        List<MaterialBinVO> list = materialBinService.selectMaterialBinList(materialBinDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    /**
     * 导出物料库位分配策略列表
     */
    @RequiresPermissions("masterdata:bin:export")
    @Log(title = "物料库位分配策略", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MaterialBinDTO materialBinDTO) {
        List<MaterialBinVO> list = materialBinService.selectMaterialBinList(materialBinDTO);
        ExcelUtil<MaterialBin> util = new ExcelUtil<MaterialBin>(MaterialBin.class);
        util.exportExcel(response, BeanConverUtil.converList(list, MaterialBin.class), "物料库位分配策略数据");
    }

    /**
     * 获取物料库位分配策略详细信息
     */
    @RequiresPermissions("masterdata:bin:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("根据id查询物料库位规则")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(materialBinService.selectMaterialBinById(id));
    }

    /**
     * 新增物料库位分配策略
     */
    @RequiresPermissions("masterdata:bin:add")
    @Log(title = "物料库位分配策略", businessType = BusinessType.INSERT)
    @ApiOperation("新增物料库位分配策略")
    @PostMapping
    public AjaxResult add(@RequestBody MaterialBinDTO materialBinDTO) {
        return toAjax(materialBinService.insertMaterialBin(BeanConverUtil.conver(materialBinDTO, MaterialBin.class)));
    }

    /**
     * 修改物料库位分配策略
     */
    @RequiresPermissions("masterdata:bin:edit")
    @Log(title = "物料库位分配策略", businessType = BusinessType.UPDATE)
    @ApiOperation("修改物料库位分配策略")
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable("id") Long id, @RequestBody MaterialBinDTO materialBinDTO) {
        materialBinDTO.setId(id);
        return toAjax(materialBinService.updateMaterialBin(BeanConverUtil.conver(materialBinDTO, MaterialBin.class)));
    }

    /**
     * 删除物料库位分配策略
     */
    @RequiresPermissions("masterdata:bin:remove")
    @Log(title = "物料库位分配策略", businessType = BusinessType.DELETE)
    @ApiOperation("删除物料库位分配策略")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(materialBinService.deleteMaterialBinByIds(ids));
    }
}
