package com.bosch.masterdata.controller;

import com.bosch.masterdata.api.domain.vo.ProductFrameVO;
import com.bosch.masterdata.service.IProductFrameService;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-31 11:23
 **/
@RestController
@RequestMapping("/productFrame")
@Api(tags = "成品物料库位分配规则接口")
public class ProductFrameController {

    @Autowired
    private IProductFrameService productFrameService;

    @GetMapping("/getProductFrame")
    @ApiOperation("根据code和仓库查询成品跨规则列表")
    public R<List<ProductFrameVO>> getProductFrame(@RequestParam("materialCode") String materialCode, @RequestParam("wareCode") String wareCode)
    {
        List<ProductFrameVO> productFrameVOList = productFrameService.getBinInRule(materialCode,wareCode);
        return R.ok(productFrameVOList);
    }
}
