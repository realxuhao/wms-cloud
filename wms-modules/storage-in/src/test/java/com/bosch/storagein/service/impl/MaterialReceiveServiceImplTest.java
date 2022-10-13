package com.bosch.storagein.service.impl;

import com.bosch.storagein.api.domain.vo.MaterialReceiveVO;
import com.bosch.storagein.service.IMaterialReceiveService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: UWH4SZH
 * @since: 10/11/2022 15:51
 * @description:
 */
@SpringBootTest
class MaterialReceiveServiceImplTest {

    @Autowired
    private IMaterialReceiveService materialReceiveService;

    private String mesBarCode;

    @BeforeEach
    public void init() {
        mesBarCode = "20170826669006391110000015100961661611251128000060";
    }

    @Test
    void selectMaterialReceiveList() {
    }

    @Test
    void deleteMaterialReceiveById() {
    }

    @Test
    void deleteMaterialReceiveByIds() {
    }

    @Test
    void selectByMesBarCode() {
        List<MaterialReceiveVO> materialReceiveVOS = materialReceiveService.selectByMesBarCode(mesBarCode);
        Assertions.assertNotNull(materialReceiveVOS);
    }

    @Test
    void selectById() {
        MaterialReceiveVO materialReceiveVO = materialReceiveService.selectById(1L);
        Assertions.assertNotNull(materialReceiveVO);
    }

    @Test
    void updateEditFlag() {
    }
}