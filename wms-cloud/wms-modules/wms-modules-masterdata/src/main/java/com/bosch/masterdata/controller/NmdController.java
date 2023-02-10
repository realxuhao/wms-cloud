package com.bosch.masterdata.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.Material;
import com.bosch.masterdata.api.domain.Nmd;
import com.bosch.masterdata.api.domain.TimeWindow;
import com.bosch.masterdata.api.domain.dto.MaterialDTO;
import com.bosch.masterdata.api.domain.dto.NmdDTO;
import com.bosch.masterdata.api.domain.dto.TimeWindowDTO;
import com.bosch.masterdata.api.domain.vo.NmdVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.domain.vo.TimeWindowVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.masterdata.service.IMoveTypeService;
import com.bosch.masterdata.service.INmdService;
import com.bosch.masterdata.service.ITimeWindowService;
import com.bosch.masterdata.service.impl.NmdServiceImpl;
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

/**
 * 道口时间窗口Controller
 *
 * @author xuhao
 * @date 2022-09-22
 */
@RestController
@RequestMapping("/nmd")
@Api(tags = "nmd主数据")
public class NmdController extends BaseController {

    @Autowired
    private INmdService nmdService;

    @Autowired
    private FileService fileService;

    /**
     * 查询列表
     */
    @ApiOperation("查询列表")
    @PostMapping("/list")
    public R<PageVO<NmdVO>> list(@RequestBody NmdDTO nmdDTO) {
        startPage();
        List<NmdVO> list = nmdService.selectList(nmdDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    /**
     * 获取详细信息
     */
    @ApiOperation("获取详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(nmdService.selectNmdById(id));
    }
    /**
     * 新增
     */
    @Log(title = "新增Nmd", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增")
    public AjaxResult add(@RequestBody NmdDTO nmdDTO) {
        return toAjax(nmdService.insertNmd(nmdDTO));
    }

    /**
     * 修改
     */
    @Log(title = "修改Nmd", businessType = BusinessType.UPDATE)
    @ApiOperation("修改")
    @PutMapping
    public AjaxResult edit(@RequestBody NmdDTO nmdDTO) {
        Nmd nmd = BeanConverUtil.conver(nmdDTO, Nmd.class);
        return toAjax(nmdService.updateById(nmd));
    }

    /**
     * 删除
     */
    @Log(title = "删除Nmd", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    @DeleteMapping("/{ids}")
    public AjaxResult delete(@RequestParam Long[] ids) {
        return toAjax(nmdService.deleteNmd(ids));
    }

    /**
     * 批量上传
     */
    @ApiOperation("批量上传")
    @PostMapping(value = "/import", headers = "content-type=multipart/form-data")
    public R importExcel(@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {

        //解析文件服务
        R result = fileService.masterDataImport(file, ClassType.NMDDTO.getDesc());
        if (result.isSuccess()) {
            Object data = result.getData();
            List<NmdDTO> nmdDTOList = JSON.parseArray(JSON.toJSONString(data), NmdDTO.class);
            if (CollectionUtils.isNotEmpty(nmdDTOList)) {
                boolean valid = nmdService.validNmdList(nmdDTOList);
                if (valid) {
                    return R.fail(400, "存在重复数据");
                } else {
                    List<Nmd> nmds = BeanConverUtil.converList(nmdDTOList, Nmd.class);
                    nmdService.saveBatch(nmds);
                }
            } else {
                return R.fail("excel中无数据");
            }
            return R.ok(nmdDTOList);
        } else {
            return R.fail(result.getMsg());
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
            R result = fileService.masterDataImport(file, ClassType.NMDDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<NmdDTO> nmdDTOList = JSON.parseArray(JSON.toJSONString(data), NmdDTO.class);
                if (CollectionUtils.isNotEmpty(nmdDTOList)) {
                    //转换DO
                    List<Nmd> nmds = BeanConverUtil.converList(nmdDTOList, Nmd.class);
                    nmds.forEach(r -> {
                        LambdaUpdateWrapper<Nmd> wrapper = new LambdaUpdateWrapper<Nmd>();
                        wrapper.eq(Nmd::getMaterialCode, r.getMaterialCode());
                        boolean update = nmdService.update(r, wrapper);
                        if (!update) {
                            r.setCreateBy(SecurityUtils.getUsername());
                            r.setCreateTime(DateUtils.getNowDate());
                            nmdService.save(r);
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
