package com.bosch.binin.controller;

import com.bosch.storagein.api.constants.ResponseConstants;
import com.bosch.storagein.api.domain.vo.MaterialInCheckVO;
import com.bosch.storagein.api.domain.vo.MaterialReceiveVO;
import com.bosch.storagein.api.enumeration.MaterialStatusEnum;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: UWH4SZH
 * @since: 10/18/2022 10:13
 * @description:
 */
@RestController
@RequestMapping("/testController")
public class TestController {

    @GetMapping(value = "/test")
    public String test() {
        return "test controller success";
    }
}
