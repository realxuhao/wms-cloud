package com.bosch.storagein.controller;

import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.storagein.domain.dto.MaterialReceiveDTO;
import com.bosch.storagein.domain.vo.MaterialReceiveVO;
import com.bosch.storagein.service.IMaterialReceiveService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "原材料收货接口")
@RequestMapping("/material-receive")
public class MaterialReceiveCotroller extends BaseController {


    @Autowired
    private IMaterialReceiveService materialReceiveService;


//    @RequiresPermissions("masterdata:department:list")
    @GetMapping("/list")
    @ApiOperation("查询收货列表")
    public R<PageVO<MaterialReceiveVO>> list(MaterialReceiveDTO materialReceiveDTO) {
        startPage();
        List<MaterialReceiveVO> list = materialReceiveService.selectMaterialReceiveList(materialReceiveDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    //    @RequiresPermissions("masterdata:area:remove")
    @Log(title = "原材料收货", businessType = BusinessType.DELETE)
    @ApiOperation("逻辑删除原材料收货")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(materialReceiveService.deleteMaterialReceiveById(id));
    }

    //    @RequiresPermissions("masterdata:frame:remove")
//    @Log(title = "原材料收货", businessType = BusinessType.DELETE)
    @ApiOperation("批量逻辑删除原材料收货")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(materialReceiveService.deleteMaterialReceiveByIds(ids));
    }


}
