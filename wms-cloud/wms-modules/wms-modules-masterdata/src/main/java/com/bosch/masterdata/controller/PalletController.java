package com.bosch.masterdata.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.bosch.masterdata.api.domain.dto.PalletDTO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.domain.vo.PalletVO;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.service.IPalletService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;

/**
 * 托盘Controller
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@RestController
@Api(tags = "托盘接口")
@RequestMapping("/pallet")
public class PalletController extends BaseController
{
    @Autowired
    private IPalletService palletService;



    /**
     * 导出托盘列表
     */
    //@RequiresPermissions("masterdata:pallet:export")
    @Log(title = "托盘", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Pallet pallet)
    {
        List<Pallet> list = palletService.selectPalletList(pallet);
        ExcelUtil<Pallet> util = new ExcelUtil<Pallet>(Pallet.class);
        util.exportExcel(response, list, "托盘数据");
    }


    /**
     * 删除托盘
     */
    ////@RequiresPermissions("masterdata:pallet:remove")
    @ApiOperation("删除托盘")
    @Log(title = "托盘", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(palletService.deletePalletByIds(ids));
    }


    /**
     * 查询托盘列表
     */
    ////@RequiresPermissions("masterdata:pallet:list")
    @GetMapping("/list")
    @ApiOperation("查询托盘列表")
    public R<PageVO<PalletVO>> list(PalletDTO palletDTO)
    {
        startPage();
        List<PalletVO> list = palletService.selectPalletList(palletDTO);
        return R.ok(new PageVO<>(list,new PageInfo<>(list).getTotal()));
    }
    /**
     * 新增托盘
     */
    ////@RequiresPermissions("masterdata:pallet:add")
    @Log(title = "托盘", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增托盘")
    public R add(@Validated @RequestBody PalletDTO palletDTO)
    {
        try {
            List<PalletDTO> dtos=new ArrayList<>();
            dtos.add(palletDTO);
            boolean b = palletService.validDTO(dtos);
            if (b){
                return R.fail(400,"存在重复类型");
            }
            Pallet pallet = BeanConverUtil.conver(palletDTO, Pallet.class);
            return R.ok(palletService.save(pallet));
        }catch (Exception ex){
            return R.fail(ex.getMessage());
        }

    }

    /**
     * 修改托盘
     */
    ////@RequiresPermissions("masterdata:pallet:edit")
    @Log(title = "托盘", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    @ApiOperation("修改托盘")
    public R edit(@PathVariable("id") Long id,@RequestBody PalletDTO palletDTO)
    {
        try {
            palletDTO.setId(id);
            boolean b = palletService.validDTO(palletDTO);
            if (b){
                return R.fail(400,"存在重复类型");
            }
            Pallet pallet = BeanConverUtil.conver(palletDTO, Pallet.class);
            return R.ok(palletService.updateById(pallet));
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }
    /**
     * 获取托盘详细信息
     */
    ////@RequiresPermissions("masterdata:pallet:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取托盘详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(palletService.selectPalletById(id));
    }

    @GetMapping(value = "/getByType/{palletType}")
    @ApiOperation("根据类型获取托盘信息")
    public R<Pallet> getByType(@PathVariable("palletType") String palletType)
    {
        return R.ok(palletService.selectPalletByType(palletType));
    }
}
