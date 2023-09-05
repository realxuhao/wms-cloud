package com.bosch.product.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.product.api.domain.SPDN;
import com.bosch.product.api.domain.SUDN;
import com.bosch.product.api.domain.dto.ProductPickDTO;
import com.bosch.product.api.domain.dto.SPDNDTO;
import com.bosch.product.api.domain.dto.SUDNDTO;
import com.bosch.product.api.domain.dto.SUDNShipDTO;
import com.bosch.product.api.domain.vo.ProductPickVO;
import com.bosch.product.api.domain.vo.SPDNVO;
import com.bosch.product.api.domain.vo.SUDNVO;
import com.bosch.product.service.IProductPickService;
import com.bosch.product.service.ISUDNService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private FileService fileService;

    @Autowired
    private IProductPickService pickService;

    @GetMapping(value = "/sudnList")
    @ApiOperation("sudn列表")
    public R<PageVO<SUDNVO>> getSpdnList(SUDNDTO sudndto) {
        startPage();
        List<SUDNVO> list = sudnService.getList(sudndto);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @GetMapping(value = "/getById/{id}")
    @ApiOperation("根据ID获取单个")
    public R<SUDN> getById(@PathVariable("id") Long id) {

        SUDN sudn = sudnService.getById(id);
        return R.ok(sudn);
    }


    @DeleteMapping(value = "/{ids}")
    @ApiOperation("删除SUDN")
    @Log(title = "删除SUDN", businessType = BusinessType.DELETE)
    public R batchDelete(@PathVariable Long[] ids) {
        sudnService.batchDelete(Arrays.asList(ids));
        return R.ok();
    }


    @PutMapping(value = "/{ids}")
    @ApiOperation("SUDN生成PICK")
    @Log(title = "SUDN生成PICK", businessType = BusinessType.INSERT)
    public R generatePick(@PathVariable Long[] ids) {
        sudnService.generate(Arrays.asList(ids));
        return R.ok();
    }


    @PostMapping(value = "/modifyQuantity")
    @ApiOperation("修改需求量")
    @Log(title = "SUDN修改需求量", businessType = BusinessType.UPDATE)
    public R modifyQuantity(@RequestBody SUDNDTO sudndto) {
        sudnService.modifyQuantity(sudndto.getId(), sudndto.getNewQuantity());
        return R.ok();
    }

    @PostMapping(value = "/ship")
    @ApiOperation("sudn发运")
    @Log(title = "SUDN发运", businessType = BusinessType.UPDATE)
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
            list=sudnService.getUnFinishedSUDN(sudndto);
        }else {
            list = sudnService.getFinishedSUDN(sudndto);
        }
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @GetMapping(value = "/getSUDNShipListByType")
    @ApiOperation("查询已发运和未发运的数据")
    public R<PageVO<SUDNVO>> getSUDNShipListByType(SUDNDTO sudndto) {

        if (sudndto.getType()==0) {
            startPage();

            List<SUDNVO> unFinishedShipSUDN = sudnService.getUnFinishedShipSUDN(sudndto);
            unFinishedShipSUDN.stream().forEach(item->{
                String delivery = item.getDelivery();
                ProductPickDTO pickDTO = new ProductPickDTO();
                pickDTO.setDelivery(delivery);
                pickDTO.setItem(item.getItem());
                pickDTO.setSudnId(item.getId());
                List<ProductPickVO> list = pickService.list(pickDTO);
                if (!CollectionUtils.isEmpty(list)){
                    List<String> collect = list.stream().map(ProductPickVO::getProductionBatch).collect(Collectors.toList());
                    item.setProductionBatchs(new HashSet<>(collect));
                }

            });
            return R.ok(new PageVO<>(unFinishedShipSUDN, new PageInfo<>(unFinishedShipSUDN).getTotal()));

        }else {
            startPage();

            List<SUDNVO> finishedShipSUDN = sudnService.getFinishedShipSUDN(sudndto);
            finishedShipSUDN.stream().forEach(item->{
                String delivery = item.getDelivery();
                ProductPickDTO pickDTO = new ProductPickDTO();
                pickDTO.setDelivery(delivery);
                pickDTO.setItem(item.getItem());
                pickDTO.setSudnId(item.getId());

                List<ProductPickVO> list = pickService.list(pickDTO);
                if (!CollectionUtils.isEmpty(list)){
                    List<String> collect = list.stream().map(ProductPickVO::getProductionBatch).collect(Collectors.toList());
                    item.setProductionBatchs(new HashSet<>(collect));
                }

            });
            return R.ok(new PageVO<>(finishedShipSUDN, new PageInfo<>(finishedShipSUDN).getTotal()));

        }
    }


    @PostMapping(value = "/importSUDN")
    @ApiOperation("SUDN导入")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件", name = "file", dataType = "File")
    })
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "SUDN导入", businessType = BusinessType.IMPORT)
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
