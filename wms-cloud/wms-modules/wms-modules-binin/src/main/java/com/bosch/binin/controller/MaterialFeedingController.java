package com.bosch.binin.controller;

import com.bosch.binin.api.domain.dto.BinInDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-09 13:05
 **/

@Controller
public class MaterialFeedingController {


    @Autowired



    @PostMapping(value = "/call")
    @ApiOperation("叫料")
    public R<BinInVO> in(@RequestParam("file") MultipartFile file,
                         @RequestParam("sortType") String sortType,
                         @RequestParam("cell") String cell) {


        return  null;
    }

}
