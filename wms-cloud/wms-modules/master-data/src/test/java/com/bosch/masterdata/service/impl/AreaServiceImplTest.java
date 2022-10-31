package com.bosch.masterdata.service.impl;

import com.bosch.masterdata.api.domain.Area;
import com.bosch.masterdata.mapper.AreaMapper;
import com.bosch.masterdata.mapper.WareMapper;
import com.bosch.masterdata.service.IAreaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AreaServiceImplTest {

    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private WareMapper wareMapper;

    @Autowired
    private IAreaService areaService;
    @Test
    void selectAreaById() {

        Area area = areaService.selectAreaById(5);
    }

    @Test
    void testSelectAreaById() {
    }

    @Test
    void selectAreaList() {
    }

    @Test
    void selectAreaVOList() {
    }

    @Test
    void insertArea() {
    }

    @Test
    void testInsertArea() {
    }

    @Test
    void updateArea() {
    }

    @Test
    void testUpdateArea() {
    }

    @Test
    void deleteAreaByIds() {
    }

    @Test
    void deleteAreaById() {
    }

    @Test
    void getTypeMap() {
    }

    @Test
    void validList() {
    }

    @Test
    void setValue() {
    }
}