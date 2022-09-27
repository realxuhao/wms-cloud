package com.bosch.masterdata.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.bosch.masterdata.domain.dto.FrameDTO;
import com.bosch.masterdata.domain.vo.FrameVO;
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
import com.bosch.masterdata.domain.Frame;
import com.bosch.masterdata.service.IFrameService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 跨Controller
 *
 * @author xuhao
 * @date 2022-09-26
 */
@RestController
@Api(tags = "跨接口")
@RequestMapping("/frame")
public class FrameController extends BaseController {
    @Autowired
    private IFrameService frameService;

    /**
     * 查询跨列表
     */
    @RequiresPermissions("masterdata:frame:list")
    @GetMapping("/list")
    @ApiOperation("查询跨列表")
    public R<PageVO<FrameVO>> list(FrameDTO frameDTO) {
        startPage();
        List<FrameVO> list = frameService.selectFrameList(frameDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    /**
     * 导出跨列表
     */
    @RequiresPermissions("masterdata:frame:export")
    @Log(title = "跨", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FrameDTO frameDTO) {
        List<FrameVO> list = frameService.selectFrameList(frameDTO);
        ExcelUtil<Frame> util = new ExcelUtil<Frame>(Frame.class);
        util.exportExcel(response, BeanConverUtil.converList(list, Frame.class), "跨数据");
    }

    /**
     * 获取跨详细信息
     */
    @RequiresPermissions("masterdata:frame:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("根据id查询跨详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(frameService.selectFrameById(id));
    }

    /**
     * 新增跨
     */
    @RequiresPermissions("masterdata:frame:add")
    @Log(title = "跨", businessType = BusinessType.INSERT)
    @ApiOperation("新增跨")
    @PostMapping
    public AjaxResult add(@RequestBody FrameDTO frameDTO) {
        return toAjax(frameService.insertFrame(frameDTO));
    }

    /**
     * 修改跨
     */
    @RequiresPermissions("masterdata:frame:edit")
    @Log(title = "跨", businessType = BusinessType.UPDATE)
    @ApiOperation("更新跨")
    @PutMapping
    public AjaxResult edit(@RequestBody FrameDTO frameDTO) {
        return toAjax(frameService.updateFrame(frameDTO));
    }

    /**
     * 删除跨
     */
    @RequiresPermissions("masterdata:frame:remove")
    @Log(title = "跨", businessType = BusinessType.DELETE)
    @ApiOperation("删除跨")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(frameService.deleteFrameByIds(ids));
    }
}
