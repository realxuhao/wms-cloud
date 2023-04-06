package com.bosch.masterdata.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.MaterialBin;
import com.bosch.masterdata.api.domain.ProductFrame;
import com.bosch.masterdata.api.domain.dto.MaterialBinDTO;
import com.bosch.masterdata.api.domain.dto.ProductFrameDTO;
import com.bosch.masterdata.api.domain.vo.MaterialBinVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.domain.vo.ProductFrameVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.masterdata.service.IProductFrameService;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.ruoyi.common.core.utils.PageUtils.startPage;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-31 11:23
 **/
@RestController
@RequestMapping("/productFrame")
@Api(tags = "成品物料库位分配规则接口")
public class ProductFrameController extends BaseController {

    @Autowired
    private IProductFrameService productFrameService;

    @Autowired
    private FileService fileService;

    @GetMapping("/getProductFrame")
    @ApiOperation("根据code和仓库查询成品跨规则列表")
    public R<List<ProductFrameVO>> getProductFrame(@RequestParam("materialCode") String materialCode, @RequestParam("wareCode") String wareCode)
    {
        List<ProductFrameVO> productFrameVOList = productFrameService.getBinInRule(materialCode,wareCode);
        return R.ok(productFrameVOList);
    }

    /**
     * 新增物料库位分配策略
     */
    @RequiresPermissions("masterdata:bin:add")
    @Log(title = "物料库位分配策略", businessType = BusinessType.INSERT)
    @ApiOperation("新增物料库位分配策略")
    @PostMapping
    public R add(@RequestBody ProductFrameDTO dto) {

        try {

            boolean b = productFrameService.validOne(dto);
            if (!b) {
                return R.fail(400, "存在重复数据");
            }
            return R.ok(productFrameService.insertProductFrame(BeanConverUtil.conver(dto, ProductFrame.class)));
        }catch (Exception ex){
            return R.fail(ex.getMessage());
        }

    }

    /**
     * 修改物料库位分配策略
     */
    @RequiresPermissions("masterdata:bin:edit")
    @Log(title = "物料库位分配策略", businessType = BusinessType.UPDATE)
    @ApiOperation("修改物料库位分配策略")
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable("id") Long id, @RequestBody ProductFrameDTO productFrameDTO) {
        productFrameDTO.setId(id);
        return toAjax(productFrameService.updateProductFrame(BeanConverUtil.conver(productFrameDTO, ProductFrame.class)));
    }

    /**
     * 删除物料库位分配策略
     */
    @RequiresPermissions("masterdata:bin:remove")
    @Log(title = "物料库位分配策略", businessType = BusinessType.DELETE)
    @ApiOperation("删除物料库位分配策略")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(productFrameService.deleteProductFrameByIds(ids));
    }

    /**
     * 查询物料库位分配策略列表
     */
    @RequiresPermissions("masterdata:bin:list")
    @PostMapping("/list")
    @ApiOperation("查询物料库位规则列表")
    public R<PageVO<ProductFrameVO>> list(@RequestBody ProductFrameDTO productFrameDTO) {
        startPage();
        List<ProductFrameVO> list = productFrameService.selectProductFrameList(productFrameDTO);

        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    /**
     * 获取物料库位分配策略详细信息
     */
    @RequiresPermissions("masterdata:bin:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("根据id查询物料库位规则")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(productFrameService.selectProductFrameById(id));
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
            R result = fileService.masterDataImport(file, ClassType.PRODUCTFRAMEDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<ProductFrameDTO> dtos = JSON.parseArray(JSON.toJSONString(data), ProductFrameDTO.class);
                if (CollectionUtils.isNotEmpty(dtos)) {
                    boolean b = productFrameService.validList(dtos);
                    if (!b) {
                        return R.fail(400, "存在重复数据");
                    }
                    //dto赋值
                    List<ProductFrameDTO> productFrameDTOS = productFrameService.setValue(dtos);
                    //添加
                    List<ProductFrame> dos = BeanConverUtil.converList(productFrameDTOS, ProductFrame.class);
                    productFrameService.saveBatch(dos);
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
            R result = fileService.masterDataImport(file, ClassType.PRODUCTFRAMEDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<ProductFrameDTO> dtos = JSON.parseArray(JSON.toJSONString(data), ProductFrameDTO.class);
                if (CollectionUtils.isNotEmpty(dtos)) {
                    //dto赋值
                    List<ProductFrameDTO> productFrameDTOS = productFrameService.setValue(dtos);
                    //转换DO
                    List<ProductFrame> dos = BeanConverUtil.converList(productFrameDTOS, ProductFrame.class);
                    dos.forEach(r -> {
                        LambdaUpdateWrapper<ProductFrame> wrapper = new LambdaUpdateWrapper<ProductFrame>();
                        wrapper.eq(ProductFrame::getMaterialCode, r.getMaterialCode());
                        wrapper.eq(ProductFrame::getFrameTypeCode, r.getFrameTypeCode());
                        boolean update = productFrameService.update(r, wrapper);
                        if (!update) {
                            productFrameService.save(r);
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
