package com.bosch.masterdata.controller;

import com.bosch.masterdata.api.domain.MaterialType;
import com.bosch.masterdata.api.domain.vo.MesBarCodeVO;
import com.bosch.masterdata.service.IMesBarCodeService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: wms-cloud
 * @description: mesbarcode controller
 * @author: xuhao
 * @create: 2022-12-08 17:23
 **/
@RestController
@RequestMapping("/mesBarCode")
public class MesBarCodeController {

    @Autowired
    private IMesBarCodeService mesBarCodeService;



    /**
     * 解析mesbarcode
     */
    @GetMapping("/parseMesBarCode/{mesBarCode}")
    public R<MesBarCodeVO> parseMesBarCode(@PathVariable("mesBarCode") String mesBarCode) {
        return R.ok(mesBarCodeService.parseMesBarCode(mesBarCode));
    }
}
