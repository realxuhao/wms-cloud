package com.bosch.storagein.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bosch.storagein.api.domain.dto.MaterialReceiveDTO;
import com.bosch.storagein.api.domain.vo.MaterialReceiveVO;
import com.bosch.storagein.mapper.MaterialRecevieMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MaterialReceiveServiceImplTest {

    @Mock
    private MaterialRecevieMapper mockMaterialRecevieMapper;

    @InjectMocks
    private MaterialReceiveServiceImpl materialReceiveServiceImplUnderTest;

    @Test
    void testSelectMaterialReceiveList() {
        // Setup
        final MaterialReceiveDTO materialReceiveDTO = new MaterialReceiveDTO();
//        materialReceiveDTO.setSsccNumber("ssccNumber");
        materialReceiveDTO.setBatchNb("batchNumber");
        materialReceiveDTO.setMaterialNb("materialNb");
        materialReceiveDTO.setBatchNb("batchNb");
        materialReceiveDTO.setFromPurchaseOrder("fromPurchaseOrder");
        materialReceiveDTO.setStatus(0);
        materialReceiveDTO.setEditFlag(0);

        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
        materialReceiveVO.setId(0L);
        materialReceiveVO.setPlantNb("plantNb");
        materialReceiveVO.setSsccNumber("ssccNumber");
        materialReceiveVO.setMaterialNb("materialNb");
        materialReceiveVO.setMaterialName("materialName");
        materialReceiveVO.setBatchNb("batchNb");
        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        materialReceiveVO.setUnit("unit");
        materialReceiveVO.setQuantity(Double.valueOf(0));
        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
        materialReceiveVO.setPoNumberItem("poNumberItem");
        materialReceiveVO.setUploadUser("uploadUser");
        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        materialReceiveVO.setUpdateUser("updateUser");
        materialReceiveVO.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final List<MaterialReceiveVO> expectedResult = Arrays.asList(materialReceiveVO);

        // Configure MaterialRecevieMapper.selectMaterialReceiveVOList(...).
        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
        materialReceiveVO1.setId(0L);
        materialReceiveVO1.setPlantNb("plantNb");
        materialReceiveVO1.setSsccNumber("ssccNumber");
        materialReceiveVO1.setMaterialNb("materialNb");
        materialReceiveVO1.setMaterialName("materialName");
        materialReceiveVO1.setBatchNb("batchNb");
        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        materialReceiveVO1.setUnit("unit");
        materialReceiveVO1.setQuantity(Double.valueOf(0));
        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
        materialReceiveVO1.setPoNumberItem("poNumberItem");
        materialReceiveVO1.setUploadUser("uploadUser");
        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        materialReceiveVO1.setUpdateUser("updateUser");
        materialReceiveVO1.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO1);
        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
                .thenReturn(materialReceiveVOS);

        // Run the test
        final List<MaterialReceiveVO> result = materialReceiveServiceImplUnderTest.selectMaterialReceiveList(
                materialReceiveDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testSelectMaterialReceiveList_MaterialRecevieMapperReturnsNoItems() {
        // Setup
        final MaterialReceiveDTO materialReceiveDTO = new MaterialReceiveDTO();
//        materialReceiveDTO.setSsccNumber("ssccNumber");
        materialReceiveDTO.setBatchNb("batchNumber");
        materialReceiveDTO.setMaterialNb("materialNb");
        materialReceiveDTO.setBatchNb("batchNb");
        materialReceiveDTO.setFromPurchaseOrder("fromPurchaseOrder");
        materialReceiveDTO.setStatus(0);
        materialReceiveDTO.setEditFlag(0);

        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
                .thenReturn(Collections.emptyList());

        // Run the test
        final List<MaterialReceiveVO> result = materialReceiveServiceImplUnderTest.selectMaterialReceiveList(
                materialReceiveDTO);

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testDeleteMaterialReceiveById() {
        // Setup
        when(mockMaterialRecevieMapper.deleteMaterialReceiveVOById(0L)).thenReturn(0);

        // Run the test
        final Integer result = materialReceiveServiceImplUnderTest.deleteMaterialReceiveById(0L);

        // Verify the results
        assertThat(result).isEqualTo(0);
    }

    @Test
    void testDeleteMaterialReceiveByIds() {
        // Setup
        when(mockMaterialRecevieMapper.deleteMaterialReceiveVOByIds(any(Long[].class))).thenReturn(0);

        // Run the test
        final Integer result = materialReceiveServiceImplUnderTest.deleteMaterialReceiveByIds(new Long[]{0L});

        // Verify the results
        assertThat(result).isEqualTo(0);
    }

    @Test
    void testSelectByMesBarCode() {
        // Setup
        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
        materialReceiveVO.setId(0L);
        materialReceiveVO.setPlantNb("plantNb");
        materialReceiveVO.setSsccNumber("ssccNumber");
        materialReceiveVO.setMaterialNb("materialNb");
        materialReceiveVO.setMaterialName("materialName");
        materialReceiveVO.setBatchNb("batchNb");
        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        materialReceiveVO.setUnit("unit");
        materialReceiveVO.setQuantity(Double.valueOf(0));
        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
        materialReceiveVO.setPoNumberItem("poNumberItem");
        materialReceiveVO.setUploadUser("uploadUser");
        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        materialReceiveVO.setUpdateUser("updateUser");
        materialReceiveVO.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final List<MaterialReceiveVO> expectedResult = Arrays.asList(materialReceiveVO);

        // Configure MaterialRecevieMapper.selectMaterialReceiveVOList(...).
        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
        materialReceiveVO1.setId(0L);
        materialReceiveVO1.setPlantNb("plantNb");
        materialReceiveVO1.setSsccNumber("ssccNumber");
        materialReceiveVO1.setMaterialNb("materialNb");
        materialReceiveVO1.setMaterialName("materialName");
        materialReceiveVO1.setBatchNb("batchNb");
        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        materialReceiveVO1.setUnit("unit");
        materialReceiveVO1.setQuantity(Double.valueOf(0));
        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
        materialReceiveVO1.setPoNumberItem("poNumberItem");
        materialReceiveVO1.setUploadUser("uploadUser");
        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        materialReceiveVO1.setUpdateUser("updateUser");
        materialReceiveVO1.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO1);
        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
                .thenReturn(materialReceiveVOS);

        // Run the test
        final List<MaterialReceiveVO> result = materialReceiveServiceImplUnderTest.selectSameBatchMaterial("mesBarCode");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testSelectByMesBarCode_MaterialRecevieMapperReturnsNoItems() {
        // Setup
        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
                .thenReturn(Collections.emptyList());

        // Run the test
        final List<MaterialReceiveVO> result = materialReceiveServiceImplUnderTest.selectSameBatchMaterial("mesBarCode");

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testSelectById() {
        // Setup
        final MaterialReceiveVO expectedResult = new MaterialReceiveVO();
        expectedResult.setId(0L);
        expectedResult.setPlantNb("plantNb");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setMaterialName("materialName");
        expectedResult.setBatchNb("batchNb");
        expectedResult.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        expectedResult.setUnit("unit");
        expectedResult.setQuantity(Double.valueOf(0));
        expectedResult.setFromPurchaseOrder("fromPurchaseOrder");
        expectedResult.setPoNumberItem("poNumberItem");
        expectedResult.setUploadUser("uploadUser");
        expectedResult.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        expectedResult.setUpdateUser("updateUser");
        expectedResult.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());

        // Configure MaterialRecevieMapper.selectMaterialReceiveVOById(...).
        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
        materialReceiveVO.setId(0L);
        materialReceiveVO.setPlantNb("plantNb");
        materialReceiveVO.setSsccNumber("ssccNumber");
        materialReceiveVO.setMaterialNb("materialNb");
        materialReceiveVO.setMaterialName("materialName");
        materialReceiveVO.setBatchNb("batchNb");
        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        materialReceiveVO.setUnit("unit");
        materialReceiveVO.setQuantity(Double.valueOf(0));
        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
        materialReceiveVO.setPoNumberItem("poNumberItem");
        materialReceiveVO.setUploadUser("uploadUser");
        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        materialReceiveVO.setUpdateUser("updateUser");
        materialReceiveVO.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        when(mockMaterialRecevieMapper.selectMaterialReceiveVOById(0L)).thenReturn(materialReceiveVO);

        // Run the test
        final MaterialReceiveVO result = materialReceiveServiceImplUnderTest.selectById(0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testUpdateEditFlag() {
        // Setup
        when(mockMaterialRecevieMapper.updateEditFlag(Arrays.asList(0L), 0)).thenReturn(0);

        // Run the test
        final Integer result = materialReceiveServiceImplUnderTest.updateEditFlag(Arrays.asList(0L), 0);

        // Verify the results
        assertThat(result).isEqualTo(0);
    }

    @Test
    void testValidList() {
        // Setup
        when(mockMaterialRecevieMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0);

        // Run the test
        final boolean result = materialReceiveServiceImplUnderTest.validList(Arrays.asList("value"));

        // Verify the results
        assertThat(result).isFalse();
    }
}
