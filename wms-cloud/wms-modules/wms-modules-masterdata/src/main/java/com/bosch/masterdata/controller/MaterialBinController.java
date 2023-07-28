package com.bosch.masterdata.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.domain.Area;
import com.bosch.masterdata.api.domain.Material;
import com.bosch.masterdata.api.domain.dto.AreaDTO;
import com.bosch.masterdata.api.domain.dto.MaterialBinDTO;
import com.bosch.masterdata.api.domain.dto.MaterialDTO;
import com.bosch.masterdata.api.domain.vo.MaterialBinVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.masterdata.service.IMaterialService;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.web.page.PageDomain;
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
import com.bosch.masterdata.api.domain.MaterialBin;
import com.bosch.masterdata.service.IMaterialBinService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private IMaterialService materialService;

    @Autowired
    private FileService fileService;


    /**
     * 查询物料库位分配策略列表
     */

    @GetMapping("/getListByMaterial")
    @ApiOperation("查询物料库位规则列表")
    public R<List<MaterialBinVO>> getListByMaterial(@RequestParam("materialCode") String materialCode) {
        try {

            List<MaterialBinVO> listByMaterial = materialBinService.getListByMaterial(materialCode);
            return R.ok(listByMaterial);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }

    }

    /**
     * 根据code和仓库查询物料库位分配策略列表
     */

    @GetMapping("/getListByMaterialAndWare")
    @ApiOperation("根据code和仓库查询物料库位规则列表")
    public R<List<MaterialBinVO>> getListByMaterialAndWare(@RequestParam("materialCode") String materialCode,@RequestParam("wareCode") String wareCode)
    {
        try {

            List<MaterialBinVO> listByMaterial = materialBinService.getListByMaterial(materialCode,wareCode);
            return R.ok(listByMaterial);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }

    }
    /**
     * 查询物料库位分配策略列表
     */
    //@RequiresPermissions("masterdata:bin:list")
    @PostMapping("/list")
    @ApiOperation("查询物料库位规则列表")
    public R<PageVO<MaterialBinVO>> list(@RequestBody MaterialBinDTO materialBinDTO) {
        PageDomain pageDomain=BeanConverUtil.conver(materialBinDTO,PageDomain.class);

        startPage(pageDomain);
        List<MaterialBinVO> list = materialBinService.selectMaterialBinList(materialBinDTO);

        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    /**
     * 导出物料库位分配策略列表
     */
    //@RequiresPermissions("masterdata:bin:export")
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
    //@RequiresPermissions("masterdata:bin:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("根据id查询物料库位规则")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(materialBinService.selectMaterialBinById(id));
    }

    /**
     * 新增物料库位分配策略
     */
    //@RequiresPermissions("masterdata:bin:add")
    @Log(title = "物料库位分配策略", businessType = BusinessType.INSERT)
    @ApiOperation("新增物料库位分配策略")
    @PostMapping
    public R add(@RequestBody MaterialBinDTO materialBinDTO) {

        try {

            boolean b = materialBinService.validOne(materialBinDTO);
            if (!b) {
                return R.fail(400, "存在重复数据");
            }
            return R.ok(materialBinService.insertMaterialBin(BeanConverUtil.conver(materialBinDTO, MaterialBin.class)));
        }catch (Exception ex){
            return R.fail(ex.getMessage());
        }

    }

    /**
     * 修改物料库位分配策略
     */
    //@RequiresPermissions("masterdata:bin:edit")
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
    //@RequiresPermissions("masterdata:bin:remove")
    @Log(title = "物料库位分配策略", businessType = BusinessType.DELETE)
    @ApiOperation("删除物料库位分配策略")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") Long[] ids) {
        return toAjax(materialBinService.deleteMaterialBinByIds(ids));
    }

    /**
     * 批量上传
     */
    @ApiOperation("批量上传")
    @PostMapping(value = "/import", headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R importExcel(@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {
        try {
            //解析文件服务
            R result = fileService.masterDataImport(file, ClassType.MATERIALBINDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<MaterialBinDTO> dtos = JSON.parseArray(JSON.toJSONString(data), MaterialBinDTO.class);
                if (CollectionUtils.isNotEmpty(dtos)) {
                    boolean b = materialBinService.validList(dtos);
                    if (!b) {
                        return R.fail(400, "存在重复数据");
                    }
                    //dto赋值
                    List<MaterialBinDTO> materialBinDTOS = materialBinService.setValue(dtos);
                    //添加
                    List<MaterialBin> dos = BeanConverUtil.converList(materialBinDTOS, MaterialBin.class);
                    materialBinService.saveBatch(dos);
                } else {
                    return R.fail("excel中无数据");
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
     * 批量更新
     */
    @ApiOperation("批量更新")
    @PostMapping(value = "/saveBatch", headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R saveBatch(@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {

        try {
            //解析文件服务
            R result = fileService.masterDataImport(file, ClassType.MATERIALBINDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<MaterialBinDTO> dtos = JSON.parseArray(JSON.toJSONString(data), MaterialBinDTO.class);
                if (CollectionUtils.isNotEmpty(dtos)) {
                    //dto赋值
                    List<MaterialBinDTO> materialBinDTOS = materialBinService.setValue(dtos);
                    //转换DO
                    List<MaterialBin> dos = BeanConverUtil.converList(materialBinDTOS, MaterialBin.class);
                    dos.forEach(r -> {
                        LambdaUpdateWrapper<MaterialBin> wrapper = new LambdaUpdateWrapper<MaterialBin>();
                        wrapper.eq(MaterialBin::getMaterialCode, r.getMaterialCode());
                        wrapper.eq(MaterialBin::getFrameTypeCode, r.getFrameTypeCode());
                        boolean update = materialBinService.update(r, wrapper);
                        if (!update) {
                            r.setCreateBy(SecurityUtils.getUsername());
                            r.setCreateTime(DateUtils.getNowDate());
                            materialBinService.save(r);
                        }
                    });
                } else {
                    return R.fail("excel中无数据");
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
