package com.bosch.product.controller;

import com.alibaba.fastjson2.JSON;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.product.api.domain.SPDN;
import com.bosch.product.api.domain.SUDN;
import com.bosch.product.api.domain.dto.SPDNDTO;
import com.bosch.product.api.domain.dto.SUDNDTO;
import com.bosch.product.api.domain.dto.SUDNShipDTO;
import com.bosch.product.api.domain.vo.SPDNVO;
import com.bosch.product.api.domain.vo.SUDNVO;
import com.bosch.product.service.ISUDNService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-26 15:19
 **/
@RestController
@Api(tags = "SUDN发运")
@RequestMapping("/SUDN")
public class SudnController extends BaseController {

    @Autowired
    private ISUDNService sudnService;

    @Autowired
    private FileService fileService;

    @GetMapping(value = "/sudnList")
    @ApiOperation("sudn列表")
    public R<PageVO<SUDNVO>> getSpdnList(SUDNDTO sudndto) {
        startPage();
        List<SUDNVO> list = sudnService.getList(sudndto);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @DeleteMapping(value = "/{ids}")
    @ApiOperation("删除spdn")
    public R batchDelete(@PathVariable Long[] ids) {
        sudnService.batchDelete(Arrays.asList(ids));
        return R.ok();
    }


    @PutMapping(value = "/{ids}")
    @ApiOperation("SUDN生成PICK")
    public R generatePick(@PathVariable Long[] ids) {
        sudnService.generate(Arrays.asList(ids));
        return R.ok();
    }


    @PostMapping(value = "/modifyQuantity")
    @ApiOperation("修改需求量")
    public R modifyQuantity(@RequestBody SUDNDTO sudndto) {
        sudnService.modifyQuantity(sudndto.getId(), sudndto.getNewQuantity());
        return R.ok();
    }

    @PostMapping(value = "/ship")
    @ApiOperation("sudn发运")
    public R ship(@RequestBody SUDNShipDTO shipDTO) {
        sudnService.ship(shipDTO);
        return R.ok();
    }




    @GetMapping(value = "/getSUDNListByType")
    @ApiOperation("查询已完成和未完成的数据")
    public R<PageVO<SUDNVO>> getUnFinishedSUDN(SUDNDTO sudndto) {
        startPage();

        List<SUDNVO> list = new ArrayList<>();
        if (sudndto.getType()==0) {
            list=sudnService.getUnFinishedSUDN();
        }else {
            list = sudnService.getFinishedSUDN();
        }
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @GetMapping(value = "/getSUDNShipListByType")
    @ApiOperation("查询已发运和未发运的数据")
    public R<PageVO<SUDNVO>> getSUDNShipListByType(SUDNDTO sudndto) {

        if (sudndto.getType()==0) {
            startPage();

            List<SUDNVO> unFinishedShipSUDN = sudnService.getUnFinishedShipSUDN();
            return R.ok(new PageVO<>(unFinishedShipSUDN, new PageInfo<>(unFinishedShipSUDN).getTotal()));

        }else {
            startPage();

            List<SUDNVO> finishedShipSUDN = sudnService.getFinishedShipSUDN();
            return R.ok(new PageVO<>(finishedShipSUDN, new PageInfo<>(finishedShipSUDN).getTotal()));

        }
    }


    @PostMapping(value = "/importSUDN")
    @ApiOperation("SUDN导入")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件", name = "file", dataType = "File")
    })
    @Transactional(rollbackFor = Exception.class)
    public R importSPDN(@RequestParam(value = "file") MultipartFile file) {

        R result = fileService.SUDNFileImport(file, ClassType.SUDNDTO.getDesc());
        if (result.isSuccess()) {
            Object data = result.getData();
            List<SUDNDTO> dtos = JSON.parseArray(JSON.toJSONString(data), SUDNDTO.class);
            if (!CollectionUtils.isEmpty(dtos)) {
                sudnService.validList(dtos);
                List<SUDN> sudnList = BeanConverUtil.converList(dtos, SUDN.class);
                sudnService.saveBatch(sudnList);

            }
            return R.ok(null);
        } else {
            return R.fail(result.getMsg());
        }

    }





//    @GetMapping(value = "/getFinishedSUDN")
//    @ApiOperation("已完成")
//    public R<PageVO<SUDNVO>> getFinishedSUDN() {
//        startPage();
//        List<SUDNVO> list = sudnService.getUnFinishedSUDN();
//        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
//    }


}
