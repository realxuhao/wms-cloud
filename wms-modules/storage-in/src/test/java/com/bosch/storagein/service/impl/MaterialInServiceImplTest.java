package com.bosch.storagein.service.impl;

import com.bosch.storagein.api.domain.vo.MaterialInVO;
import com.bosch.storagein.service.IMaterialInService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

/**
 * @author: UWH4SZH
 * @since: 10/11/2022 15:41
 * @description:
 */
@SpringBootTest
class MaterialInServiceImplTest {

    @Autowired
    private IMaterialInService materialInService;

    @Test
    void getMaterialSampleInfo() {
    }

    @Test
    void check() {
    }

    @Test
    void selectById() {
        MaterialInVO materialInVO = materialInService.selectById(Long.valueOf(1));
        Assert.isNull(materialInVO);

    }

    @Test
    void selectBySsccNumber() {
    }

    @Test
    void getByUserName() {
    }

    @Test
    void selectMaterialInList() {
    }
}