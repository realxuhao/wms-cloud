package com.bosch.product.controller;


import com.bosch.product.api.domain.dto.ShippingPlanDTO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@Api(tags = "成品打包接口")
@RequestMapping("/productPackaging")
public class ProductPackagingController extends BaseController {



    @PostMapping(value = "/lsit")
    @ApiOperation("成品导入list")
    public R<ShippingPlanDTO> allocate(@RequestBody  ShippingPlanDTO dto) {
        try {
            return R.ok();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }




}
