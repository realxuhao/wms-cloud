package com.bosch.storagein.controller;

import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.storagein.domain.dto.MaterialInDTO;
import com.bosch.storagein.domain.dto.MaterialReceiveDTO;
import com.bosch.storagein.domain.dto.request.EditBean;
import com.bosch.storagein.domain.vo.MaterialInVO;
import com.bosch.storagein.domain.vo.MaterialReceiveVO;
import com.bosch.storagein.service.IMaterialReceiveService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "原材料收货接口")
@RequestMapping("/material-receive")
public class MaterialReceiveCotroller extends BaseController {


    @Autowired
    private IMaterialReceiveService materialReceiveService;


    @GetMapping("/list")
    @ApiOperation("查询收货列表")
    public R<PageVO<MaterialReceiveVO>> list(MaterialReceiveDTO materialReceiveDTO) {
        startPage();
        List<MaterialReceiveVO> list = materialReceiveService.selectMaterialReceiveList(materialReceiveDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @Log(title = "原材料收货", businessType = BusinessType.DELETE)
    @ApiOperation("逻辑删除原材料收货")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(materialReceiveService.deleteMaterialReceiveById(id));
    }


    @ApiOperation("批量逻辑删除原材料收货")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(materialReceiveService.deleteMaterialReceiveByIds(ids));
    }

    @GetMapping(value = "/{id}")
    @ApiOperation("获取收货信息详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(materialReceiveService.selectById(id));
    }


    @PatchMapping("/edit")
    @ApiOperation("批量是否可编辑")
    public AjaxResult edit(@RequestBody EditBean editBean) {
        Asserts.notNull(editBean, "参数不能为空");
        Asserts.check(!CollectionUtils.isEmpty(editBean.getIds()), "id不能为空");
        Asserts.notNull(editBean.getEditFlag(), "editFlag不能为空");
        return AjaxResult.success((materialReceiveService.updateEditFlag(editBean.getIds(), editBean.getEditFlag())));
    }


}
