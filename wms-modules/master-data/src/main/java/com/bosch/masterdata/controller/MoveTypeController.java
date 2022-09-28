package com.bosch.masterdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.bosch.masterdata.domain.dto.MoveTypeDTO;
import com.bosch.masterdata.domain.vo.MaterialTypeVO;
import com.bosch.masterdata.domain.vo.MoveTypeVO;
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
import com.bosch.masterdata.domain.MoveType;
import com.bosch.masterdata.service.IMoveTypeService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 移动类型配置Controller
 *
 * @author xuhao
 * @date 2022-09-22
 */
@RestController
@RequestMapping("/moveType")
@Api(tags = "移动类型接口")
public class MoveTypeController extends BaseController {
    @Autowired
    private IMoveTypeService moveTypeService;

    /**
     * 查询移动类型配置列表
     */
    @RequiresPermissions("masterdata:type:list")
    @ApiOperation("查询移动类型列表")
    @GetMapping("/list")
    public R<PageVO<MoveTypeVO>> list(MoveTypeDTO moveTypeDTO) {
        startPage();
        List<MoveTypeVO> list = moveTypeService.selectMoveTypeList(moveTypeDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    /**
     * 导出移动类型配置列表
     */
    @RequiresPermissions("masterdata:type:export")
    @Log(title = "移动类型配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MoveTypeDTO moveType) {
        List<MoveTypeVO> list = moveTypeService.selectMoveTypeList(moveType);
        ExcelUtil<MoveType> util = new ExcelUtil<MoveType>(MoveType.class);
        util.exportExcel(response, BeanConverUtil.converList(list, MoveType.class), "移动类型配置数据");
    }

    /**
     * 获取移动类型配置详细信息
     */
    @RequiresPermissions("masterdata:type:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(moveTypeService.selectMoveTypeById(id));
    }

    /**
     * 新增移动类型配置
     */
//    @RequiresPermissions("masterdata:type:add")
    @ApiOperation("增加移动类型")
    @Log(title = "移动类型配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MoveTypeDTO moveType) {
        return toAjax(moveTypeService.insertMoveType(moveType));
    }

    /**
     * 修改移动类型配置
     */
    @RequiresPermissions("masterdata:type:edit")
    @Log(title = "移动类型配置", businessType = BusinessType.UPDATE)
    @ApiOperation("修改移动类型")
    @PutMapping
    public AjaxResult edit(@RequestBody MoveTypeDTO moveTypeDTO) {
        return toAjax(moveTypeService.updateMoveType(moveTypeDTO));
    }

    /**
     * 删除移动类型配置
     */
    @RequiresPermissions("masterdata:type:remove")
    @Log(title = "移动类型配置", businessType = BusinessType.DELETE)
    @ApiOperation("删除移动类型")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(moveTypeService.deleteMoveTypeByIds(ids));
    }
}
