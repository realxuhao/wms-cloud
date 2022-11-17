//
//
//package com.bosch.binin.service.impl;
//
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.spy;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.assertj.core.api.Assertions.within;
//    import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.InjectMocks;
//
//@ExtendWith(MockitoExtension.class)
//class IBinAssignmentServiceImplTest {
//
//            @Mock
//        private com.bosch.binin.mapper.BinInMapper mockBinInMapper;
//            @Mock
//        private com.bosch.binin.mapper.StockMapper mockStockMapper;
//            @Mock
//        private com.bosch.masterdata.api.RemotePalletService mockRemotePalletService;
//            @Mock
//        private com.bosch.storagein.api.RemoteMaterialInService mockRemoteMaterialInService;
//            @Mock
//        private com.bosch.masterdata.api.RemoteMaterialService mockRemoteMaterialService;
//            @Mock
//        private com.bosch.masterdata.api.RemoteMasterDataService mockRemoteMasterDataService;
//
// @InjectMocks     private com.bosch.binin.service.impl.BinAssignmentServiceImpl iBinAssignmentServiceImplUnderTest;
//
//    @Test
//    void testGetBinAllocationVO() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//            // Configure RemoteMasterDataService.getFrameInfoByType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(frameVO));
//            when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR1);
//
//            // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.BinVO binVO = new com.bosch.masterdata.api.domain.vo.BinVO();
//                binVO.setId(0L);
//                binVO.setFrameId(0L);
//                binVO.setCode("code");
//                binVO.setName("name");
//                binVO.setWareId(0L);
//                binVO.setWareCode("wareCode");
//                binVO.setWareName("wareName");
//                binVO.setAreaId(0L);
//                binVO.setAreaName("areaName");
//                binVO.setAreaCode("areaCode");
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.BinVO>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(binVO));
//            when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn(listR2);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO1 = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO1.setId(0L);
//                frameVO1.setWareId(0L);
//                frameVO1.setWareCode("wareCode");
//                frameVO1.setWareName("wareName");
//                frameVO1.setAreaId(0L);
//                frameVO1.setAreaName("areaName");
//                frameVO1.setAreaCode("areaCode");
//                frameVO1.setCode("code");
//                frameVO1.setName("name");
//                frameVO1.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO1);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR3 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR3);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_BinInMapperReturnsNoItems() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//         when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn( java.util.Collections.emptyList() );
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//            // Configure RemoteMasterDataService.getFrameInfoByType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(frameVO));
//            when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR1);
//
//            // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.BinVO binVO = new com.bosch.masterdata.api.domain.vo.BinVO();
//                binVO.setId(0L);
//                binVO.setFrameId(0L);
//                binVO.setCode("code");
//                binVO.setName("name");
//                binVO.setWareId(0L);
//                binVO.setWareCode("wareCode");
//                binVO.setWareName("wareName");
//                binVO.setAreaId(0L);
//                binVO.setAreaName("areaName");
//                binVO.setAreaCode("areaCode");
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.BinVO>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(binVO));
//            when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn(listR2);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO1 = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO1.setId(0L);
//                frameVO1.setWareId(0L);
//                frameVO1.setWareCode("wareCode");
//                frameVO1.setWareName("wareName");
//                frameVO1.setAreaId(0L);
//                frameVO1.setAreaName("areaName");
//                frameVO1.setAreaCode("areaCode");
//                frameVO1.setCode("code");
//                frameVO1.setName("name");
//                frameVO1.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO1);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR3 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR3);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemotePalletServiceReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//         when( mockRemotePalletService .getByType("palletType")).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//            // Configure RemoteMasterDataService.getFrameInfoByType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(frameVO));
//            when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR1);
//
//            // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.BinVO binVO = new com.bosch.masterdata.api.domain.vo.BinVO();
//                binVO.setId(0L);
//                binVO.setFrameId(0L);
//                binVO.setCode("code");
//                binVO.setName("name");
//                binVO.setWareId(0L);
//                binVO.setWareCode("wareCode");
//                binVO.setWareName("wareName");
//                binVO.setAreaId(0L);
//                binVO.setAreaName("areaName");
//                binVO.setAreaCode("areaCode");
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.BinVO>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(binVO));
//            when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn(listR2);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO1 = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO1.setId(0L);
//                frameVO1.setWareId(0L);
//                frameVO1.setWareCode("wareCode");
//                frameVO1.setWareName("wareName");
//                frameVO1.setAreaId(0L);
//                frameVO1.setAreaName("areaName");
//                frameVO1.setAreaCode("areaCode");
//                frameVO1.setCode("code");
//                frameVO1.setName("name");
//                frameVO1.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO1);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR3 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR3);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemotePalletServiceReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//        // Configure RemotePalletService.getByType(...).
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//            // Configure RemoteMasterDataService.getFrameInfoByType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(frameVO));
//            when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR1);
//
//            // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.BinVO binVO = new com.bosch.masterdata.api.domain.vo.BinVO();
//                binVO.setId(0L);
//                binVO.setFrameId(0L);
//                binVO.setCode("code");
//                binVO.setName("name");
//                binVO.setWareId(0L);
//                binVO.setWareCode("wareCode");
//                binVO.setWareName("wareName");
//                binVO.setAreaId(0L);
//                binVO.setAreaName("areaName");
//                binVO.setAreaCode("areaCode");
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.BinVO>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(binVO));
//            when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn(listR2);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO1 = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO1.setId(0L);
//                frameVO1.setWareId(0L);
//                frameVO1.setWareCode("wareCode");
//                frameVO1.setWareName("wareName");
//                frameVO1.setAreaId(0L);
//                frameVO1.setAreaName("areaName");
//                frameVO1.setAreaCode("areaCode");
//                frameVO1.setCode("code");
//                frameVO1.setName("name");
//                frameVO1.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO1);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR3 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR3);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemoteMaterialServiceReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//         when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//            // Configure RemoteMasterDataService.getFrameInfoByType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(frameVO));
//            when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR1);
//
//            // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.BinVO binVO = new com.bosch.masterdata.api.domain.vo.BinVO();
//                binVO.setId(0L);
//                binVO.setFrameId(0L);
//                binVO.setCode("code");
//                binVO.setName("name");
//                binVO.setWareId(0L);
//                binVO.setWareCode("wareCode");
//                binVO.setWareName("wareName");
//                binVO.setAreaId(0L);
//                binVO.setAreaName("areaName");
//                binVO.setAreaCode("areaCode");
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.BinVO>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(binVO));
//            when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn(listR2);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO1 = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO1.setId(0L);
//                frameVO1.setWareId(0L);
//                frameVO1.setWareCode("wareCode");
//                frameVO1.setWareName("wareName");
//                frameVO1.setAreaId(0L);
//                frameVO1.setAreaName("areaName");
//                frameVO1.setAreaCode("areaCode");
//                frameVO1.setCode("code");
//                frameVO1.setName("name");
//                frameVO1.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO1);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR3 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR3);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemoteMaterialServiceReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//            // Configure RemoteMasterDataService.getFrameInfoByType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(frameVO));
//            when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR1);
//
//            // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.BinVO binVO = new com.bosch.masterdata.api.domain.vo.BinVO();
//                binVO.setId(0L);
//                binVO.setFrameId(0L);
//                binVO.setCode("code");
//                binVO.setName("name");
//                binVO.setWareId(0L);
//                binVO.setWareCode("wareCode");
//                binVO.setWareName("wareName");
//                binVO.setAreaId(0L);
//                binVO.setAreaName("areaName");
//                binVO.setAreaCode("areaCode");
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.BinVO>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(binVO));
//            when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn(listR2);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO1 = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO1.setId(0L);
//                frameVO1.setWareId(0L);
//                frameVO1.setWareCode("wareCode");
//                frameVO1.setWareName("wareName");
//                frameVO1.setAreaId(0L);
//                frameVO1.setAreaName("areaName");
//                frameVO1.setAreaCode("areaCode");
//                frameVO1.setCode("code");
//                frameVO1.setName("name");
//                frameVO1.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO1);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR3 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR3);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemoteMasterDataServiceGetListByMaterialReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//         when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//            // Configure RemoteMasterDataService.getFrameInfoByType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(frameVO));
//            when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR);
//
//            // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.BinVO binVO = new com.bosch.masterdata.api.domain.vo.BinVO();
//                binVO.setId(0L);
//                binVO.setFrameId(0L);
//                binVO.setCode("code");
//                binVO.setName("name");
//                binVO.setWareId(0L);
//                binVO.setWareCode("wareCode");
//                binVO.setWareName("wareName");
//                binVO.setAreaId(0L);
//                binVO.setAreaName("areaName");
//                binVO.setAreaCode("areaCode");
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.BinVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(binVO));
//            when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn(listR1);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO1 = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO1.setId(0L);
//                frameVO1.setWareId(0L);
//                frameVO1.setWareCode("wareCode");
//                frameVO1.setWareName("wareName");
//                frameVO1.setAreaId(0L);
//                frameVO1.setAreaName("areaName");
//                frameVO1.setAreaCode("areaCode");
//                frameVO1.setCode("code");
//                frameVO1.setName("name");
//                frameVO1.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO1);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR2);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemoteMasterDataServiceGetListByMaterialReturnsNoItems() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//         when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn( com.ruoyi.common.core.domain.R.ok(java.util.Collections.emptyList()) );
//
//            // Configure RemoteMasterDataService.getFrameInfoByType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(frameVO));
//            when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR);
//
//            // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.BinVO binVO = new com.bosch.masterdata.api.domain.vo.BinVO();
//                binVO.setId(0L);
//                binVO.setFrameId(0L);
//                binVO.setCode("code");
//                binVO.setName("name");
//                binVO.setWareId(0L);
//                binVO.setWareCode("wareCode");
//                binVO.setWareName("wareName");
//                binVO.setAreaId(0L);
//                binVO.setAreaName("areaName");
//                binVO.setAreaCode("areaCode");
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.BinVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(binVO));
//            when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn(listR1);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO1 = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO1.setId(0L);
//                frameVO1.setWareId(0L);
//                frameVO1.setWareCode("wareCode");
//                frameVO1.setWareName("wareName");
//                frameVO1.setAreaId(0L);
//                frameVO1.setAreaName("areaName");
//                frameVO1.setAreaCode("areaCode");
//                frameVO1.setCode("code");
//                frameVO1.setName("name");
//                frameVO1.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO1);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR2);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemoteMasterDataServiceGetListByMaterialReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//        // Configure RemoteMasterDataService.getListByMaterial(...).
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemoteMasterDataServiceGetFrameInfoByTypeReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//         when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//            // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.BinVO binVO = new com.bosch.masterdata.api.domain.vo.BinVO();
//                binVO.setId(0L);
//                binVO.setFrameId(0L);
//                binVO.setCode("code");
//                binVO.setName("name");
//                binVO.setWareId(0L);
//                binVO.setWareCode("wareCode");
//                binVO.setWareName("wareName");
//                binVO.setAreaId(0L);
//                binVO.setAreaName("areaName");
//                binVO.setAreaCode("areaCode");
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.BinVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(binVO));
//            when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn(listR1);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR2);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemoteMasterDataServiceGetFrameInfoByTypeReturnsNoItems() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//         when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn( com.ruoyi.common.core.domain.R.ok(java.util.Collections.emptyList()) );
//
//            // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.BinVO binVO = new com.bosch.masterdata.api.domain.vo.BinVO();
//                binVO.setId(0L);
//                binVO.setFrameId(0L);
//                binVO.setCode("code");
//                binVO.setName("name");
//                binVO.setWareId(0L);
//                binVO.setWareCode("wareCode");
//                binVO.setWareName("wareName");
//                binVO.setAreaId(0L);
//                binVO.setAreaName("areaName");
//                binVO.setAreaCode("areaCode");
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.BinVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(binVO));
//            when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn(listR1);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR2);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemoteMasterDataServiceGetFrameInfoByTypeReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//        // Configure RemoteMasterDataService.getFrameInfoByType(...).
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR1 = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR1);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemoteMasterDataServiceSelectBinVOByFrameTypeReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//            // Configure RemoteMasterDataService.getFrameInfoByType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(frameVO));
//            when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR1);
//
//         when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO1 = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO1.setId(0L);
//                frameVO1.setWareId(0L);
//                frameVO1.setWareCode("wareCode");
//                frameVO1.setWareName("wareName");
//                frameVO1.setAreaId(0L);
//                frameVO1.setAreaName("areaName");
//                frameVO1.setAreaCode("areaCode");
//                frameVO1.setCode("code");
//                frameVO1.setName("name");
//                frameVO1.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO1);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR2);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemoteMasterDataServiceSelectBinVOByFrameTypeReturnsNoItems() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//            // Configure RemoteMasterDataService.getFrameInfoByType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(frameVO));
//            when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR1);
//
//         when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn( com.ruoyi.common.core.domain.R.ok(java.util.Collections.emptyList()) );
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO1 = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO1.setId(0L);
//                frameVO1.setWareId(0L);
//                frameVO1.setWareCode("wareCode");
//                frameVO1.setWareName("wareName");
//                frameVO1.setAreaId(0L);
//                frameVO1.setAreaName("areaName");
//                frameVO1.setAreaCode("areaCode");
//                frameVO1.setCode("code");
//                frameVO1.setName("name");
//                frameVO1.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO1);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR2);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemoteMasterDataServiceSelectBinVOByFrameTypeReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//            // Configure RemoteMasterDataService.getFrameInfoByType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(frameVO));
//            when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR1);
//
//        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.BinVO>> listR2 = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn(listR2);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemoteMasterDataServiceGetFrameInfoByCodeReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//            // Configure RemoteMasterDataService.getFrameInfoByType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(frameVO));
//            when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR1);
//
//            // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.BinVO binVO = new com.bosch.masterdata.api.domain.vo.BinVO();
//                binVO.setId(0L);
//                binVO.setFrameId(0L);
//                binVO.setCode("code");
//                binVO.setName("name");
//                binVO.setWareId(0L);
//                binVO.setWareCode("wareCode");
//                binVO.setWareName("wareName");
//                binVO.setAreaId(0L);
//                binVO.setAreaName("areaName");
//                binVO.setAreaCode("areaCode");
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.BinVO>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(binVO));
//            when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn(listR2);
//
//         when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR3 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR3);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemoteMasterDataServiceGetFrameInfoByCodeReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//            // Configure RemoteMasterDataService.getFrameInfoByType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(frameVO));
//            when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR1);
//
//            // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.BinVO binVO = new com.bosch.masterdata.api.domain.vo.BinVO();
//                binVO.setId(0L);
//                binVO.setFrameId(0L);
//                binVO.setCode("code");
//                binVO.setName("name");
//                binVO.setWareId(0L);
//                binVO.setWareCode("wareCode");
//                binVO.setWareName("wareName");
//                binVO.setAreaId(0L);
//                binVO.setAreaName("areaName");
//                binVO.setAreaCode("areaCode");
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.BinVO>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(binVO));
//            when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn(listR2);
//
//        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemoteMasterDataServiceGetInfoByFrameIdReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//            // Configure RemoteMasterDataService.getFrameInfoByType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(frameVO));
//            when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR1);
//
//            // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.BinVO binVO = new com.bosch.masterdata.api.domain.vo.BinVO();
//                binVO.setId(0L);
//                binVO.setFrameId(0L);
//                binVO.setCode("code");
//                binVO.setName("name");
//                binVO.setWareId(0L);
//                binVO.setWareCode("wareCode");
//                binVO.setWareName("wareName");
//                binVO.setAreaId(0L);
//                binVO.setAreaName("areaName");
//                binVO.setAreaCode("areaCode");
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.BinVO>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(binVO));
//            when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn(listR2);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO1 = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO1.setId(0L);
//                frameVO1.setWareId(0L);
//                frameVO1.setWareCode("wareCode");
//                frameVO1.setWareName("wareName");
//                frameVO1.setAreaId(0L);
//                frameVO1.setAreaName("areaName");
//                frameVO1.setAreaCode("areaCode");
//                frameVO1.setCode("code");
//                frameVO1.setName("name");
//                frameVO1.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO1);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//         when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemoteMasterDataServiceGetInfoByFrameIdReturnsNoItems() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//            // Configure RemoteMasterDataService.getFrameInfoByType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(frameVO));
//            when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR1);
//
//            // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.BinVO binVO = new com.bosch.masterdata.api.domain.vo.BinVO();
//                binVO.setId(0L);
//                binVO.setFrameId(0L);
//                binVO.setCode("code");
//                binVO.setName("name");
//                binVO.setWareId(0L);
//                binVO.setWareCode("wareCode");
//                binVO.setWareName("wareName");
//                binVO.setAreaId(0L);
//                binVO.setAreaName("areaName");
//                binVO.setAreaCode("areaCode");
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.BinVO>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(binVO));
//            when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn(listR2);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO1 = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO1.setId(0L);
//                frameVO1.setWareId(0L);
//                frameVO1.setWareCode("wareCode");
//                frameVO1.setWareName("wareName");
//                frameVO1.setAreaId(0L);
//                frameVO1.setAreaName("areaName");
//                frameVO1.setAreaCode("areaCode");
//                frameVO1.setCode("code");
//                frameVO1.setName("name");
//                frameVO1.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO1);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//         when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn( com.ruoyi.common.core.domain.R.ok(java.util.Collections.emptyList()) );
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBinAllocationVO_RemoteMasterDataServiceGetInfoByFrameIdReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.binin.api.domain.dto.BinAllocationDTO binAllocationDTO = new com.bosch.binin.api.domain.dto.BinAllocationDTO();
//                binAllocationDTO.setMesBarCode("mesBarCode");
//                binAllocationDTO.setPalletType("palletType");
//                binAllocationDTO.setPalletCode("palletCode");
//
//         final com.bosch.binin.api.domain.vo.BinAllocationVO expectedResult = new com.bosch.binin.api.domain.vo.BinAllocationVO();
//                expectedResult.setMesBarCode("mesBarCode");
//                expectedResult.setSsccNumber("ssccNumber");
//                expectedResult.setMaterialNb("materialNb");
//                expectedResult.setPalletType("palletType");
//                expectedResult.setPalletCode("palletCode");
//                expectedResult.setWareCode("wareCode");
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getListByMaterial(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialBinVO materialBinVO = new com.bosch.masterdata.api.domain.vo.MaterialBinVO();
//                materialBinVO.setId(0L);
//                materialBinVO.setMaterialId(0L);
//                materialBinVO.setMaterialCode("materialCode");
//                materialBinVO.setMaterialName("materialName");
//                materialBinVO.setFrameTypeCode("frameTypeCode");
//                materialBinVO.setFrameName("frameName");
//                materialBinVO.setPriorityLevel(0L);
//                materialBinVO.setCreateTime(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.MaterialBinVO>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(materialBinVO));
//            when( mockRemoteMasterDataService .getListByMaterial("materialCode")).thenReturn(listR);
//
//            // Configure RemoteMasterDataService.getFrameInfoByType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.FrameVO>> listR1 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(frameVO));
//            when( mockRemoteMasterDataService .getFrameInfoByType("type")).thenReturn(listR1);
//
//            // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
//                                                        final com.bosch.masterdata.api.domain.vo.BinVO binVO = new com.bosch.masterdata.api.domain.vo.BinVO();
//                binVO.setId(0L);
//                binVO.setFrameId(0L);
//                binVO.setCode("code");
//                binVO.setName("name");
//                binVO.setWareId(0L);
//                binVO.setWareCode("wareCode");
//                binVO.setWareName("wareName");
//                binVO.setAreaId(0L);
//                binVO.setAreaName("areaName");
//                binVO.setAreaCode("areaCode");
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.vo.BinVO>> listR2 = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(binVO));
//            when( mockRemoteMasterDataService .selectBinVOByFrameType("typeCode")).thenReturn(listR2);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO1 = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO1.setId(0L);
//                frameVO1.setWareId(0L);
//                frameVO1.setWareCode("wareCode");
//                frameVO1.setWareName("wareName");
//                frameVO1.setAreaId(0L);
//                frameVO1.setAreaName("areaName");
//                frameVO1.setAreaCode("areaCode");
//                frameVO1.setCode("code");
//                frameVO1.setName("name");
//                frameVO1.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO1);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//        // Configure RemoteMasterDataService.getInfoByFrameId(...).
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR3 = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR3);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.BinAllocationVO result =  iBinAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//    @Test
//    void testSortList() throws Exception {
//                 assertThat( iBinAssignmentServiceImplUnderTest.sortList(java.util.Arrays.asList("value")) ).isEqualTo(java.util.Arrays.asList("value")) ;
//                 assertThat( iBinAssignmentServiceImplUnderTest.sortList(java.util.Arrays.asList("value")) ).isEqualTo(java.util.Collections.emptyList()) ;
//                    }
//
//    @Test
//    void testGetCanUseCode() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final java.lang.String result =  iBinAssignmentServiceImplUnderTest.getCanUseCode(java.util.Arrays.asList("value"),java.util.Arrays.asList("value"),material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo( "result" ) ;
//    }
//
//    @Test
//    void testGetCanUseCode_RemoteMasterDataServiceGetFrameInfoByCodeReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final java.lang.String result =  iBinAssignmentServiceImplUnderTest.getCanUseCode(java.util.Arrays.asList("value"),java.util.Arrays.asList("value"),material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo( "result" ) ;
//    }
//
//    @Test
//    void testGetCanUseCode_RemoteMasterDataServiceGetFrameInfoByCodeReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//    // Run the test
// final java.lang.String result =  iBinAssignmentServiceImplUnderTest.getCanUseCode(java.util.Arrays.asList("value"),java.util.Arrays.asList("value"),material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo( "result" ) ;
//    }
//
//    @Test
//    void testGetCanUseCode_BinInMapperReturnsNoItems() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//         when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn( java.util.Collections.emptyList() );
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final java.lang.String result =  iBinAssignmentServiceImplUnderTest.getCanUseCode(java.util.Arrays.asList("value"),java.util.Arrays.asList("value"),material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo( "result" ) ;
//    }
//
//    @Test
//    void testGetCanUseCode_RemotePalletServiceReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//         when( mockRemotePalletService .getByType("palletType")).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final java.lang.String result =  iBinAssignmentServiceImplUnderTest.getCanUseCode(java.util.Arrays.asList("value"),java.util.Arrays.asList("value"),material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo( "result" ) ;
//    }
//
//    @Test
//    void testGetCanUseCode_RemotePalletServiceReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//        // Configure RemotePalletService.getByType(...).
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final java.lang.String result =  iBinAssignmentServiceImplUnderTest.getCanUseCode(java.util.Arrays.asList("value"),java.util.Arrays.asList("value"),material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo( "result" ) ;
//    }
//
//    @Test
//    void testGetCanUseCode_RemoteMaterialServiceReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//         when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final java.lang.String result =  iBinAssignmentServiceImplUnderTest.getCanUseCode(java.util.Arrays.asList("value"),java.util.Arrays.asList("value"),material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo( "result" ) ;
//    }
//
//    @Test
//    void testGetCanUseCode_RemoteMaterialServiceReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final java.lang.String result =  iBinAssignmentServiceImplUnderTest.getCanUseCode(java.util.Arrays.asList("value"),java.util.Arrays.asList("value"),material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo( "result" ) ;
//    }
//
//    @Test
//    void testGetCanUseCode_RemoteMasterDataServiceGetInfoByFrameIdReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//         when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//    // Run the test
// final java.lang.String result =  iBinAssignmentServiceImplUnderTest.getCanUseCode(java.util.Arrays.asList("value"),java.util.Arrays.asList("value"),material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo( "result" ) ;
//    }
//
//    @Test
//    void testGetCanUseCode_RemoteMasterDataServiceGetInfoByFrameIdReturnsNoItems() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//         when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn( com.ruoyi.common.core.domain.R.ok(java.util.Collections.emptyList()) );
//
//    // Run the test
// final java.lang.String result =  iBinAssignmentServiceImplUnderTest.getCanUseCode(java.util.Arrays.asList("value"),java.util.Arrays.asList("value"),material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo( "result" ) ;
//    }
//
//    @Test
//    void testGetCanUseCode_RemoteMasterDataServiceGetInfoByFrameIdReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//        // Configure RemoteMasterDataService.getInfoByFrameId(...).
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final java.lang.String result =  iBinAssignmentServiceImplUnderTest.getCanUseCode(java.util.Arrays.asList("value"),java.util.Arrays.asList("value"),material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo( "result" ) ;
//    }
//
//    @Test
//    void testValidateBin() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.validateBin("frameCode",material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testValidateBin_RemoteMasterDataServiceGetFrameInfoByCodeReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//         when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.validateBin("frameCode",material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testValidateBin_RemoteMasterDataServiceGetFrameInfoByCodeReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.validateBin("frameCode",material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testValidateBin_BinInMapperReturnsNoItems() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//         when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn( java.util.Collections.emptyList() );
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.validateBin("frameCode",material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testValidateBin_RemotePalletServiceReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//         when( mockRemotePalletService .getByType("palletType")).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.validateBin("frameCode",material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testValidateBin_RemotePalletServiceReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//        // Configure RemotePalletService.getByType(...).
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.validateBin("frameCode",material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testValidateBin_RemoteMaterialServiceReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//         when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.validateBin("frameCode",material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testValidateBin_RemoteMaterialServiceReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.validateBin("frameCode",material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testValidateBin_RemoteMasterDataServiceGetInfoByFrameIdReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//         when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.validateBin("frameCode",material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testValidateBin_RemoteMasterDataServiceGetInfoByFrameIdReturnsNoItems() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//         when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn( com.ruoyi.common.core.domain.R.ok(java.util.Collections.emptyList()) );
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.validateBin("frameCode",material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testValidateBin_RemoteMasterDataServiceGetInfoByFrameIdReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.vo.MaterialVO material = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                material.setId(0L);
//                material.setCode("code");
//                material.setName("name");
//                material.setMinPackageNumber(0L);
//                material.setMaterialTypeId(0L);
//                material.setMaterialType("materialType");
//                material.setUnit("unit");
//                material.setErrorProofingMethod("errorProofingMethod");
//                material.setHasPallet(0);
//                material.setBindPallet(0);
//
//         final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure RemoteMasterDataService.getFrameInfoByCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.FrameVO frameVO = new com.bosch.masterdata.api.domain.vo.FrameVO();
//                frameVO.setId(0L);
//                frameVO.setWareId(0L);
//                frameVO.setWareCode("wareCode");
//                frameVO.setWareName("wareName");
//                frameVO.setAreaId(0L);
//                frameVO.setAreaName("areaName");
//                frameVO.setAreaCode("areaCode");
//                frameVO.setCode("code");
//                frameVO.setName("name");
//                frameVO.setWidth(new java.math.BigDecimal("0.00"));
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.FrameVO> frameVOR = com.ruoyi.common.core.domain.R.ok(frameVO);
//            when( mockRemoteMasterDataService .getFrameInfoByCode("code")).thenReturn(frameVOR);
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//        // Configure RemoteMasterDataService.getInfoByFrameId(...).
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.validateBin("frameCode",material,pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBins() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.getBins(0L,"frameCode",pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBins_BinInMapperReturnsNoItems() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//         when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn( java.util.Collections.emptyList() );
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.getBins(0L,"frameCode",pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBins_RemotePalletServiceReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//         when( mockRemotePalletService .getByType("palletType")).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.getBins(0L,"frameCode",pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBins_RemotePalletServiceReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//        // Configure RemotePalletService.getByType(...).
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.getBins(0L,"frameCode",pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBins_RemoteMaterialServiceReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//         when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.getBins(0L,"frameCode",pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBins_RemoteMaterialServiceReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//            // Configure RemoteMasterDataService.getInfoByFrameId(...).
//                                                        final com.bosch.masterdata.api.domain.Bin bin = new com.bosch.masterdata.api.domain.Bin();
//                bin.setId(0L);
//                bin.setFrameId(0L);
//                bin.setName("name");
//                bin.setCode("code");
//                bin.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.ok(java.util.Arrays.asList(bin));
//            when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.getBins(0L,"frameCode",pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBins_RemoteMasterDataServiceReturnsNoItem() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//         when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.getBins(0L,"frameCode",pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBins_RemoteMasterDataServiceReturnsNoItems() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//         when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn( com.ruoyi.common.core.domain.R.ok(java.util.Collections.emptyList()) );
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.getBins(0L,"frameCode",pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetBins_RemoteMasterDataServiceReturnsFailure() throws Exception {
//    // Setup
//                final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//
//         final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure BinInMapper.selectList(...).
//                                                        final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//            when( mockBinInMapper .selectList(any(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.class))).thenReturn(binIns);
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet1 = new com.bosch.masterdata.api.domain.Pallet();
//                pallet1.setId(0L);
//                pallet1.setType("type");
//                pallet1.setLength(new java.math.BigDecimal("0.00"));
//                pallet1.setWidth(new java.math.BigDecimal("0.00"));
//                pallet1.setHeight(new java.math.BigDecimal("0.00"));
//                pallet1.setVirtualPrefixCode("virtualPrefixCode");
//                pallet1.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet1);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//        // Configure RemoteMasterDataService.getInfoByFrameId(...).
//        final com.ruoyi.common.core.domain.R<java.util.List<com.bosch.masterdata.api.domain.Bin>> listR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemoteMasterDataService .getInfoByFrameId(0L)).thenReturn(listR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.getBins(0L,"frameCode",pallet);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetFrameRemain() throws Exception {
//    // Setup
//                                                                final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> actualBins = java.util.Arrays.asList(binIn);
//        final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.getFrameRemain(actualBins);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetFrameRemain_RemotePalletServiceReturnsNoItem() throws Exception {
//    // Setup
//                                                                final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> actualBins = java.util.Arrays.asList(binIn);
//        final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//         when( mockRemotePalletService .getByType("palletType")).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.getFrameRemain(actualBins);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetFrameRemain_RemotePalletServiceReturnsFailure() throws Exception {
//    // Setup
//                                                                final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> actualBins = java.util.Arrays.asList(binIn);
//        final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//        // Configure RemotePalletService.getByType(...).
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//            // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//                                                        final com.bosch.masterdata.api.domain.vo.MaterialVO materialVO = new com.bosch.masterdata.api.domain.vo.MaterialVO();
//                materialVO.setId(0L);
//                materialVO.setCode("code");
//                materialVO.setName("name");
//                materialVO.setMinPackageNumber(0L);
//                materialVO.setMaterialTypeId(0L);
//                materialVO.setMaterialType("materialType");
//                materialVO.setUnit("unit");
//                materialVO.setErrorProofingMethod("errorProofingMethod");
//                materialVO.setHasPallet(0);
//                materialVO.setBindPallet(0);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.ok(materialVO);
//            when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.getFrameRemain(actualBins);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetFrameRemain_RemoteMaterialServiceReturnsNoItem() throws Exception {
//    // Setup
//                                                                final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> actualBins = java.util.Arrays.asList(binIn);
//        final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//         when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn( com.ruoyi.common.core.domain.R.ok() );
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.getFrameRemain(actualBins);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetFrameRemain_RemoteMaterialServiceReturnsFailure() throws Exception {
//    // Setup
//                                                                final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> actualBins = java.util.Arrays.asList(binIn);
//        final com.bosch.binin.api.domain.vo.FrameRemainVO expectedResult = new com.bosch.binin.api.domain.vo.FrameRemainVO();
//                expectedResult.setFrameWidth(new java.math.BigDecimal("0.00"));
//                expectedResult.setFrameBearWeight(new java.math.BigDecimal("0.00"));
//                expectedResult.setRecommendBinCode("recommendBinCode");
//
//            // Configure RemotePalletService.getByType(...).
//                                                        final com.bosch.masterdata.api.domain.Pallet pallet = new com.bosch.masterdata.api.domain.Pallet();
//                pallet.setId(0L);
//                pallet.setType("type");
//                pallet.setLength(new java.math.BigDecimal("0.00"));
//                pallet.setWidth(new java.math.BigDecimal("0.00"));
//                pallet.setHeight(new java.math.BigDecimal("0.00"));
//                pallet.setVirtualPrefixCode("virtualPrefixCode");
//                pallet.setStatus(0L);
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.Pallet> palletR = com.ruoyi.common.core.domain.R.ok(pallet);
//            when( mockRemotePalletService .getByType("palletType")).thenReturn(palletR);
//
//        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
//        final com.ruoyi.common.core.domain.R<com.bosch.masterdata.api.domain.vo.MaterialVO> materialVOR = com.ruoyi.common.core.domain.R.fail();
//        when( mockRemoteMaterialService .getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);
//
//    // Run the test
// final com.bosch.binin.api.domain.vo.FrameRemainVO result =  iBinAssignmentServiceImplUnderTest.getFrameRemain(actualBins);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//
//    @Test
//    void testGetUsedBins() throws Exception {
//    // Setup
//                                                                final com.bosch.binin.api.domain.BinIn binIn = new com.bosch.binin.api.domain.BinIn();
//                binIn.setId(0L);
//                binIn.setMaterialNb("materialNb");
//                binIn.setBatchNb("batchNb");
//                binIn.setExpireDate(new java.util.GregorianCalendar(2020, java.util.Calendar.JANUARY, 1).getTime());
//                binIn.setPlantNb("plantNb");
//                binIn.setWareCode("wareCode");
//                binIn.setRecommendBinCode("recommendBinCode");
//                binIn.setActualBinCode("actualBinCode");
//                binIn.setQuantity(0);
//                binIn.setPalletCode("palletCode");
//        final java.util.List<com.bosch.binin.api.domain.BinIn> binIns = java.util.Arrays.asList(binIn);
//        final java.util.Map<java.lang.String,java.lang.String> expectedResult = new java.util.HashMap<>();
//
//    // Run the test
// final java.util.Map<java.lang.String,java.lang.String> result =  iBinAssignmentServiceImplUnderTest.getUsedBins(binIns);
//
//        // Verify the results
// assertThat(result).isEqualTo(expectedResult ) ;
//    }
//                                                }
//
