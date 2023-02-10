//package com.bosch.storagein.service.impl;
//
//import com.bosch.masterdata.api.RemoteMaterialService;
//import com.bosch.masterdata.api.domain.vo.MaterialVO;
//import com.bosch.storagein.api.domain.dto.MaterialInCheckDTO;
//import com.bosch.storagein.api.domain.dto.MaterialInDTO;
//import com.bosch.storagein.api.domain.dto.MaterialReceiveDTO;
//import com.bosch.storagein.api.domain.vo.MaterialCheckResultVO;
//import com.bosch.storagein.api.domain.vo.MaterialInCheckVO;
//import com.bosch.storagein.api.domain.vo.MaterialInVO;
//import com.bosch.storagein.api.domain.vo.MaterialReceiveVO;
//import com.bosch.storagein.mapper.MaterialInMapper;
//import com.bosch.storagein.mapper.MaterialRecevieMapper;
//import com.ruoyi.common.core.domain.R;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class MaterialInServiceImplTest {
//
//    @Mock
//    private RemoteMaterialService mockRemoteMaterialService;
//    @Mock
//    private MaterialRecevieMapper mockMaterialRecevieMapper;
//    @Mock
//    private MaterialInMapper mockMaterialInMapper;
//
//    @InjectMocks
//    private MaterialInServiceImpl materialInServiceImplUnderTest;
//
//    @Test
//    void testGetMaterialCheckInfo() {
//        // Setup
//        final MaterialInCheckVO expectedResult = new MaterialInCheckVO();
//        expectedResult.setMesBarCode("mesBarCode");
//        expectedResult.setSsccNumber("ssccNumber");
//        expectedResult.setMaterialNb("materialNb");
//        expectedResult.setMaterialName("materialName");
//        expectedResult.setBatchNb("batchNb");
//        expectedResult.setCheckQuantity(0);
//        expectedResult.setMinStandard(0.0);
//        expectedResult.setMaxStandard(0.0);
//        expectedResult.setCheckType(0);
//        expectedResult.setCheckTypeDesc("desc");
//        expectedResult.setUnit("unit");
//        expectedResult.setFromPurchaseOrder("fromPurchaseOrder");
//        expectedResult.setPoNumberItem("poNumberItem");
//        expectedResult.setTotalPallet(0);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final MaterialVO materialVO = new MaterialVO();
//        materialVO.setId(0L);
//        materialVO.setCode("code");
//        materialVO.setName("materialName");
//        materialVO.setMinPackageNumber(0L);
//        materialVO.setMaterialTypeId(0L);
//        materialVO.setMaterialType("materialType");
//        materialVO.setUnit("unit");
//        materialVO.setErrorProofingMethod("errorProofingMethod");
//        materialVO.setHasPallet(0);
//        materialVO.setBindPallet(0);
//        materialVO.setPackageWeight(new BigDecimal("0.00"));
//        materialVO.setMinPackageNetWeight(new BigDecimal("0.00"));
//        materialVO.setLessDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setMoreDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setTransferWeightRatio(new BigDecimal("0.00"));
//        final R<MaterialVO> materialVOR = R.ok(materialVO);
//        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//        // Configure MaterialRecevieMapper.selectSameBatchMaterialReceiveVO(...).
//        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
//        materialReceiveVO.setId(0L);
//        materialReceiveVO.setPlantNb("plantNb");
//        materialReceiveVO.setSsccNumber("ssccNumber");
//        materialReceiveVO.setMaterialNb("materialNb");
//        materialReceiveVO.setMaterialName("materialName");
//        materialReceiveVO.setBatchNb("batchNb");
//        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUnit("unit");
//        materialReceiveVO.setQuantity(0);
//        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO.setPoNumberItem("poNumberItem");
//        materialReceiveVO.setUploadUser("uploadUser");
//        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUpdateUser("updateUser");
//        materialReceiveVO.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO);
//        when(mockMaterialRecevieMapper.selectSameBatchMaterialReceiveVO("materialNb", "batchNb"))
//                .thenReturn(materialReceiveVOS);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOList(...).
//        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
//        materialReceiveVO1.setId(0L);
//        materialReceiveVO1.setPlantNb("plantNb");
//        materialReceiveVO1.setSsccNumber("ssccNumber");
//        materialReceiveVO1.setMaterialNb("materialNb");
//        materialReceiveVO1.setMaterialName("materialName");
//        materialReceiveVO1.setBatchNb("batchNb");
//        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUnit("unit");
//        materialReceiveVO1.setQuantity(0);
//        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO1.setPoNumberItem("poNumberItem");
//        materialReceiveVO1.setUploadUser("uploadUser");
//        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUpdateUser("updateUser");
//        materialReceiveVO1.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS1 = Arrays.asList(materialReceiveVO1);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
//                .thenReturn(materialReceiveVOS1);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOBySncc(...).
//        final MaterialReceiveVO materialReceiveVO2 = new MaterialReceiveVO();
//        materialReceiveVO2.setId(0L);
//        materialReceiveVO2.setPlantNb("plantNb");
//        materialReceiveVO2.setSsccNumber("ssccNumber");
//        materialReceiveVO2.setMaterialNb("materialNb");
//        materialReceiveVO2.setMaterialName("materialName");
//        materialReceiveVO2.setBatchNb("batchNb");
//        materialReceiveVO2.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUnit("unit");
//        materialReceiveVO2.setQuantity(0);
//        materialReceiveVO2.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO2.setPoNumberItem("poNumberItem");
//        materialReceiveVO2.setUploadUser("uploadUser");
//        materialReceiveVO2.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUpdateUser("updateUser");
//        materialReceiveVO2.setStatus(0);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOBySncc("ssccNumber")).thenReturn(materialReceiveVO2);
//
//        // Run the test
//        final MaterialInCheckVO result = materialInServiceImplUnderTest.getMaterialCheckInfo("mesBarCode");
//
//        // Verify the results
//        assertThat(result).isEqualTo(expectedResult);
//        verify(mockMaterialRecevieMapper).updateStatusBySscc("ssccNumber", 0);
//    }
//
//    @Test
//    void testGetMaterialCheckInfo_RemoteMaterialServiceReturnsNoItem() {
//        // Setup
//        final MaterialInCheckVO expectedResult = new MaterialInCheckVO();
//        expectedResult.setMesBarCode("mesBarCode");
//        expectedResult.setSsccNumber("ssccNumber");
//        expectedResult.setMaterialNb("materialNb");
//        expectedResult.setMaterialName("materialName");
//        expectedResult.setBatchNb("batchNb");
//        expectedResult.setCheckQuantity(0);
//        expectedResult.setMinStandard(0.0);
//        expectedResult.setMaxStandard(0.0);
//        expectedResult.setCheckType(0);
//        expectedResult.setCheckTypeDesc("desc");
//        expectedResult.setUnit("unit");
//        expectedResult.setFromPurchaseOrder("fromPurchaseOrder");
//        expectedResult.setPoNumberItem("poNumberItem");
//        expectedResult.setTotalPallet(0);
//
//        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(R.ok());
//
//        // Configure MaterialRecevieMapper.selectSameBatchMaterialReceiveVO(...).
//        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
//        materialReceiveVO.setId(0L);
//        materialReceiveVO.setPlantNb("plantNb");
//        materialReceiveVO.setSsccNumber("ssccNumber");
//        materialReceiveVO.setMaterialNb("materialNb");
//        materialReceiveVO.setMaterialName("materialName");
//        materialReceiveVO.setBatchNb("batchNb");
//        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUnit("unit");
//        materialReceiveVO.setQuantity(0);
//        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO.setPoNumberItem("poNumberItem");
//        materialReceiveVO.setUploadUser("uploadUser");
//        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUpdateUser("updateUser");
//        materialReceiveVO.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO);
//        when(mockMaterialRecevieMapper.selectSameBatchMaterialReceiveVO("materialNb", "batchNb"))
//                .thenReturn(materialReceiveVOS);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOList(...).
//        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
//        materialReceiveVO1.setId(0L);
//        materialReceiveVO1.setPlantNb("plantNb");
//        materialReceiveVO1.setSsccNumber("ssccNumber");
//        materialReceiveVO1.setMaterialNb("materialNb");
//        materialReceiveVO1.setMaterialName("materialName");
//        materialReceiveVO1.setBatchNb("batchNb");
//        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUnit("unit");
//        materialReceiveVO1.setQuantity(0);
//        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO1.setPoNumberItem("poNumberItem");
//        materialReceiveVO1.setUploadUser("uploadUser");
//        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUpdateUser("updateUser");
//        materialReceiveVO1.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS1 = Arrays.asList(materialReceiveVO1);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
//                .thenReturn(materialReceiveVOS1);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOBySncc(...).
//        final MaterialReceiveVO materialReceiveVO2 = new MaterialReceiveVO();
//        materialReceiveVO2.setId(0L);
//        materialReceiveVO2.setPlantNb("plantNb");
//        materialReceiveVO2.setSsccNumber("ssccNumber");
//        materialReceiveVO2.setMaterialNb("materialNb");
//        materialReceiveVO2.setMaterialName("materialName");
//        materialReceiveVO2.setBatchNb("batchNb");
//        materialReceiveVO2.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUnit("unit");
//        materialReceiveVO2.setQuantity(0);
//        materialReceiveVO2.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO2.setPoNumberItem("poNumberItem");
//        materialReceiveVO2.setUploadUser("uploadUser");
//        materialReceiveVO2.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUpdateUser("updateUser");
//        materialReceiveVO2.setStatus(0);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOBySncc("ssccNumber")).thenReturn(materialReceiveVO2);
//
//        // Run the test
//        final MaterialInCheckVO result = materialInServiceImplUnderTest.getMaterialCheckInfo("mesBarCode");
//
//        // Verify the results
//        assertThat(result).isEqualTo(expectedResult);
//        verify(mockMaterialRecevieMapper).updateStatusBySscc("ssccNumber", 0);
//    }
//
//    @Test
//    void testGetMaterialCheckInfo_RemoteMaterialServiceReturnsFailure() {
//        // Setup
//        final MaterialInCheckVO expectedResult = new MaterialInCheckVO();
//        expectedResult.setMesBarCode("mesBarCode");
//        expectedResult.setSsccNumber("ssccNumber");
//        expectedResult.setMaterialNb("materialNb");
//        expectedResult.setMaterialName("materialName");
//        expectedResult.setBatchNb("batchNb");
//        expectedResult.setCheckQuantity(0);
//        expectedResult.setMinStandard(0.0);
//        expectedResult.setMaxStandard(0.0);
//        expectedResult.setCheckType(0);
//        expectedResult.setCheckTypeDesc("desc");
//        expectedResult.setUnit("unit");
//        expectedResult.setFromPurchaseOrder("fromPurchaseOrder");
//        expectedResult.setPoNumberItem("poNumberItem");
//        expectedResult.setTotalPallet(0);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final R<MaterialVO> materialVOR = R.fail();
//        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//        // Configure MaterialRecevieMapper.selectSameBatchMaterialReceiveVO(...).
//        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
//        materialReceiveVO.setId(0L);
//        materialReceiveVO.setPlantNb("plantNb");
//        materialReceiveVO.setSsccNumber("ssccNumber");
//        materialReceiveVO.setMaterialNb("materialNb");
//        materialReceiveVO.setMaterialName("materialName");
//        materialReceiveVO.setBatchNb("batchNb");
//        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUnit("unit");
//        materialReceiveVO.setQuantity(0);
//        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO.setPoNumberItem("poNumberItem");
//        materialReceiveVO.setUploadUser("uploadUser");
//        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUpdateUser("updateUser");
//        materialReceiveVO.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO);
//        when(mockMaterialRecevieMapper.selectSameBatchMaterialReceiveVO("materialNb", "batchNb"))
//                .thenReturn(materialReceiveVOS);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOList(...).
//        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
//        materialReceiveVO1.setId(0L);
//        materialReceiveVO1.setPlantNb("plantNb");
//        materialReceiveVO1.setSsccNumber("ssccNumber");
//        materialReceiveVO1.setMaterialNb("materialNb");
//        materialReceiveVO1.setMaterialName("materialName");
//        materialReceiveVO1.setBatchNb("batchNb");
//        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUnit("unit");
//        materialReceiveVO1.setQuantity(0);
//        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO1.setPoNumberItem("poNumberItem");
//        materialReceiveVO1.setUploadUser("uploadUser");
//        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUpdateUser("updateUser");
//        materialReceiveVO1.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS1 = Arrays.asList(materialReceiveVO1);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
//                .thenReturn(materialReceiveVOS1);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOBySncc(...).
//        final MaterialReceiveVO materialReceiveVO2 = new MaterialReceiveVO();
//        materialReceiveVO2.setId(0L);
//        materialReceiveVO2.setPlantNb("plantNb");
//        materialReceiveVO2.setSsccNumber("ssccNumber");
//        materialReceiveVO2.setMaterialNb("materialNb");
//        materialReceiveVO2.setMaterialName("materialName");
//        materialReceiveVO2.setBatchNb("batchNb");
//        materialReceiveVO2.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUnit("unit");
//        materialReceiveVO2.setQuantity(0);
//        materialReceiveVO2.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO2.setPoNumberItem("poNumberItem");
//        materialReceiveVO2.setUploadUser("uploadUser");
//        materialReceiveVO2.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUpdateUser("updateUser");
//        materialReceiveVO2.setStatus(0);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOBySncc("ssccNumber")).thenReturn(materialReceiveVO2);
//
//        // Run the test
//        final MaterialInCheckVO result = materialInServiceImplUnderTest.getMaterialCheckInfo("mesBarCode");
//
//        // Verify the results
//        assertThat(result).isEqualTo(expectedResult);
//        verify(mockMaterialRecevieMapper).updateStatusBySscc("ssccNumber", 0);
//    }
//
//    @Test
//    void testGetMaterialCheckInfo_MaterialRecevieMapperSelectSameBatchMaterialReceiveVOReturnsNoItems() {
//        // Setup
//        final MaterialInCheckVO expectedResult = new MaterialInCheckVO();
//        expectedResult.setMesBarCode("mesBarCode");
//        expectedResult.setSsccNumber("ssccNumber");
//        expectedResult.setMaterialNb("materialNb");
//        expectedResult.setMaterialName("materialName");
//        expectedResult.setBatchNb("batchNb");
//        expectedResult.setCheckQuantity(0);
//        expectedResult.setMinStandard(0.0);
//        expectedResult.setMaxStandard(0.0);
//        expectedResult.setCheckType(0);
//        expectedResult.setCheckTypeDesc("desc");
//        expectedResult.setUnit("unit");
//        expectedResult.setFromPurchaseOrder("fromPurchaseOrder");
//        expectedResult.setPoNumberItem("poNumberItem");
//        expectedResult.setTotalPallet(0);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final MaterialVO materialVO = new MaterialVO();
//        materialVO.setId(0L);
//        materialVO.setCode("code");
//        materialVO.setName("materialName");
//        materialVO.setMinPackageNumber(0L);
//        materialVO.setMaterialTypeId(0L);
//        materialVO.setMaterialType("materialType");
//        materialVO.setUnit("unit");
//        materialVO.setErrorProofingMethod("errorProofingMethod");
//        materialVO.setHasPallet(0);
//        materialVO.setBindPallet(0);
//        materialVO.setPackageWeight(new BigDecimal("0.00"));
//        materialVO.setMinPackageNetWeight(new BigDecimal("0.00"));
//        materialVO.setLessDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setMoreDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setTransferWeightRatio(new BigDecimal("0.00"));
//        final R<MaterialVO> materialVOR = R.ok(materialVO);
//        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//        when(mockMaterialRecevieMapper.selectSameBatchMaterialReceiveVO("materialNb", "batchNb"))
//                .thenReturn(Collections.emptyList());
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOList(...).
//        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
//        materialReceiveVO.setId(0L);
//        materialReceiveVO.setPlantNb("plantNb");
//        materialReceiveVO.setSsccNumber("ssccNumber");
//        materialReceiveVO.setMaterialNb("materialNb");
//        materialReceiveVO.setMaterialName("materialName");
//        materialReceiveVO.setBatchNb("batchNb");
//        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUnit("unit");
//        materialReceiveVO.setQuantity(0);
//        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO.setPoNumberItem("poNumberItem");
//        materialReceiveVO.setUploadUser("uploadUser");
//        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUpdateUser("updateUser");
//        materialReceiveVO.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
//                .thenReturn(materialReceiveVOS);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOBySncc(...).
//        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
//        materialReceiveVO1.setId(0L);
//        materialReceiveVO1.setPlantNb("plantNb");
//        materialReceiveVO1.setSsccNumber("ssccNumber");
//        materialReceiveVO1.setMaterialNb("materialNb");
//        materialReceiveVO1.setMaterialName("materialName");
//        materialReceiveVO1.setBatchNb("batchNb");
//        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUnit("unit");
//        materialReceiveVO1.setQuantity(0);
//        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO1.setPoNumberItem("poNumberItem");
//        materialReceiveVO1.setUploadUser("uploadUser");
//        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUpdateUser("updateUser");
//        materialReceiveVO1.setStatus(0);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOBySncc("ssccNumber")).thenReturn(materialReceiveVO1);
//
//        // Run the test
//        final MaterialInCheckVO result = materialInServiceImplUnderTest.getMaterialCheckInfo("mesBarCode");
//
//        // Verify the results
//        assertThat(result).isEqualTo(expectedResult);
//        verify(mockMaterialRecevieMapper).updateStatusBySscc("ssccNumber", 0);
//    }
//
//    @Test
//    void testGetMaterialCheckInfo_MaterialRecevieMapperSelectMaterialReceiveVOListReturnsNoItems() {
//        // Setup
//        final MaterialInCheckVO expectedResult = new MaterialInCheckVO();
//        expectedResult.setMesBarCode("mesBarCode");
//        expectedResult.setSsccNumber("ssccNumber");
//        expectedResult.setMaterialNb("materialNb");
//        expectedResult.setMaterialName("materialName");
//        expectedResult.setBatchNb("batchNb");
//        expectedResult.setCheckQuantity(0);
//        expectedResult.setMinStandard(0.0);
//        expectedResult.setMaxStandard(0.0);
//        expectedResult.setCheckType(0);
//        expectedResult.setCheckTypeDesc("desc");
//        expectedResult.setUnit("unit");
//        expectedResult.setFromPurchaseOrder("fromPurchaseOrder");
//        expectedResult.setPoNumberItem("poNumberItem");
//        expectedResult.setTotalPallet(0);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final MaterialVO materialVO = new MaterialVO();
//        materialVO.setId(0L);
//        materialVO.setCode("code");
//        materialVO.setName("materialName");
//        materialVO.setMinPackageNumber(0L);
//        materialVO.setMaterialTypeId(0L);
//        materialVO.setMaterialType("materialType");
//        materialVO.setUnit("unit");
//        materialVO.setErrorProofingMethod("errorProofingMethod");
//        materialVO.setHasPallet(0);
//        materialVO.setBindPallet(0);
//        materialVO.setPackageWeight(new BigDecimal("0.00"));
//        materialVO.setMinPackageNetWeight(new BigDecimal("0.00"));
//        materialVO.setLessDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setMoreDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setTransferWeightRatio(new BigDecimal("0.00"));
//        final R<MaterialVO> materialVOR = R.ok(materialVO);
//        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//        // Configure MaterialRecevieMapper.selectSameBatchMaterialReceiveVO(...).
//        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
//        materialReceiveVO.setId(0L);
//        materialReceiveVO.setPlantNb("plantNb");
//        materialReceiveVO.setSsccNumber("ssccNumber");
//        materialReceiveVO.setMaterialNb("materialNb");
//        materialReceiveVO.setMaterialName("materialName");
//        materialReceiveVO.setBatchNb("batchNb");
//        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUnit("unit");
//        materialReceiveVO.setQuantity(0);
//        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO.setPoNumberItem("poNumberItem");
//        materialReceiveVO.setUploadUser("uploadUser");
//        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUpdateUser("updateUser");
//        materialReceiveVO.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO);
//        when(mockMaterialRecevieMapper.selectSameBatchMaterialReceiveVO("materialNb", "batchNb"))
//                .thenReturn(materialReceiveVOS);
//
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
//                .thenReturn(Collections.emptyList());
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOBySncc(...).
//        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
//        materialReceiveVO1.setId(0L);
//        materialReceiveVO1.setPlantNb("plantNb");
//        materialReceiveVO1.setSsccNumber("ssccNumber");
//        materialReceiveVO1.setMaterialNb("materialNb");
//        materialReceiveVO1.setMaterialName("materialName");
//        materialReceiveVO1.setBatchNb("batchNb");
//        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUnit("unit");
//        materialReceiveVO1.setQuantity(0);
//        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO1.setPoNumberItem("poNumberItem");
//        materialReceiveVO1.setUploadUser("uploadUser");
//        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUpdateUser("updateUser");
//        materialReceiveVO1.setStatus(0);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOBySncc("ssccNumber")).thenReturn(materialReceiveVO1);
//
//        // Run the test
//        final MaterialInCheckVO result = materialInServiceImplUnderTest.getMaterialCheckInfo("mesBarCode");
//
//        // Verify the results
//        assertThat(result).isEqualTo(expectedResult);
//        verify(mockMaterialRecevieMapper).updateStatusBySscc("ssccNumber", 0);
//    }
//
//    @Test
//    void testCheck() {
//        // Setup
//        final MaterialInCheckDTO materialInCheckDTO = new MaterialInCheckDTO();
//        materialInCheckDTO.setMesBarCode("mesBarCode");
//        materialInCheckDTO.setActualQuantity(0);
//        materialInCheckDTO.setActualResult(0.0);
//        materialInCheckDTO.setOriginalPalletQuantity(0);
//
//        final MaterialCheckResultVO expectedResult = new MaterialCheckResultVO();
//        expectedResult.setSsccNumber("ssccNumber");
//        expectedResult.setMaterialNb("materialNb");
//        expectedResult.setMaterialName("materialName");
//        expectedResult.setBatchNb("batchNb");
//        expectedResult.setCheckQuantity(0);
//        expectedResult.setMinStandard(0.0);
//        expectedResult.setMaxStandard(0.0);
//        expectedResult.setCheckType(0);
//        expectedResult.setCheckTypeDesc("desc");
//        expectedResult.setUnit("unit");
//        expectedResult.setFromPurchaseOrder("fromPurchaseOrder");
//        expectedResult.setPoNumberItem("poNumberItem");
//        expectedResult.setTotalPallet(0);
//        expectedResult.setCheckFlag(false);
//        expectedResult.setActualQuantity(0);
//        expectedResult.setActualResult(0.0);
//        expectedResult.setAverageResult(0.0);
//        expectedResult.setOperateUser("operateUser");
//        expectedResult.setOperateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        expectedResult.setResponseCode(0);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final MaterialVO materialVO = new MaterialVO();
//        materialVO.setId(0L);
//        materialVO.setCode("code");
//        materialVO.setName("materialName");
//        materialVO.setMinPackageNumber(0L);
//        materialVO.setMaterialTypeId(0L);
//        materialVO.setMaterialType("materialType");
//        materialVO.setUnit("unit");
//        materialVO.setErrorProofingMethod("errorProofingMethod");
//        materialVO.setHasPallet(0);
//        materialVO.setBindPallet(0);
//        materialVO.setPackageWeight(new BigDecimal("0.00"));
//        materialVO.setMinPackageNetWeight(new BigDecimal("0.00"));
//        materialVO.setLessDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setMoreDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setTransferWeightRatio(new BigDecimal("0.00"));
//        final R<MaterialVO> materialVOR = R.ok(materialVO);
//        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//        // Configure MaterialRecevieMapper.selectSameBatchMaterialReceiveVO(...).
//        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
//        materialReceiveVO.setId(0L);
//        materialReceiveVO.setPlantNb("plantNb");
//        materialReceiveVO.setSsccNumber("ssccNumber");
//        materialReceiveVO.setMaterialNb("materialNb");
//        materialReceiveVO.setMaterialName("materialName");
//        materialReceiveVO.setBatchNb("batchNb");
//        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUnit("unit");
//        materialReceiveVO.setQuantity(0);
//        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO.setPoNumberItem("poNumberItem");
//        materialReceiveVO.setUploadUser("uploadUser");
//        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUpdateUser("updateUser");
//        materialReceiveVO.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO);
//        when(mockMaterialRecevieMapper.selectSameBatchMaterialReceiveVO("materialNb", "batchNb"))
//                .thenReturn(materialReceiveVOS);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOList(...).
//        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
//        materialReceiveVO1.setId(0L);
//        materialReceiveVO1.setPlantNb("plantNb");
//        materialReceiveVO1.setSsccNumber("ssccNumber");
//        materialReceiveVO1.setMaterialNb("materialNb");
//        materialReceiveVO1.setMaterialName("materialName");
//        materialReceiveVO1.setBatchNb("batchNb");
//        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUnit("unit");
//        materialReceiveVO1.setQuantity(0);
//        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO1.setPoNumberItem("poNumberItem");
//        materialReceiveVO1.setUploadUser("uploadUser");
//        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUpdateUser("updateUser");
//        materialReceiveVO1.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS1 = Arrays.asList(materialReceiveVO1);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
//                .thenReturn(materialReceiveVOS1);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOBySncc(...).
//        final MaterialReceiveVO materialReceiveVO2 = new MaterialReceiveVO();
//        materialReceiveVO2.setId(0L);
//        materialReceiveVO2.setPlantNb("plantNb");
//        materialReceiveVO2.setSsccNumber("ssccNumber");
//        materialReceiveVO2.setMaterialNb("materialNb");
//        materialReceiveVO2.setMaterialName("materialName");
//        materialReceiveVO2.setBatchNb("batchNb");
//        materialReceiveVO2.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUnit("unit");
//        materialReceiveVO2.setQuantity(0);
//        materialReceiveVO2.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO2.setPoNumberItem("poNumberItem");
//        materialReceiveVO2.setUploadUser("uploadUser");
//        materialReceiveVO2.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUpdateUser("updateUser");
//        materialReceiveVO2.setStatus(0);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOBySncc("ssccNumber")).thenReturn(materialReceiveVO2);
//
//        // Run the test
//        final MaterialCheckResultVO result = materialInServiceImplUnderTest.check(materialInCheckDTO);
//
//        // Verify the results
//        assertThat(result).isEqualTo(expectedResult);
//        verify(mockMaterialInMapper).batchInsert(Arrays.asList(new MaterialInDTO()));
////        verify(mockMaterialRecevieMapper).batchUpdateStatus("materialNb", "batchNb", 0);
//    }
//
//    @Test
//    void testCheck_RemoteMaterialServiceReturnsNoItem() {
//        // Setup
//        final MaterialInCheckDTO materialInCheckDTO = new MaterialInCheckDTO();
//        materialInCheckDTO.setMesBarCode("mesBarCode");
//        materialInCheckDTO.setActualQuantity(0);
//        materialInCheckDTO.setActualResult(0.0);
//        materialInCheckDTO.setOriginalPalletQuantity(0);
//
//        final MaterialCheckResultVO expectedResult = new MaterialCheckResultVO();
//        expectedResult.setSsccNumber("ssccNumber");
//        expectedResult.setMaterialNb("materialNb");
//        expectedResult.setMaterialName("materialName");
//        expectedResult.setBatchNb("batchNb");
//        expectedResult.setCheckQuantity(0);
//        expectedResult.setMinStandard(0.0);
//        expectedResult.setMaxStandard(0.0);
//        expectedResult.setCheckType(0);
//        expectedResult.setCheckTypeDesc("desc");
//        expectedResult.setUnit("unit");
//        expectedResult.setFromPurchaseOrder("fromPurchaseOrder");
//        expectedResult.setPoNumberItem("poNumberItem");
//        expectedResult.setTotalPallet(0);
//        expectedResult.setCheckFlag(false);
//        expectedResult.setActualQuantity(0);
//        expectedResult.setActualResult(0.0);
//        expectedResult.setAverageResult(0.0);
//        expectedResult.setOperateUser("operateUser");
//        expectedResult.setOperateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        expectedResult.setResponseCode(0);
//
//        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(R.ok());
//
//        // Configure MaterialRecevieMapper.selectSameBatchMaterialReceiveVO(...).
//        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
//        materialReceiveVO.setId(0L);
//        materialReceiveVO.setPlantNb("plantNb");
//        materialReceiveVO.setSsccNumber("ssccNumber");
//        materialReceiveVO.setMaterialNb("materialNb");
//        materialReceiveVO.setMaterialName("materialName");
//        materialReceiveVO.setBatchNb("batchNb");
//        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUnit("unit");
//        materialReceiveVO.setQuantity(0);
//        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO.setPoNumberItem("poNumberItem");
//        materialReceiveVO.setUploadUser("uploadUser");
//        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUpdateUser("updateUser");
//        materialReceiveVO.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO);
//        when(mockMaterialRecevieMapper.selectSameBatchMaterialReceiveVO("materialNb", "batchNb"))
//                .thenReturn(materialReceiveVOS);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOList(...).
//        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
//        materialReceiveVO1.setId(0L);
//        materialReceiveVO1.setPlantNb("plantNb");
//        materialReceiveVO1.setSsccNumber("ssccNumber");
//        materialReceiveVO1.setMaterialNb("materialNb");
//        materialReceiveVO1.setMaterialName("materialName");
//        materialReceiveVO1.setBatchNb("batchNb");
//        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUnit("unit");
//        materialReceiveVO1.setQuantity(0);
//        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO1.setPoNumberItem("poNumberItem");
//        materialReceiveVO1.setUploadUser("uploadUser");
//        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUpdateUser("updateUser");
//        materialReceiveVO1.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS1 = Arrays.asList(materialReceiveVO1);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
//                .thenReturn(materialReceiveVOS1);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOBySncc(...).
//        final MaterialReceiveVO materialReceiveVO2 = new MaterialReceiveVO();
//        materialReceiveVO2.setId(0L);
//        materialReceiveVO2.setPlantNb("plantNb");
//        materialReceiveVO2.setSsccNumber("ssccNumber");
//        materialReceiveVO2.setMaterialNb("materialNb");
//        materialReceiveVO2.setMaterialName("materialName");
//        materialReceiveVO2.setBatchNb("batchNb");
//        materialReceiveVO2.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUnit("unit");
//        materialReceiveVO2.setQuantity(0);
//        materialReceiveVO2.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO2.setPoNumberItem("poNumberItem");
//        materialReceiveVO2.setUploadUser("uploadUser");
//        materialReceiveVO2.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUpdateUser("updateUser");
//        materialReceiveVO2.setStatus(0);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOBySncc("ssccNumber")).thenReturn(materialReceiveVO2);
//
//        // Run the test
//        final MaterialCheckResultVO result = materialInServiceImplUnderTest.check(materialInCheckDTO);
//
//        // Verify the results
//        assertThat(result).isEqualTo(expectedResult);
//        verify(mockMaterialInMapper).batchInsert(Arrays.asList(new MaterialInDTO()));
////        verify(mockMaterialRecevieMapper).batchUpdateStatus("materialNb", "batchNb", 0);
//    }
//
//    @Test
//    void testCheck_RemoteMaterialServiceReturnsFailure() {
//        // Setup
//        final MaterialInCheckDTO materialInCheckDTO = new MaterialInCheckDTO();
//        materialInCheckDTO.setMesBarCode("mesBarCode");
//        materialInCheckDTO.setActualQuantity(0);
//        materialInCheckDTO.setActualResult(0.0);
//        materialInCheckDTO.setOriginalPalletQuantity(0);
//
//        final MaterialCheckResultVO expectedResult = new MaterialCheckResultVO();
//        expectedResult.setSsccNumber("ssccNumber");
//        expectedResult.setMaterialNb("materialNb");
//        expectedResult.setMaterialName("materialName");
//        expectedResult.setBatchNb("batchNb");
//        expectedResult.setCheckQuantity(0);
//        expectedResult.setMinStandard(0.0);
//        expectedResult.setMaxStandard(0.0);
//        expectedResult.setCheckType(0);
//        expectedResult.setCheckTypeDesc("desc");
//        expectedResult.setUnit("unit");
//        expectedResult.setFromPurchaseOrder("fromPurchaseOrder");
//        expectedResult.setPoNumberItem("poNumberItem");
//        expectedResult.setTotalPallet(0);
//        expectedResult.setCheckFlag(false);
//        expectedResult.setActualQuantity(0);
//        expectedResult.setActualResult(0.0);
//        expectedResult.setAverageResult(0.0);
//        expectedResult.setOperateUser("operateUser");
//        expectedResult.setOperateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        expectedResult.setResponseCode(0);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final R<MaterialVO> materialVOR = R.fail();
//        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//        // Configure MaterialRecevieMapper.selectSameBatchMaterialReceiveVO(...).
//        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
//        materialReceiveVO.setId(0L);
//        materialReceiveVO.setPlantNb("plantNb");
//        materialReceiveVO.setSsccNumber("ssccNumber");
//        materialReceiveVO.setMaterialNb("materialNb");
//        materialReceiveVO.setMaterialName("materialName");
//        materialReceiveVO.setBatchNb("batchNb");
//        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUnit("unit");
//        materialReceiveVO.setQuantity(0);
//        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO.setPoNumberItem("poNumberItem");
//        materialReceiveVO.setUploadUser("uploadUser");
//        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUpdateUser("updateUser");
//        materialReceiveVO.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO);
//        when(mockMaterialRecevieMapper.selectSameBatchMaterialReceiveVO("materialNb", "batchNb"))
//                .thenReturn(materialReceiveVOS);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOList(...).
//        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
//        materialReceiveVO1.setId(0L);
//        materialReceiveVO1.setPlantNb("plantNb");
//        materialReceiveVO1.setSsccNumber("ssccNumber");
//        materialReceiveVO1.setMaterialNb("materialNb");
//        materialReceiveVO1.setMaterialName("materialName");
//        materialReceiveVO1.setBatchNb("batchNb");
//        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUnit("unit");
//        materialReceiveVO1.setQuantity(0);
//        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO1.setPoNumberItem("poNumberItem");
//        materialReceiveVO1.setUploadUser("uploadUser");
//        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUpdateUser("updateUser");
//        materialReceiveVO1.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS1 = Arrays.asList(materialReceiveVO1);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
//                .thenReturn(materialReceiveVOS1);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOBySncc(...).
//        final MaterialReceiveVO materialReceiveVO2 = new MaterialReceiveVO();
//        materialReceiveVO2.setId(0L);
//        materialReceiveVO2.setPlantNb("plantNb");
//        materialReceiveVO2.setSsccNumber("ssccNumber");
//        materialReceiveVO2.setMaterialNb("materialNb");
//        materialReceiveVO2.setMaterialName("materialName");
//        materialReceiveVO2.setBatchNb("batchNb");
//        materialReceiveVO2.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUnit("unit");
//        materialReceiveVO2.setQuantity(0);
//        materialReceiveVO2.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO2.setPoNumberItem("poNumberItem");
//        materialReceiveVO2.setUploadUser("uploadUser");
//        materialReceiveVO2.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUpdateUser("updateUser");
//        materialReceiveVO2.setStatus(0);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOBySncc("ssccNumber")).thenReturn(materialReceiveVO2);
//
//        // Run the test
//        final MaterialCheckResultVO result = materialInServiceImplUnderTest.check(materialInCheckDTO);
//
//        // Verify the results
//        assertThat(result).isEqualTo(expectedResult);
//        verify(mockMaterialInMapper).batchInsert(Arrays.asList(new MaterialInDTO()));
////        verify(mockMaterialRecevieMapper).batchUpdateStatus("materialNb", "batchNb", 0);
//    }
//
//    @Test
//    void testCheck_MaterialRecevieMapperSelectSameBatchMaterialReceiveVOReturnsNoItems() {
//        // Setup
//        final MaterialInCheckDTO materialInCheckDTO = new MaterialInCheckDTO();
//        materialInCheckDTO.setMesBarCode("mesBarCode");
//        materialInCheckDTO.setActualQuantity(0);
//        materialInCheckDTO.setActualResult(0.0);
//        materialInCheckDTO.setOriginalPalletQuantity(0);
//
//        final MaterialCheckResultVO expectedResult = new MaterialCheckResultVO();
//        expectedResult.setSsccNumber("ssccNumber");
//        expectedResult.setMaterialNb("materialNb");
//        expectedResult.setMaterialName("materialName");
//        expectedResult.setBatchNb("batchNb");
//        expectedResult.setCheckQuantity(0);
//        expectedResult.setMinStandard(0.0);
//        expectedResult.setMaxStandard(0.0);
//        expectedResult.setCheckType(0);
//        expectedResult.setCheckTypeDesc("desc");
//        expectedResult.setUnit("unit");
//        expectedResult.setFromPurchaseOrder("fromPurchaseOrder");
//        expectedResult.setPoNumberItem("poNumberItem");
//        expectedResult.setTotalPallet(0);
//        expectedResult.setCheckFlag(false);
//        expectedResult.setActualQuantity(0);
//        expectedResult.setActualResult(0.0);
//        expectedResult.setAverageResult(0.0);
//        expectedResult.setOperateUser("operateUser");
//        expectedResult.setOperateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        expectedResult.setResponseCode(0);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final MaterialVO materialVO = new MaterialVO();
//        materialVO.setId(0L);
//        materialVO.setCode("code");
//        materialVO.setName("materialName");
//        materialVO.setMinPackageNumber(0L);
//        materialVO.setMaterialTypeId(0L);
//        materialVO.setMaterialType("materialType");
//        materialVO.setUnit("unit");
//        materialVO.setErrorProofingMethod("errorProofingMethod");
//        materialVO.setHasPallet(0);
//        materialVO.setBindPallet(0);
//        materialVO.setPackageWeight(new BigDecimal("0.00"));
//        materialVO.setMinPackageNetWeight(new BigDecimal("0.00"));
//        materialVO.setLessDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setMoreDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setTransferWeightRatio(new BigDecimal("0.00"));
//        final R<MaterialVO> materialVOR = R.ok(materialVO);
//        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//        when(mockMaterialRecevieMapper.selectSameBatchMaterialReceiveVO("materialNb", "batchNb"))
//                .thenReturn(Collections.emptyList());
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOList(...).
//        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
//        materialReceiveVO.setId(0L);
//        materialReceiveVO.setPlantNb("plantNb");
//        materialReceiveVO.setSsccNumber("ssccNumber");
//        materialReceiveVO.setMaterialNb("materialNb");
//        materialReceiveVO.setMaterialName("materialName");
//        materialReceiveVO.setBatchNb("batchNb");
//        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUnit("unit");
//        materialReceiveVO.setQuantity(0);
//        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO.setPoNumberItem("poNumberItem");
//        materialReceiveVO.setUploadUser("uploadUser");
//        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUpdateUser("updateUser");
//        materialReceiveVO.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
//                .thenReturn(materialReceiveVOS);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOBySncc(...).
//        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
//        materialReceiveVO1.setId(0L);
//        materialReceiveVO1.setPlantNb("plantNb");
//        materialReceiveVO1.setSsccNumber("ssccNumber");
//        materialReceiveVO1.setMaterialNb("materialNb");
//        materialReceiveVO1.setMaterialName("materialName");
//        materialReceiveVO1.setBatchNb("batchNb");
//        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUnit("unit");
//        materialReceiveVO1.setQuantity(0);
//        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO1.setPoNumberItem("poNumberItem");
//        materialReceiveVO1.setUploadUser("uploadUser");
//        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUpdateUser("updateUser");
//        materialReceiveVO1.setStatus(0);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOBySncc("ssccNumber")).thenReturn(materialReceiveVO1);
//
//        // Run the test
//        final MaterialCheckResultVO result = materialInServiceImplUnderTest.check(materialInCheckDTO);
//
//        // Verify the results
//        assertThat(result).isEqualTo(expectedResult);
//        verify(mockMaterialInMapper).batchInsert(Arrays.asList(new MaterialInDTO()));
////        verify(mockMaterialRecevieMapper).batchUpdateStatus("materialNb", "batchNb", 0);
//    }
//
//    @Test
//    void testCheck_MaterialRecevieMapperSelectMaterialReceiveVOListReturnsNoItems() {
//        // Setup
//        final MaterialInCheckDTO materialInCheckDTO = new MaterialInCheckDTO();
//        materialInCheckDTO.setMesBarCode("mesBarCode");
//        materialInCheckDTO.setActualQuantity(0);
//        materialInCheckDTO.setActualResult(0.0);
//        materialInCheckDTO.setOriginalPalletQuantity(0);
//
//        final MaterialCheckResultVO expectedResult = new MaterialCheckResultVO();
//        expectedResult.setSsccNumber("ssccNumber");
//        expectedResult.setMaterialNb("materialNb");
//        expectedResult.setMaterialName("materialName");
//        expectedResult.setBatchNb("batchNb");
//        expectedResult.setCheckQuantity(0);
//        expectedResult.setMinStandard(0.0);
//        expectedResult.setMaxStandard(0.0);
//        expectedResult.setCheckType(0);
//        expectedResult.setCheckTypeDesc("desc");
//        expectedResult.setUnit("unit");
//        expectedResult.setFromPurchaseOrder("fromPurchaseOrder");
//        expectedResult.setPoNumberItem("poNumberItem");
//        expectedResult.setTotalPallet(0);
//        expectedResult.setCheckFlag(false);
//        expectedResult.setActualQuantity(0);
//        expectedResult.setActualResult(0.0);
//        expectedResult.setAverageResult(0.0);
//        expectedResult.setOperateUser("operateUser");
//        expectedResult.setOperateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        expectedResult.setResponseCode(0);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final MaterialVO materialVO = new MaterialVO();
//        materialVO.setId(0L);
//        materialVO.setCode("code");
//        materialVO.setName("materialName");
//        materialVO.setMinPackageNumber(0L);
//        materialVO.setMaterialTypeId(0L);
//        materialVO.setMaterialType("materialType");
//        materialVO.setUnit("unit");
//        materialVO.setErrorProofingMethod("errorProofingMethod");
//        materialVO.setHasPallet(0);
//        materialVO.setBindPallet(0);
//        materialVO.setPackageWeight(new BigDecimal("0.00"));
//        materialVO.setMinPackageNetWeight(new BigDecimal("0.00"));
//        materialVO.setLessDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setMoreDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setTransferWeightRatio(new BigDecimal("0.00"));
//        final R<MaterialVO> materialVOR = R.ok(materialVO);
//        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//        // Configure MaterialRecevieMapper.selectSameBatchMaterialReceiveVO(...).
//        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
//        materialReceiveVO.setId(0L);
//        materialReceiveVO.setPlantNb("plantNb");
//        materialReceiveVO.setSsccNumber("ssccNumber");
//        materialReceiveVO.setMaterialNb("materialNb");
//        materialReceiveVO.setMaterialName("materialName");
//        materialReceiveVO.setBatchNb("batchNb");
//        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUnit("unit");
//        materialReceiveVO.setQuantity(0);
//        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO.setPoNumberItem("poNumberItem");
//        materialReceiveVO.setUploadUser("uploadUser");
//        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUpdateUser("updateUser");
//        materialReceiveVO.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO);
//        when(mockMaterialRecevieMapper.selectSameBatchMaterialReceiveVO("materialNb", "batchNb"))
//                .thenReturn(materialReceiveVOS);
//
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
//                .thenReturn(Collections.emptyList());
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOBySncc(...).
//        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
//        materialReceiveVO1.setId(0L);
//        materialReceiveVO1.setPlantNb("plantNb");
//        materialReceiveVO1.setSsccNumber("ssccNumber");
//        materialReceiveVO1.setMaterialNb("materialNb");
//        materialReceiveVO1.setMaterialName("materialName");
//        materialReceiveVO1.setBatchNb("batchNb");
//        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUnit("unit");
//        materialReceiveVO1.setQuantity(0);
//        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO1.setPoNumberItem("poNumberItem");
//        materialReceiveVO1.setUploadUser("uploadUser");
//        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUpdateUser("updateUser");
//        materialReceiveVO1.setStatus(0);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOBySncc("ssccNumber")).thenReturn(materialReceiveVO1);
//
//        // Run the test
//        final MaterialCheckResultVO result = materialInServiceImplUnderTest.check(materialInCheckDTO);
//
//        // Verify the results
//        assertThat(result).isEqualTo(expectedResult);
//        verify(mockMaterialInMapper).batchInsert(Arrays.asList(new MaterialInDTO()));
////        verify(mockMaterialRecevieMapper).batchUpdateStatus("materialNb", "batchNb", 0);
//    }
//
//    @Test
//    void testSelectById() {
//        // Setup
//        final MaterialInVO expectedResult = new MaterialInVO();
//        expectedResult.setId(0L);
//        expectedResult.setSsccNumber("ssccNumber");
//        expectedResult.setBatchNb("batchNb");
//        expectedResult.setCheckType(0);
//        expectedResult.setMaterialNb("materialNb");
//        expectedResult.setMaterialName("materialName");
//        expectedResult.setCheckQuantity(0);
//        expectedResult.setMinStandard(0);
//        expectedResult.setMaxStandard(0);
//        expectedResult.setActualQuantity(0);
//        expectedResult.setActualResult(0.0);
//        expectedResult.setAverageResult(0.0);
//        expectedResult.setOperateUser("operateUser");
//        expectedResult.setOperateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        expectedResult.setOriginalPalletQuantity(0);
//
//        // Configure MaterialInMapper.selectById(...).
//        final MaterialInVO materialInVO = new MaterialInVO();
//        materialInVO.setId(0L);
//        materialInVO.setSsccNumber("ssccNumber");
//        materialInVO.setBatchNb("batchNb");
//        materialInVO.setCheckType(0);
//        materialInVO.setMaterialNb("materialNb");
//        materialInVO.setMaterialName("materialName");
//        materialInVO.setCheckQuantity(0);
//        materialInVO.setMinStandard(0);
//        materialInVO.setMaxStandard(0);
//        materialInVO.setActualQuantity(0);
//        materialInVO.setActualResult(0.0);
//        materialInVO.setAverageResult(0.0);
//        materialInVO.setOperateUser("operateUser");
//        materialInVO.setOperateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialInVO.setOriginalPalletQuantity(0);
//        when(mockMaterialInMapper.selectById(0L)).thenReturn(materialInVO);
//
//        // Run the test
//        final MaterialInVO result = materialInServiceImplUnderTest.selectById(0L);
//
//        // Verify the results
//        assertThat(result).isEqualTo(expectedResult);
//    }
//
//
//    @Test
//    void testGetByUserName() {
//        // Setup
//        final MaterialInVO materialInVO = new MaterialInVO();
//        materialInVO.setId(0L);
//        materialInVO.setSsccNumber("ssccNumber");
//        materialInVO.setBatchNb("batchNb");
//        materialInVO.setCheckType(0);
//        materialInVO.setMaterialNb("materialNb");
//        materialInVO.setMaterialName("materialName");
//        materialInVO.setCheckQuantity(0);
//        materialInVO.setMinStandard(0);
//        materialInVO.setMaxStandard(0);
//        materialInVO.setActualQuantity(0);
//        materialInVO.setActualResult(0.0);
//        materialInVO.setAverageResult(0.0);
//        materialInVO.setOperateUser("operateUser");
//        materialInVO.setOperateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialInVO.setOriginalPalletQuantity(0);
//        final List<MaterialInVO> expectedResult = Arrays.asList(materialInVO);
//
//        // Configure MaterialInMapper.getByUserName(...).
//        final MaterialInVO materialInVO1 = new MaterialInVO();
//        materialInVO1.setId(0L);
//        materialInVO1.setSsccNumber("ssccNumber");
//        materialInVO1.setBatchNb("batchNb");
//        materialInVO1.setCheckType(0);
//        materialInVO1.setMaterialNb("materialNb");
//        materialInVO1.setMaterialName("materialName");
//        materialInVO1.setCheckQuantity(0);
//        materialInVO1.setMinStandard(0);
//        materialInVO1.setMaxStandard(0);
//        materialInVO1.setActualQuantity(0);
//        materialInVO1.setActualResult(0.0);
//        materialInVO1.setAverageResult(0.0);
//        materialInVO1.setOperateUser("operateUser");
//        materialInVO1.setOperateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialInVO1.setOriginalPalletQuantity(0);
//        final List<MaterialInVO> materialInVOS = Arrays.asList(materialInVO1);
//        when(mockMaterialInMapper.getByUserName("username")).thenReturn(materialInVOS);
//
//        // Run the test
//        final List<MaterialInVO> result = materialInServiceImplUnderTest.getByUserName("username");
//
//        // Verify the results
//        assertThat(result).isEqualTo(expectedResult);
//    }
//
//    @Test
//    void testGetByUserName_MaterialInMapperReturnsNoItems() {
//        // Setup
//        when(mockMaterialInMapper.getByUserName("username")).thenReturn(Collections.emptyList());
//
//        // Run the test
//        final List<MaterialInVO> result = materialInServiceImplUnderTest.getByUserName("username");
//
//        // Verify the results
//        assertThat(result).isEqualTo(Collections.emptyList());
//    }
//
//
//    @Test
//    void testCheckSampleQuantity() {
//        // Setup
//        final MaterialInCheckDTO materialInCheckDTO = new MaterialInCheckDTO();
//        materialInCheckDTO.setMesBarCode("mesBarCode");
//        materialInCheckDTO.setActualQuantity(0);
//        materialInCheckDTO.setActualResult(0.0);
//        materialInCheckDTO.setOriginalPalletQuantity(0);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final MaterialVO materialVO = new MaterialVO();
//        materialVO.setId(0L);
//        materialVO.setCode("code");
//        materialVO.setName("materialName");
//        materialVO.setMinPackageNumber(0L);
//        materialVO.setMaterialTypeId(0L);
//        materialVO.setMaterialType("materialType");
//        materialVO.setUnit("unit");
//        materialVO.setErrorProofingMethod("errorProofingMethod");
//        materialVO.setHasPallet(0);
//        materialVO.setBindPallet(0);
//        materialVO.setPackageWeight(new BigDecimal("0.00"));
//        materialVO.setMinPackageNetWeight(new BigDecimal("0.00"));
//        materialVO.setLessDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setMoreDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setTransferWeightRatio(new BigDecimal("0.00"));
//        final R<MaterialVO> materialVOR = R.ok(materialVO);
//        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//        // Configure MaterialRecevieMapper.selectSameBatchMaterialReceiveVO(...).
//        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
//        materialReceiveVO.setId(0L);
//        materialReceiveVO.setPlantNb("plantNb");
//        materialReceiveVO.setSsccNumber("ssccNumber");
//        materialReceiveVO.setMaterialNb("materialNb");
//        materialReceiveVO.setMaterialName("materialName");
//        materialReceiveVO.setBatchNb("batchNb");
//        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUnit("unit");
//        materialReceiveVO.setQuantity(0);
//        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO.setPoNumberItem("poNumberItem");
//        materialReceiveVO.setUploadUser("uploadUser");
//        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUpdateUser("updateUser");
//        materialReceiveVO.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO);
//        when(mockMaterialRecevieMapper.selectSameBatchMaterialReceiveVO("materialNb", "batchNb"))
//                .thenReturn(materialReceiveVOS);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOList(...).
//        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
//        materialReceiveVO1.setId(0L);
//        materialReceiveVO1.setPlantNb("plantNb");
//        materialReceiveVO1.setSsccNumber("ssccNumber");
//        materialReceiveVO1.setMaterialNb("materialNb");
//        materialReceiveVO1.setMaterialName("materialName");
//        materialReceiveVO1.setBatchNb("batchNb");
//        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUnit("unit");
//        materialReceiveVO1.setQuantity(0);
//        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO1.setPoNumberItem("poNumberItem");
//        materialReceiveVO1.setUploadUser("uploadUser");
//        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUpdateUser("updateUser");
//        materialReceiveVO1.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS1 = Arrays.asList(materialReceiveVO1);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
//                .thenReturn(materialReceiveVOS1);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOBySncc(...).
//        final MaterialReceiveVO materialReceiveVO2 = new MaterialReceiveVO();
//        materialReceiveVO2.setId(0L);
//        materialReceiveVO2.setPlantNb("plantNb");
//        materialReceiveVO2.setSsccNumber("ssccNumber");
//        materialReceiveVO2.setMaterialNb("materialNb");
//        materialReceiveVO2.setMaterialName("materialName");
//        materialReceiveVO2.setBatchNb("batchNb");
//        materialReceiveVO2.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUnit("unit");
//        materialReceiveVO2.setQuantity(0);
//        materialReceiveVO2.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO2.setPoNumberItem("poNumberItem");
//        materialReceiveVO2.setUploadUser("uploadUser");
//        materialReceiveVO2.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUpdateUser("updateUser");
//        materialReceiveVO2.setStatus(0);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOBySncc("ssccNumber")).thenReturn(materialReceiveVO2);
//
//        // Run the test
//        final boolean result = materialInServiceImplUnderTest.checkSampleQuantity(materialInCheckDTO);
//
//        // Verify the results
//        assertThat(result).isFalse();
//    }
//
//    @Test
//    void testCheckSampleQuantity_RemoteMaterialServiceReturnsNoItem() {
//        // Setup
//        final MaterialInCheckDTO materialInCheckDTO = new MaterialInCheckDTO();
//        materialInCheckDTO.setMesBarCode("mesBarCode");
//        materialInCheckDTO.setActualQuantity(0);
//        materialInCheckDTO.setActualResult(0.0);
//        materialInCheckDTO.setOriginalPalletQuantity(0);
//
//        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(R.ok());
//
//        // Configure MaterialRecevieMapper.selectSameBatchMaterialReceiveVO(...).
//        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
//        materialReceiveVO.setId(0L);
//        materialReceiveVO.setPlantNb("plantNb");
//        materialReceiveVO.setSsccNumber("ssccNumber");
//        materialReceiveVO.setMaterialNb("materialNb");
//        materialReceiveVO.setMaterialName("materialName");
//        materialReceiveVO.setBatchNb("batchNb");
//        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUnit("unit");
//        materialReceiveVO.setQuantity(0);
//        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO.setPoNumberItem("poNumberItem");
//        materialReceiveVO.setUploadUser("uploadUser");
//        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUpdateUser("updateUser");
//        materialReceiveVO.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO);
//        when(mockMaterialRecevieMapper.selectSameBatchMaterialReceiveVO("materialNb", "batchNb"))
//                .thenReturn(materialReceiveVOS);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOList(...).
//        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
//        materialReceiveVO1.setId(0L);
//        materialReceiveVO1.setPlantNb("plantNb");
//        materialReceiveVO1.setSsccNumber("ssccNumber");
//        materialReceiveVO1.setMaterialNb("materialNb");
//        materialReceiveVO1.setMaterialName("materialName");
//        materialReceiveVO1.setBatchNb("batchNb");
//        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUnit("unit");
//        materialReceiveVO1.setQuantity(0);
//        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO1.setPoNumberItem("poNumberItem");
//        materialReceiveVO1.setUploadUser("uploadUser");
//        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUpdateUser("updateUser");
//        materialReceiveVO1.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS1 = Arrays.asList(materialReceiveVO1);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
//                .thenReturn(materialReceiveVOS1);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOBySncc(...).
//        final MaterialReceiveVO materialReceiveVO2 = new MaterialReceiveVO();
//        materialReceiveVO2.setId(0L);
//        materialReceiveVO2.setPlantNb("plantNb");
//        materialReceiveVO2.setSsccNumber("ssccNumber");
//        materialReceiveVO2.setMaterialNb("materialNb");
//        materialReceiveVO2.setMaterialName("materialName");
//        materialReceiveVO2.setBatchNb("batchNb");
//        materialReceiveVO2.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUnit("unit");
//        materialReceiveVO2.setQuantity(0);
//        materialReceiveVO2.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO2.setPoNumberItem("poNumberItem");
//        materialReceiveVO2.setUploadUser("uploadUser");
//        materialReceiveVO2.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUpdateUser("updateUser");
//        materialReceiveVO2.setStatus(0);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOBySncc("ssccNumber")).thenReturn(materialReceiveVO2);
//
//        // Run the test
//        final boolean result = materialInServiceImplUnderTest.checkSampleQuantity(materialInCheckDTO);
//
//        // Verify the results
//        assertThat(result).isFalse();
//    }
//
//    @Test
//    void testCheckSampleQuantity_RemoteMaterialServiceReturnsFailure() {
//        // Setup
//        final MaterialInCheckDTO materialInCheckDTO = new MaterialInCheckDTO();
//        materialInCheckDTO.setMesBarCode("mesBarCode");
//        materialInCheckDTO.setActualQuantity(0);
//        materialInCheckDTO.setActualResult(0.0);
//        materialInCheckDTO.setOriginalPalletQuantity(0);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final R<MaterialVO> materialVOR = R.fail();
//        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//        // Configure MaterialRecevieMapper.selectSameBatchMaterialReceiveVO(...).
//        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
//        materialReceiveVO.setId(0L);
//        materialReceiveVO.setPlantNb("plantNb");
//        materialReceiveVO.setSsccNumber("ssccNumber");
//        materialReceiveVO.setMaterialNb("materialNb");
//        materialReceiveVO.setMaterialName("materialName");
//        materialReceiveVO.setBatchNb("batchNb");
//        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUnit("unit");
//        materialReceiveVO.setQuantity(0);
//        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO.setPoNumberItem("poNumberItem");
//        materialReceiveVO.setUploadUser("uploadUser");
//        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUpdateUser("updateUser");
//        materialReceiveVO.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO);
//        when(mockMaterialRecevieMapper.selectSameBatchMaterialReceiveVO("materialNb", "batchNb"))
//                .thenReturn(materialReceiveVOS);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOList(...).
//        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
//        materialReceiveVO1.setId(0L);
//        materialReceiveVO1.setPlantNb("plantNb");
//        materialReceiveVO1.setSsccNumber("ssccNumber");
//        materialReceiveVO1.setMaterialNb("materialNb");
//        materialReceiveVO1.setMaterialName("materialName");
//        materialReceiveVO1.setBatchNb("batchNb");
//        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUnit("unit");
//        materialReceiveVO1.setQuantity(0);
//        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO1.setPoNumberItem("poNumberItem");
//        materialReceiveVO1.setUploadUser("uploadUser");
//        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUpdateUser("updateUser");
//        materialReceiveVO1.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS1 = Arrays.asList(materialReceiveVO1);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
//                .thenReturn(materialReceiveVOS1);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOBySncc(...).
//        final MaterialReceiveVO materialReceiveVO2 = new MaterialReceiveVO();
//        materialReceiveVO2.setId(0L);
//        materialReceiveVO2.setPlantNb("plantNb");
//        materialReceiveVO2.setSsccNumber("ssccNumber");
//        materialReceiveVO2.setMaterialNb("materialNb");
//        materialReceiveVO2.setMaterialName("materialName");
//        materialReceiveVO2.setBatchNb("batchNb");
//        materialReceiveVO2.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUnit("unit");
//        materialReceiveVO2.setQuantity(0);
//        materialReceiveVO2.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO2.setPoNumberItem("poNumberItem");
//        materialReceiveVO2.setUploadUser("uploadUser");
//        materialReceiveVO2.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO2.setUpdateUser("updateUser");
//        materialReceiveVO2.setStatus(0);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOBySncc("ssccNumber")).thenReturn(materialReceiveVO2);
//
//        // Run the test
//        final boolean result = materialInServiceImplUnderTest.checkSampleQuantity(materialInCheckDTO);
//
//        // Verify the results
//        assertThat(result).isFalse();
//    }
//
//    @Test
//    void testCheckSampleQuantity_MaterialRecevieMapperSelectSameBatchMaterialReceiveVOReturnsNoItems() {
//        // Setup
//        final MaterialInCheckDTO materialInCheckDTO = new MaterialInCheckDTO();
//        materialInCheckDTO.setMesBarCode("mesBarCode");
//        materialInCheckDTO.setActualQuantity(0);
//        materialInCheckDTO.setActualResult(0.0);
//        materialInCheckDTO.setOriginalPalletQuantity(0);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final MaterialVO materialVO = new MaterialVO();
//        materialVO.setId(0L);
//        materialVO.setCode("code");
//        materialVO.setName("materialName");
//        materialVO.setMinPackageNumber(0L);
//        materialVO.setMaterialTypeId(0L);
//        materialVO.setMaterialType("materialType");
//        materialVO.setUnit("unit");
//        materialVO.setErrorProofingMethod("errorProofingMethod");
//        materialVO.setHasPallet(0);
//        materialVO.setBindPallet(0);
//        materialVO.setPackageWeight(new BigDecimal("0.00"));
//        materialVO.setMinPackageNetWeight(new BigDecimal("0.00"));
//        materialVO.setLessDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setMoreDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setTransferWeightRatio(new BigDecimal("0.00"));
//        final R<MaterialVO> materialVOR = R.ok(materialVO);
//        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//        when(mockMaterialRecevieMapper.selectSameBatchMaterialReceiveVO("materialNb", "batchNb"))
//                .thenReturn(Collections.emptyList());
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOList(...).
//        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
//        materialReceiveVO.setId(0L);
//        materialReceiveVO.setPlantNb("plantNb");
//        materialReceiveVO.setSsccNumber("ssccNumber");
//        materialReceiveVO.setMaterialNb("materialNb");
//        materialReceiveVO.setMaterialName("materialName");
//        materialReceiveVO.setBatchNb("batchNb");
//        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUnit("unit");
//        materialReceiveVO.setQuantity(0);
//        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO.setPoNumberItem("poNumberItem");
//        materialReceiveVO.setUploadUser("uploadUser");
//        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUpdateUser("updateUser");
//        materialReceiveVO.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
//                .thenReturn(materialReceiveVOS);
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOBySncc(...).
//        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
//        materialReceiveVO1.setId(0L);
//        materialReceiveVO1.setPlantNb("plantNb");
//        materialReceiveVO1.setSsccNumber("ssccNumber");
//        materialReceiveVO1.setMaterialNb("materialNb");
//        materialReceiveVO1.setMaterialName("materialName");
//        materialReceiveVO1.setBatchNb("batchNb");
//        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUnit("unit");
//        materialReceiveVO1.setQuantity(0);
//        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO1.setPoNumberItem("poNumberItem");
//        materialReceiveVO1.setUploadUser("uploadUser");
//        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUpdateUser("updateUser");
//        materialReceiveVO1.setStatus(0);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOBySncc("ssccNumber")).thenReturn(materialReceiveVO1);
//
//        // Run the test
//        final boolean result = materialInServiceImplUnderTest.checkSampleQuantity(materialInCheckDTO);
//
//        // Verify the results
//        assertThat(result).isFalse();
//    }
//
//    @Test
//    void testCheckSampleQuantity_MaterialRecevieMapperSelectMaterialReceiveVOListReturnsNoItems() {
//        // Setup
//        final MaterialInCheckDTO materialInCheckDTO = new MaterialInCheckDTO();
//        materialInCheckDTO.setMesBarCode("mesBarCode");
//        materialInCheckDTO.setActualQuantity(0);
//        materialInCheckDTO.setActualResult(0.0);
//        materialInCheckDTO.setOriginalPalletQuantity(0);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final MaterialVO materialVO = new MaterialVO();
//        materialVO.setId(0L);
//        materialVO.setCode("code");
//        materialVO.setName("materialName");
//        materialVO.setMinPackageNumber(0L);
//        materialVO.setMaterialTypeId(0L);
//        materialVO.setMaterialType("materialType");
//        materialVO.setUnit("unit");
//        materialVO.setErrorProofingMethod("errorProofingMethod");
//        materialVO.setHasPallet(0);
//        materialVO.setBindPallet(0);
//        materialVO.setPackageWeight(new BigDecimal("0.00"));
//        materialVO.setMinPackageNetWeight(new BigDecimal("0.00"));
//        materialVO.setLessDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setMoreDeviationRatio(new BigDecimal("0.00"));
//        materialVO.setTransferWeightRatio(new BigDecimal("0.00"));
//        final R<MaterialVO> materialVOR = R.ok(materialVO);
//        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//        // Configure MaterialRecevieMapper.selectSameBatchMaterialReceiveVO(...).
//        final MaterialReceiveVO materialReceiveVO = new MaterialReceiveVO();
//        materialReceiveVO.setId(0L);
//        materialReceiveVO.setPlantNb("plantNb");
//        materialReceiveVO.setSsccNumber("ssccNumber");
//        materialReceiveVO.setMaterialNb("materialNb");
//        materialReceiveVO.setMaterialName("materialName");
//        materialReceiveVO.setBatchNb("batchNb");
//        materialReceiveVO.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUnit("unit");
//        materialReceiveVO.setQuantity(0);
//        materialReceiveVO.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO.setPoNumberItem("poNumberItem");
//        materialReceiveVO.setUploadUser("uploadUser");
//        materialReceiveVO.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO.setUpdateUser("updateUser");
//        materialReceiveVO.setStatus(0);
//        final List<MaterialReceiveVO> materialReceiveVOS = Arrays.asList(materialReceiveVO);
//        when(mockMaterialRecevieMapper.selectSameBatchMaterialReceiveVO("materialNb", "batchNb"))
//                .thenReturn(materialReceiveVOS);
//
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOList(new MaterialReceiveDTO()))
//                .thenReturn(Collections.emptyList());
//
//        // Configure MaterialRecevieMapper.selectMaterialReceiveVOBySncc(...).
//        final MaterialReceiveVO materialReceiveVO1 = new MaterialReceiveVO();
//        materialReceiveVO1.setId(0L);
//        materialReceiveVO1.setPlantNb("plantNb");
//        materialReceiveVO1.setSsccNumber("ssccNumber");
//        materialReceiveVO1.setMaterialNb("materialNb");
//        materialReceiveVO1.setMaterialName("materialName");
//        materialReceiveVO1.setBatchNb("batchNb");
//        materialReceiveVO1.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUnit("unit");
//        materialReceiveVO1.setQuantity(0);
//        materialReceiveVO1.setFromPurchaseOrder("fromPurchaseOrder");
//        materialReceiveVO1.setPoNumberItem("poNumberItem");
//        materialReceiveVO1.setUploadUser("uploadUser");
//        materialReceiveVO1.setUploadTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
//        materialReceiveVO1.setUpdateUser("updateUser");
//        materialReceiveVO1.setStatus(0);
//        when(mockMaterialRecevieMapper.selectMaterialReceiveVOBySncc("ssccNumber")).thenReturn(materialReceiveVO1);
//
//        // Run the test
//        final boolean result = materialInServiceImplUnderTest.checkSampleQuantity(materialInCheckDTO);
//
//        // Verify the results
//        assertThat(result).isFalse();
//    }
//}
