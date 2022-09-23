package com.bosch.masterdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.bosch.masterdata.domain.dto.MaterialDTO;
import com.bosch.masterdata.domain.vo.DepartmentVO;
import com.bosch.masterdata.domain.vo.MaterialVO;
import com.bosch.masterdata.domain.vo.PageVO;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
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
import com.bosch.masterdata.domain.Material;
import com.bosch.masterdata.service.IMaterialService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 物料信息Controller
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@Api(tags = "物料接口")
@RestController
@RequestMapping("/material")
public class MaterialController extends BaseController
{
    @Autowired
    private IMaterialService materialService;

    /**
     * 查询物料信息列表
     */
//    @RequiresPermissions("masterdata:material:list")
//    @GetMapping("/list")
//    public TableDataInfo list(Material material)
//    {
//        startPage();
//        List<Material> list = materialService.selectMaterialList(material);
//        List<MaterialVO> materialVOS = BeanConverUtil.converList(list, MaterialVO.class);
//        return getDataTable(list);
//    }

    /**
     * 导出物料信息列表
     */
    @RequiresPermissions("masterdata:material:export")
    @Log(title = "物料信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Material material)
    {
        List<Material> list = materialService.selectMaterialList(material);
        ExcelUtil<Material> util = new ExcelUtil<Material>(Material.class);
        util.exportExcel(response, list, "物料信息数据");
    }

    /**
     * 获取物料信息详细信息
     */
//    @RequiresPermissions("masterdata:material:query")
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id)
//    {
//        return AjaxResult.success(materialService.selectMaterialById(id));
//    }

    /**
     * 新增物料信息
     */
//    @RequiresPermissions("masterdata:material:add")
//    @Log(title = "物料信息", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody Material material)
//    {
//        return toAjax(materialService.insertMaterial(material));
//    }

    /**
     * 修改物料信息
     */
//    @RequiresPermissions("masterdata:material:edit")
//    @Log(title = "物料信息", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody Material material)
//    {
//        return toAjax(materialService.updateMaterial(material));
//    }

    /**
     * 删除物料信息
     */
    @RequiresPermissions("masterdata:material:remove")
    @Log(title = "物料信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(materialService.deleteMaterialByIds(ids));
    }

    /**
     * 查询物料信息
     */
    @ApiOperation("查询物料列表")
    @GetMapping("/materialVOList")
    public R<PageVO<MaterialVO>> list(MaterialDTO materialDTO)
    {
        startPage();
        List<MaterialVO> list = materialService.selectMaterialVOList(materialDTO);

        return R.ok(new PageVO<>(list,new PageInfo<>(list).getTotal()));
    }
    /**
     * 新增物料信息
     */
    //@RequiresPermissions("masterdata:material:add")
    @ApiOperation("新增物料")
    @Log(title = "物料信息", businessType = BusinessType.INSERT)
    @PostMapping("/addMaterial")
    public AjaxResult addMaterial(@RequestBody MaterialDTO materialDTO)
    {
        return toAjax(materialService.insertMaterialDTO(materialDTO));
    }

    /**
     * 修改物料信息
     */
    //@RequiresPermissions("masterdata:material:edit")
    @ApiOperation("修改物料")
    @Log(title = "物料信息", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable("id") Long id,@RequestBody MaterialDTO materialDTO)
    {
        materialDTO.setId(id);
        return toAjax(materialService.updateMaterial(materialDTO));
    }
}
