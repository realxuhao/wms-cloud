package com.bosch.masterdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.bosch.masterdata.api.domain.dto.WareDTO;
import com.bosch.masterdata.api.domain.vo.FactoryVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.domain.vo.WareSelectVO;
import com.bosch.masterdata.api.domain.vo.WareVO;
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
import com.bosch.masterdata.api.domain.Ware;
import com.bosch.masterdata.service.IWareService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;

/**
 * 仓库Controller
 *
 * @author xuhao
 * @date 2022-09-26
 */
@RestController
@RequestMapping("/ware")
@Api(tags = "仓库接口")
public class WareController extends BaseController {
    @Autowired
    private IWareService wareService;

    /**
     * 查询仓库列表
     */
    @RequiresPermissions("masterdata:ware:list")
    @GetMapping("/list")
    @ApiOperation("查询仓库列表")
    public R<PageVO<WareVO>> list(WareDTO wareDTO) {
        startPage();
        List<Ware> list = wareService.selectWareList(BeanConverUtil.conver(wareDTO, Ware.class));
        List<WareVO> wareVOS = BeanConverUtil.converList(list, WareVO.class);
        return R.ok(new PageVO<>(wareVOS, new PageInfo<>(wareVOS).getTotal()));

    }

    /**
     * 查询所有工厂
     */
    @GetMapping("/factory/list")
    @ApiOperation("查询所有工厂")
    public R<List<FactoryVO>> list() {
        List<FactoryVO> list = wareService.selectFactoryList();
        return R.ok(list);
    }


    /**
     * 获取所有仓库
     */
    @GetMapping("/all/list")
    @ApiOperation("获取所有仓库")
    public R<List<WareSelectVO>> wareList() {
        List<WareSelectVO> list = wareService.fetchWareList();
        return R.ok(list);
    }


    /**
     * 导出仓库列表
     */
    @RequiresPermissions("masterdata:ware:export")
    @Log(title = "仓库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Ware ware) {
        List<Ware> list = wareService.selectWareList(ware);
        ExcelUtil<Ware> util = new ExcelUtil<Ware>(Ware.class);
        util.exportExcel(response, list, "仓库数据");
    }

    /**
     * 获取仓库详细信息
     */
    @RequiresPermissions("masterdata:ware:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取仓库详细信息")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(wareService.selectWareById(id));
    }

    /**
     * 新增仓库
     */
    @RequiresPermissions("masterdata:ware:add")
    @Log(title = "仓库", businessType = BusinessType.INSERT)
    @ApiOperation("新增仓库")
    @PostMapping
    public AjaxResult add(@RequestBody WareDTO wareDTO) {
        return toAjax(wareService.insertWare(BeanConverUtil.conver(wareDTO,Ware.class)));
    }

    /**
     * 修改仓库
     */
    @RequiresPermissions("masterdata:ware:edit")
    @Log(title = "仓库", businessType = BusinessType.UPDATE)
    @ApiOperation("修改仓库")
    @PutMapping
    public AjaxResult edit(@RequestBody WareDTO wareDTO) {
        return toAjax(wareService.updateWare(BeanConverUtil.conver(wareDTO,Ware.class)));
    }

    /**
     * 删除仓库
     */
    @RequiresPermissions("masterdata:ware:remove")
    @Log(title = "仓库", businessType = BusinessType.DELETE)
    @ApiOperation("删除仓库")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(wareService.deleteWareByIds(ids));
    }
}
