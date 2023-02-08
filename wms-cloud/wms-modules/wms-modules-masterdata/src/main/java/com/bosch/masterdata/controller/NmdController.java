package com.bosch.masterdata.controller;

import com.bosch.masterdata.api.domain.Nmd;
import com.bosch.masterdata.api.domain.TimeWindow;
import com.bosch.masterdata.api.domain.dto.NmdDTO;
import com.bosch.masterdata.api.domain.dto.TimeWindowDTO;
import com.bosch.masterdata.api.domain.vo.NmdVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.domain.vo.TimeWindowVO;
import com.bosch.masterdata.service.IMoveTypeService;
import com.bosch.masterdata.service.INmdService;
import com.bosch.masterdata.service.ITimeWindowService;
import com.bosch.masterdata.service.impl.NmdServiceImpl;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    /**
     * 查询列表
     */
    @RequiresPermissions("masterdata:window:list")
    @ApiOperation("查询列表")
    @GetMapping("/list")
    public R<PageVO<NmdVO>> list(NmdDTO nmdDTO) {
        startPage();
        List<NmdVO> list = nmdService.selectList(nmdDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }
//
//
//    /**
//     * 获取详细信息
//     */
//    @ApiOperation("获取详细信息")
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id) {
//        return AjaxResult.success(timeWindowService.selectTimeWindowById(id));
//    }
//    /**
//     * 新增
//     */
//    @RequiresPermissions("masterdata:window:add")
//    @Log(title = "道口时间窗口", businessType = BusinessType.INSERT)
//    @PostMapping
//    @ApiOperation("新增")
//    public AjaxResult add(@RequestBody TimeWindowDTO timeWindowDTO) {
//        return toAjax(timeWindowService.insertTimeWindow(timeWindowDTO));
//    }
//
//    /**
//     * 修改
//     */
//    @RequiresPermissions("masterdata:window:edit")
//    @Log(title = "道口时间窗口", businessType = BusinessType.UPDATE)
//    @ApiOperation("修改")
//    @PutMapping
//    public AjaxResult edit(@RequestBody TimeWindowDTO timeWindowDTO) {
//        return toAjax(timeWindowService.updateTimeWindow(timeWindowDTO));
//    }


}
