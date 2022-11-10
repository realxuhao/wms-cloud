package com.bosch.binin.controller;

import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.dto.MaterialCallQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.MaterialCallVO;
import com.bosch.binin.api.enumeration.BinInFileTypeEnum;
import com.bosch.binin.service.IMaterialCallService;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-09 13:05
 **/

@RestController
@Api(tags = "叫料接口")
@RequestMapping("/material-feeding")
public class MaterialFeedingController extends BaseController {


    @Autowired
    private FileService fileService;

    @Autowired
    private IMaterialCallService materialCallService;




    @PostMapping(value = "/call")
    @ApiOperation("叫料")
    public R<MaterialCall> call(@RequestParam(value = "file") MultipartFile file,
                                @RequestParam("sortType") String sortType,
                                @RequestParam("cell") String cell) {
        //解析文件服务
        R result = fileService.masterDataImport(file, BinInFileTypeEnum.MATERIALCALL.getDesc());

        return  null;
    }




    @GetMapping(value = "/call/list")
    @ApiOperation("叫料需求列表查询")
    public R<PageVO<MaterialCallVO>> list(MaterialCallQueryDTO queryDTO) {
        startPage();
        List<MaterialCallVO> list = materialCallService.getMaterialCallList(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));

    }

}
