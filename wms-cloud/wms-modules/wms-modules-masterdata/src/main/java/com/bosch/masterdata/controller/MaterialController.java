package com.bosch.masterdata.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.dto.MaterialDTO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
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
import com.bosch.masterdata.api.domain.*;
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
public class MaterialController extends BaseController {
    @Autowired
    private IMaterialService materialService;

    @Autowired
    private FileService fileService;

    /**
     * 导出物料信息列表
     */
    //@RequiresPermissions("masterdata:material:export")
    @Log(title = "物料信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Material material) {
        List<Material> list = materialService.validMaterialList(material);
        ExcelUtil<Material> util = new ExcelUtil<Material>(Material.class);
        util.exportExcel(response, list, "物料信息数据");
    }


    /**
     * 删除物料信息
     */
    //@RequiresPermissions("masterdata:material:remove")
    @Log(title = "物料信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(materialService.deleteMaterialByIds(ids));
    }

    /**
     * 查询物料信息
     */
    @ApiOperation("查询物料列表")
    @GetMapping("/materialVOList")
    public R<PageVO<MaterialVO>> list(MaterialDTO materialDTO) {
        if (materialDTO == null) {
            materialDTO = new MaterialDTO();
        }
        if (materialDTO.getStatus() == null) {
            materialDTO.setStatus(1L);
        }
        startPage();
        List<MaterialVO> list = materialService.selectMaterialVOList(materialDTO);

        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @ApiOperation("查询物料列表")
    @GetMapping("/getByCell/{cell}")
    public R<List<MaterialVO>> getByCell(@PathVariable("cell") String cell) {
        MaterialDTO  materialDTO = new MaterialDTO();
        if (materialDTO.getStatus() == null) {
            materialDTO.setStatus(1L);
        }
        materialDTO.setCell(cell);
        List<MaterialVO> list = materialService.selectMaterialVOList(materialDTO);
        return R.ok(list);
    }
    /**
     * 新增物料信息
     */
    ////@RequiresPermissions("masterdata:material:add")
    @ApiOperation("新增物料")
    @Log(title = "物料信息", businessType = BusinessType.INSERT)
    @PostMapping("/addMaterial")
    public R addMaterial(@RequestBody MaterialDTO materialDTO) {
        return R.ok(materialService.insertMaterialDTO(materialDTO));
    }

    /**
     * 修改物料信息
     */
    ////@RequiresPermissions("masterdata:material:edit")
    @ApiOperation("修改物料")
    @Log(title = "物料信息", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable("id") Long id, @RequestBody MaterialDTO materialDTO) {
        materialDTO.setId(id);
        return toAjax(materialService.updateMaterial(materialDTO));
    }

    /**
     * 获取物料信息详细信息
     */
    ////@RequiresPermissions("masterdata:material:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取物料详情")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(materialService.selectMaterialVOById(id));
    }

    /**
     * 获取物料信息详细信息
     */
    ////@RequiresPermissions("masterdata:material:query")
    @GetMapping(value = "/getByMaterialCode/{materialCode}")
    @ApiOperation("根据物料编码获取获取物料详情")
    public R<MaterialVO> getInfoByMaterialCode(@PathVariable("materialCode") String materialCode) {
        return R.ok(materialService.selectMaterialVOBymaterialCode(materialCode));
    }

    /**
     * 批量上传物料明细
     */
    @ApiOperation("批量上传物料明细")
    @PostMapping(value = "/import", headers = "content-type=multipart/form-data")
    public R importExcel(@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {

        //解析文件服务
        R result = fileService.masterDataImport(file, ClassType.MATERIALDTO.getDesc());
        if (result.isSuccess()) {
            Object data = result.getData();
            List<MaterialDTO> materialDTOList = JSON.parseArray(JSON.toJSONString(data), MaterialDTO.class);

            if (CollectionUtils.isNotEmpty(materialDTOList)) {
                boolean valid = materialService.validMaterialList(materialDTOList);
                if (valid) {
                    return R.fail(400, "存在重复数据");
                } else {
                    //物料类型id
                    List<MaterialDTO> materialDTOS = materialService.setMaterialList(materialDTOList);
                    List<Material> materials = BeanConverUtil.converList(materialDTOS, Material.class);
                    materialService.saveBatch(materials);
                }
            } else {
                return R.fail("excel中无数据");
            }
            return R.ok(materialDTOList);
        } else {
            return R.fail(result.getMsg());
        }
    }

    /**
     * 批量上传物料明细
     */
    @ApiOperation("批量更新物料明细")
    @PostMapping(value = "/saveBatch", headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R saveBatchMaterial(@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {

        try {
            //解析文件服务
            R result = fileService.masterDataImport(file, ClassType.MATERIALDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<MaterialDTO> materialDTOList = JSON.parseArray(JSON.toJSONString(data), MaterialDTO.class);
                if (CollectionUtils.isNotEmpty(materialDTOList)) {
                    //物料类型id
                    List<MaterialDTO> materialDTOS = materialService.setMaterialList(materialDTOList);
                    //转换物料DO
                    List<Material> materials = BeanConverUtil.converList(materialDTOS, Material.class);
                    materials.forEach(r -> {
                        LambdaUpdateWrapper<Material> wrapper = new LambdaUpdateWrapper<Material>();
                        wrapper.eq(Material::getCode, r.getCode());
                        wrapper.eq(Material::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
                        boolean update = materialService.update(r, wrapper);
                        if (!update) {
                            r.setCreateBy(SecurityUtils.getUsername());
                            r.setCreateTime(DateUtils.getNowDate());
                            materialService.save(r);
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
