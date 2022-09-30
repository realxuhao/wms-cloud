package com.bosch.masterdata.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.bosch.masterdata.api.FileFeign;
import com.bosch.masterdata.enumeration.ClassType;
import com.bosch.masterdata.api.domain.dto.MaterialDTO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.bosch.masterdata.api.domain.Material;
import com.bosch.masterdata.service.IMaterialService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private FileFeign fileFeign;


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

    /**
     * 获取物料信息详细信息
     */
    //@RequiresPermissions("masterdata:material:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取物料详情")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(materialService.selectMaterialVOById(id));
    }

    /**
     * 获取物料信息详细信息
     */
    //@RequiresPermissions("masterdata:material:query")
    @GetMapping(value = "/{materialCode}")
    @ApiOperation("根据物料编码获取获取物料详情")
    public AjaxResult getInfoByMaterialCode(@PathVariable("materialCode") String materialCode)
    {
        return AjaxResult.success(materialService.selectMaterialVOBymaterialCode(materialCode));
    }

    /**
     * 批量上传物料明细
     */
    @ApiOperation("批量上传物料明细")
    @PostMapping(value = "/import", headers = "content-type=multipart/form-data")
    public R importExcelSubject(@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {

        R result = fileFeign.read(file,ClassType.MATERIALDTO.getDesc());
        if (result.isSuccess()){
            Object data = result.getData();
        }
        else {

        }

        return result;

    }
}
