package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.dto.BinAllocationDTO;
import com.bosch.binin.api.domain.vo.BinAllocationVO;
import com.bosch.binin.api.domain.vo.FrameRemainVO;
import com.bosch.binin.mapper.BinInMapper;
import com.bosch.binin.mapper.StockMapper;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.RemotePalletService;
import com.bosch.masterdata.api.domain.Bin;
import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.domain.vo.BinVO;
import com.bosch.masterdata.api.domain.vo.FrameVO;
import com.bosch.masterdata.api.domain.vo.MaterialBinVO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.storagein.api.RemoteMaterialInService;
import com.ruoyi.common.core.domain.R;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BinAssignmentServiceImplTest {

    @Mock
    private BinInMapper mockBinInMapper;
    @Mock
    private StockMapper mockStockMapper;
    @Mock
    private RemotePalletService mockRemotePalletService;
    @Mock
    private RemoteMaterialInService mockRemoteMaterialInService;
    @Mock
    private RemoteMaterialService mockRemoteMaterialService;
    @Mock
    private RemoteMasterDataService mockRemoteMasterDataService;

    @InjectMocks
    private BinAssignmentServiceImpl binAssignmentServiceImplUnderTest;

    @Test
    void testGetBinAllocationVO() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<List<FrameVO>> listR1 = R.ok(Arrays.asList(frameVO));
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR1);

        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
        final BinVO binVO = new BinVO();
        binVO.setId(0L);
        binVO.setFrameId(0L);
        binVO.setCode("code");
        binVO.setName("name");
        binVO.setWareId(0L);
        binVO.setWareCode("wareCode");
        binVO.setWareName("wareName");
        binVO.setAreaId(0L);
        binVO.setAreaName("areaName");
        binVO.setAreaCode("areaCode");
        final R<List<BinVO>> listR2 = R.ok(Arrays.asList(binVO));
        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(listR2);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO1 = new FrameVO();
        frameVO1.setId(0L);
        frameVO1.setWareId(0L);
        frameVO1.setWareCode("wareCode");
        frameVO1.setWareName("wareName");
        frameVO1.setAreaId(0L);
        frameVO1.setAreaName("areaName");
        frameVO1.setAreaCode("areaCode");
        frameVO1.setCode("code");
        frameVO1.setName("name");
        frameVO1.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO1);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR3 = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR3);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_BinInMapperReturnsNoItems() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<List<FrameVO>> listR1 = R.ok(Arrays.asList(frameVO));
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR1);

        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
        final BinVO binVO = new BinVO();
        binVO.setId(0L);
        binVO.setFrameId(0L);
        binVO.setCode("code");
        binVO.setName("name");
        binVO.setWareId(0L);
        binVO.setWareCode("wareCode");
        binVO.setWareName("wareName");
        binVO.setAreaId(0L);
        binVO.setAreaName("areaName");
        binVO.setAreaCode("areaCode");
        final R<List<BinVO>> listR2 = R.ok(Arrays.asList(binVO));
        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(listR2);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO1 = new FrameVO();
        frameVO1.setId(0L);
        frameVO1.setWareId(0L);
        frameVO1.setWareCode("wareCode");
        frameVO1.setWareName("wareName");
        frameVO1.setAreaId(0L);
        frameVO1.setAreaName("areaName");
        frameVO1.setAreaCode("areaCode");
        frameVO1.setCode("code");
        frameVO1.setName("name");
        frameVO1.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO1);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR3 = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR3);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemotePalletServiceReturnsNoItem() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        when(mockRemotePalletService.getByType("palletType")).thenReturn(R.ok());

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<List<FrameVO>> listR1 = R.ok(Arrays.asList(frameVO));
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR1);

        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
        final BinVO binVO = new BinVO();
        binVO.setId(0L);
        binVO.setFrameId(0L);
        binVO.setCode("code");
        binVO.setName("name");
        binVO.setWareId(0L);
        binVO.setWareCode("wareCode");
        binVO.setWareName("wareName");
        binVO.setAreaId(0L);
        binVO.setAreaName("areaName");
        binVO.setAreaCode("areaCode");
        final R<List<BinVO>> listR2 = R.ok(Arrays.asList(binVO));
        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(listR2);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO1 = new FrameVO();
        frameVO1.setId(0L);
        frameVO1.setWareId(0L);
        frameVO1.setWareCode("wareCode");
        frameVO1.setWareName("wareName");
        frameVO1.setAreaId(0L);
        frameVO1.setAreaName("areaName");
        frameVO1.setAreaCode("areaCode");
        frameVO1.setCode("code");
        frameVO1.setName("name");
        frameVO1.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO1);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR3 = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR3);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemotePalletServiceReturnsFailure() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final R<Pallet> palletR = R.fail();
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<List<FrameVO>> listR1 = R.ok(Arrays.asList(frameVO));
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR1);

        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
        final BinVO binVO = new BinVO();
        binVO.setId(0L);
        binVO.setFrameId(0L);
        binVO.setCode("code");
        binVO.setName("name");
        binVO.setWareId(0L);
        binVO.setWareCode("wareCode");
        binVO.setWareName("wareName");
        binVO.setAreaId(0L);
        binVO.setAreaName("areaName");
        binVO.setAreaCode("areaCode");
        final R<List<BinVO>> listR2 = R.ok(Arrays.asList(binVO));
        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(listR2);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO1 = new FrameVO();
        frameVO1.setId(0L);
        frameVO1.setWareId(0L);
        frameVO1.setWareCode("wareCode");
        frameVO1.setWareName("wareName");
        frameVO1.setAreaId(0L);
        frameVO1.setAreaName("areaName");
        frameVO1.setAreaCode("areaCode");
        frameVO1.setCode("code");
        frameVO1.setName("name");
        frameVO1.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO1);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR3 = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR3);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemoteMaterialServiceReturnsNoItem() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(R.ok());

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<List<FrameVO>> listR1 = R.ok(Arrays.asList(frameVO));
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR1);

        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
        final BinVO binVO = new BinVO();
        binVO.setId(0L);
        binVO.setFrameId(0L);
        binVO.setCode("code");
        binVO.setName("name");
        binVO.setWareId(0L);
        binVO.setWareCode("wareCode");
        binVO.setWareName("wareName");
        binVO.setAreaId(0L);
        binVO.setAreaName("areaName");
        binVO.setAreaCode("areaCode");
        final R<List<BinVO>> listR2 = R.ok(Arrays.asList(binVO));
        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(listR2);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO1 = new FrameVO();
        frameVO1.setId(0L);
        frameVO1.setWareId(0L);
        frameVO1.setWareCode("wareCode");
        frameVO1.setWareName("wareName");
        frameVO1.setAreaId(0L);
        frameVO1.setAreaName("areaName");
        frameVO1.setAreaCode("areaCode");
        frameVO1.setCode("code");
        frameVO1.setName("name");
        frameVO1.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO1);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR3 = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR3);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemoteMaterialServiceReturnsFailure() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final R<MaterialVO> materialVOR = R.fail();
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<List<FrameVO>> listR1 = R.ok(Arrays.asList(frameVO));
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR1);

        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
        final BinVO binVO = new BinVO();
        binVO.setId(0L);
        binVO.setFrameId(0L);
        binVO.setCode("code");
        binVO.setName("name");
        binVO.setWareId(0L);
        binVO.setWareCode("wareCode");
        binVO.setWareName("wareName");
        binVO.setAreaId(0L);
        binVO.setAreaName("areaName");
        binVO.setAreaCode("areaCode");
        final R<List<BinVO>> listR2 = R.ok(Arrays.asList(binVO));
        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(listR2);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO1 = new FrameVO();
        frameVO1.setId(0L);
        frameVO1.setWareId(0L);
        frameVO1.setWareCode("wareCode");
        frameVO1.setWareName("wareName");
        frameVO1.setAreaId(0L);
        frameVO1.setAreaName("areaName");
        frameVO1.setAreaCode("areaCode");
        frameVO1.setCode("code");
        frameVO1.setName("name");
        frameVO1.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO1);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR3 = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR3);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemoteMasterDataServiceGetListByMaterialAndWareReturnsNoItem() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(R.ok());

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<List<FrameVO>> listR = R.ok(Arrays.asList(frameVO));
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR);

        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
        final BinVO binVO = new BinVO();
        binVO.setId(0L);
        binVO.setFrameId(0L);
        binVO.setCode("code");
        binVO.setName("name");
        binVO.setWareId(0L);
        binVO.setWareCode("wareCode");
        binVO.setWareName("wareName");
        binVO.setAreaId(0L);
        binVO.setAreaName("areaName");
        binVO.setAreaCode("areaCode");
        final R<List<BinVO>> listR1 = R.ok(Arrays.asList(binVO));
        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(listR1);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO1 = new FrameVO();
        frameVO1.setId(0L);
        frameVO1.setWareId(0L);
        frameVO1.setWareCode("wareCode");
        frameVO1.setWareName("wareName");
        frameVO1.setAreaId(0L);
        frameVO1.setAreaName("areaName");
        frameVO1.setAreaCode("areaCode");
        frameVO1.setCode("code");
        frameVO1.setName("name");
        frameVO1.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO1);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR2 = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR2);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemoteMasterDataServiceGetListByMaterialAndWareReturnsNoItems() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(R.ok(Collections.emptyList()));

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<List<FrameVO>> listR = R.ok(Arrays.asList(frameVO));
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR);

        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
        final BinVO binVO = new BinVO();
        binVO.setId(0L);
        binVO.setFrameId(0L);
        binVO.setCode("code");
        binVO.setName("name");
        binVO.setWareId(0L);
        binVO.setWareCode("wareCode");
        binVO.setWareName("wareName");
        binVO.setAreaId(0L);
        binVO.setAreaName("areaName");
        binVO.setAreaCode("areaCode");
        final R<List<BinVO>> listR1 = R.ok(Arrays.asList(binVO));
        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(listR1);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO1 = new FrameVO();
        frameVO1.setId(0L);
        frameVO1.setWareId(0L);
        frameVO1.setWareCode("wareCode");
        frameVO1.setWareName("wareName");
        frameVO1.setAreaId(0L);
        frameVO1.setAreaName("areaName");
        frameVO1.setAreaCode("areaCode");
        frameVO1.setCode("code");
        frameVO1.setName("name");
        frameVO1.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO1);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR2 = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR2);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemoteMasterDataServiceGetListByMaterialAndWareReturnsFailure() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final R<List<MaterialBinVO>> listR = R.fail();
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemoteMasterDataServiceGetFrameInfoByTypeReturnsNoItem() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(R.ok());

        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
        final BinVO binVO = new BinVO();
        binVO.setId(0L);
        binVO.setFrameId(0L);
        binVO.setCode("code");
        binVO.setName("name");
        binVO.setWareId(0L);
        binVO.setWareCode("wareCode");
        binVO.setWareName("wareName");
        binVO.setAreaId(0L);
        binVO.setAreaName("areaName");
        binVO.setAreaCode("areaCode");
        final R<List<BinVO>> listR1 = R.ok(Arrays.asList(binVO));
        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(listR1);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR2 = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR2);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemoteMasterDataServiceGetFrameInfoByTypeReturnsNoItems() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(R.ok(Collections.emptyList()));

        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
        final BinVO binVO = new BinVO();
        binVO.setId(0L);
        binVO.setFrameId(0L);
        binVO.setCode("code");
        binVO.setName("name");
        binVO.setWareId(0L);
        binVO.setWareCode("wareCode");
        binVO.setWareName("wareName");
        binVO.setAreaId(0L);
        binVO.setAreaName("areaName");
        binVO.setAreaCode("areaCode");
        final R<List<BinVO>> listR1 = R.ok(Arrays.asList(binVO));
        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(listR1);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR2 = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR2);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemoteMasterDataServiceGetFrameInfoByTypeReturnsFailure() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final R<List<FrameVO>> listR1 = R.fail();
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR1);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemoteMasterDataServiceSelectBinVOByFrameTypeReturnsNoItem() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<List<FrameVO>> listR1 = R.ok(Arrays.asList(frameVO));
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR1);

        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(R.ok());

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO1 = new FrameVO();
        frameVO1.setId(0L);
        frameVO1.setWareId(0L);
        frameVO1.setWareCode("wareCode");
        frameVO1.setWareName("wareName");
        frameVO1.setAreaId(0L);
        frameVO1.setAreaName("areaName");
        frameVO1.setAreaCode("areaCode");
        frameVO1.setCode("code");
        frameVO1.setName("name");
        frameVO1.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO1);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR2 = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR2);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemoteMasterDataServiceSelectBinVOByFrameTypeReturnsNoItems() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<List<FrameVO>> listR1 = R.ok(Arrays.asList(frameVO));
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR1);

        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(R.ok(Collections.emptyList()));

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO1 = new FrameVO();
        frameVO1.setId(0L);
        frameVO1.setWareId(0L);
        frameVO1.setWareCode("wareCode");
        frameVO1.setWareName("wareName");
        frameVO1.setAreaId(0L);
        frameVO1.setAreaName("areaName");
        frameVO1.setAreaCode("areaCode");
        frameVO1.setCode("code");
        frameVO1.setName("name");
        frameVO1.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO1);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR2 = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR2);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemoteMasterDataServiceSelectBinVOByFrameTypeReturnsFailure() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<List<FrameVO>> listR1 = R.ok(Arrays.asList(frameVO));
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR1);

        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
        final R<List<BinVO>> listR2 = R.fail();
        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(listR2);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemoteMasterDataServiceGetFrameInfoByCodeReturnsNoItem() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<List<FrameVO>> listR1 = R.ok(Arrays.asList(frameVO));
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR1);

        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
        final BinVO binVO = new BinVO();
        binVO.setId(0L);
        binVO.setFrameId(0L);
        binVO.setCode("code");
        binVO.setName("name");
        binVO.setWareId(0L);
        binVO.setWareCode("wareCode");
        binVO.setWareName("wareName");
        binVO.setAreaId(0L);
        binVO.setAreaName("areaName");
        binVO.setAreaCode("areaCode");
        final R<List<BinVO>> listR2 = R.ok(Arrays.asList(binVO));
        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(listR2);

        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(R.ok());

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR3 = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR3);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemoteMasterDataServiceGetFrameInfoByCodeReturnsFailure() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<List<FrameVO>> listR1 = R.ok(Arrays.asList(frameVO));
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR1);

        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
        final BinVO binVO = new BinVO();
        binVO.setId(0L);
        binVO.setFrameId(0L);
        binVO.setCode("code");
        binVO.setName("name");
        binVO.setWareId(0L);
        binVO.setWareCode("wareCode");
        binVO.setWareName("wareName");
        binVO.setAreaId(0L);
        binVO.setAreaName("areaName");
        binVO.setAreaCode("areaCode");
        final R<List<BinVO>> listR2 = R.ok(Arrays.asList(binVO));
        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(listR2);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final R<FrameVO> frameVOR = R.fail();
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemoteMasterDataServiceGetInfoByFrameIdReturnsNoItem() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<List<FrameVO>> listR1 = R.ok(Arrays.asList(frameVO));
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR1);

        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
        final BinVO binVO = new BinVO();
        binVO.setId(0L);
        binVO.setFrameId(0L);
        binVO.setCode("code");
        binVO.setName("name");
        binVO.setWareId(0L);
        binVO.setWareCode("wareCode");
        binVO.setWareName("wareName");
        binVO.setAreaId(0L);
        binVO.setAreaName("areaName");
        binVO.setAreaCode("areaCode");
        final R<List<BinVO>> listR2 = R.ok(Arrays.asList(binVO));
        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(listR2);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO1 = new FrameVO();
        frameVO1.setId(0L);
        frameVO1.setWareId(0L);
        frameVO1.setWareCode("wareCode");
        frameVO1.setWareName("wareName");
        frameVO1.setAreaId(0L);
        frameVO1.setAreaName("areaName");
        frameVO1.setAreaCode("areaCode");
        frameVO1.setCode("code");
        frameVO1.setName("name");
        frameVO1.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO1);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(R.ok());

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemoteMasterDataServiceGetInfoByFrameIdReturnsNoItems() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<List<FrameVO>> listR1 = R.ok(Arrays.asList(frameVO));
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR1);

        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
        final BinVO binVO = new BinVO();
        binVO.setId(0L);
        binVO.setFrameId(0L);
        binVO.setCode("code");
        binVO.setName("name");
        binVO.setWareId(0L);
        binVO.setWareCode("wareCode");
        binVO.setWareName("wareName");
        binVO.setAreaId(0L);
        binVO.setAreaName("areaName");
        binVO.setAreaCode("areaCode");
        final R<List<BinVO>> listR2 = R.ok(Arrays.asList(binVO));
        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(listR2);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO1 = new FrameVO();
        frameVO1.setId(0L);
        frameVO1.setWareId(0L);
        frameVO1.setWareCode("wareCode");
        frameVO1.setWareName("wareName");
        frameVO1.setAreaId(0L);
        frameVO1.setAreaName("areaName");
        frameVO1.setAreaCode("areaCode");
        frameVO1.setCode("code");
        frameVO1.setName("name");
        frameVO1.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO1);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(R.ok(Collections.emptyList()));

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBinAllocationVO_RemoteMasterDataServiceGetInfoByFrameIdReturnsFailure() {
        // Setup
        final BinAllocationDTO binAllocationDTO = new BinAllocationDTO();
        binAllocationDTO.setMesBarCode("mesBarCode");
        binAllocationDTO.setPalletType("palletType");
        binAllocationDTO.setPalletCode("palletCode");
        binAllocationDTO.setWareCode("wareCode");

        final BinAllocationVO expectedResult = new BinAllocationVO();
        expectedResult.setMesBarCode("mesBarCode");
        expectedResult.setSsccNumber("ssccNumber");
        expectedResult.setMaterialNb("materialNb");
        expectedResult.setPalletType("palletType");
        expectedResult.setPalletCode("palletCode");
        expectedResult.setWareCode("wareCode");
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getListByMaterialAndWare(...).
        final MaterialBinVO materialBinVO = new MaterialBinVO();
        materialBinVO.setId(0L);
        materialBinVO.setMaterialId(0L);
        materialBinVO.setMaterialCode("materialCode");
        materialBinVO.setMaterialName("materialName");
        materialBinVO.setFrameTypeCode("frameTypeCode");
        materialBinVO.setFrameName("frameName");
        materialBinVO.setPriorityLevel(0L);
        materialBinVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final R<List<MaterialBinVO>> listR = R.ok(Arrays.asList(materialBinVO));
        when(mockRemoteMasterDataService.getListByMaterialAndWare("materialCode", "wareCode")).thenReturn(listR);

        // Configure RemoteMasterDataService.getFrameInfoByType(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<List<FrameVO>> listR1 = R.ok(Arrays.asList(frameVO));
        when(mockRemoteMasterDataService.getFrameInfoByType("type")).thenReturn(listR1);

        // Configure RemoteMasterDataService.selectBinVOByFrameType(...).
        final BinVO binVO = new BinVO();
        binVO.setId(0L);
        binVO.setFrameId(0L);
        binVO.setCode("code");
        binVO.setName("name");
        binVO.setWareId(0L);
        binVO.setWareCode("wareCode");
        binVO.setWareName("wareName");
        binVO.setAreaId(0L);
        binVO.setAreaName("areaName");
        binVO.setAreaCode("areaCode");
        final R<List<BinVO>> listR2 = R.ok(Arrays.asList(binVO));
        when(mockRemoteMasterDataService.selectBinVOByFrameType("typeCode")).thenReturn(listR2);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO1 = new FrameVO();
        frameVO1.setId(0L);
        frameVO1.setWareId(0L);
        frameVO1.setWareCode("wareCode");
        frameVO1.setWareName("wareName");
        frameVO1.setAreaId(0L);
        frameVO1.setAreaName("areaName");
        frameVO1.setAreaCode("areaCode");
        frameVO1.setCode("code");
        frameVO1.setName("name");
        frameVO1.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO1);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final R<List<Bin>> listR3 = R.fail();
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR3);

        // Run the test
        final BinAllocationVO result = binAssignmentServiceImplUnderTest.getBinAllocationVO(binAllocationDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testSortList() {
        assertThat(binAssignmentServiceImplUnderTest.sortList(Arrays.asList("value"))).isEqualTo(Arrays.asList("value"
        ));
        assertThat(binAssignmentServiceImplUnderTest.sortList(Arrays.asList("value"))).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetCanUseCode() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final String result = binAssignmentServiceImplUnderTest.getCanUseCode(Arrays.asList("value"), Arrays.asList(
                "value"), material, pallet);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testGetCanUseCode_RemoteMasterDataServiceGetFrameInfoByCodeReturnsNoItem() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(R.ok());

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final String result = binAssignmentServiceImplUnderTest.getCanUseCode(Arrays.asList("value"), Arrays.asList(
                "value"), material, pallet);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testGetCanUseCode_RemoteMasterDataServiceGetFrameInfoByCodeReturnsFailure() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final R<FrameVO> frameVOR = R.fail();
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Run the test
        final String result = binAssignmentServiceImplUnderTest.getCanUseCode(Arrays.asList("value"), Arrays.asList(
                "value"), material, pallet);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testGetCanUseCode_BinInMapperReturnsNoItems() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final String result = binAssignmentServiceImplUnderTest.getCanUseCode(Arrays.asList("value"), Arrays.asList(
                "value"), material, pallet);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testGetCanUseCode_RemotePalletServiceReturnsNoItem() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        when(mockRemotePalletService.getByType("palletType")).thenReturn(R.ok());

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final String result = binAssignmentServiceImplUnderTest.getCanUseCode(Arrays.asList("value"), Arrays.asList(
                "value"), material, pallet);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testGetCanUseCode_RemotePalletServiceReturnsFailure() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final R<Pallet> palletR = R.fail();
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final String result = binAssignmentServiceImplUnderTest.getCanUseCode(Arrays.asList("value"), Arrays.asList(
                "value"), material, pallet);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testGetCanUseCode_RemoteMaterialServiceReturnsNoItem() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(R.ok());

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final String result = binAssignmentServiceImplUnderTest.getCanUseCode(Arrays.asList("value"), Arrays.asList(
                "value"), material, pallet);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testGetCanUseCode_RemoteMaterialServiceReturnsFailure() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final R<MaterialVO> materialVOR = R.fail();
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final String result = binAssignmentServiceImplUnderTest.getCanUseCode(Arrays.asList("value"), Arrays.asList(
                "value"), material, pallet);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testGetCanUseCode_RemoteMasterDataServiceGetInfoByFrameIdReturnsNoItem() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(R.ok());

        // Run the test
        final String result = binAssignmentServiceImplUnderTest.getCanUseCode(Arrays.asList("value"), Arrays.asList(
                "value"), material, pallet);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testGetCanUseCode_RemoteMasterDataServiceGetInfoByFrameIdReturnsNoItems() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(R.ok(Collections.emptyList()));

        // Run the test
        final String result = binAssignmentServiceImplUnderTest.getCanUseCode(Arrays.asList("value"), Arrays.asList(
                "value"), material, pallet);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testGetCanUseCode_RemoteMasterDataServiceGetInfoByFrameIdReturnsFailure() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final R<List<Bin>> listR = R.fail();
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final String result = binAssignmentServiceImplUnderTest.getCanUseCode(Arrays.asList("value"), Arrays.asList(
                "value"), material, pallet);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testValidateBin() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.validateBin("frameCode", material, pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testValidateBin_RemoteMasterDataServiceGetFrameInfoByCodeReturnsNoItem() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(R.ok());

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.validateBin("frameCode", material, pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testValidateBin_RemoteMasterDataServiceGetFrameInfoByCodeReturnsFailure() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final R<FrameVO> frameVOR = R.fail();
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.validateBin("frameCode", material, pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testValidateBin_BinInMapperReturnsNoItems() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.validateBin("frameCode", material, pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testValidateBin_RemotePalletServiceReturnsNoItem() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        when(mockRemotePalletService.getByType("palletType")).thenReturn(R.ok());

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.validateBin("frameCode", material, pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testValidateBin_RemotePalletServiceReturnsFailure() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final R<Pallet> palletR = R.fail();
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.validateBin("frameCode", material, pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testValidateBin_RemoteMaterialServiceReturnsNoItem() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(R.ok());

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.validateBin("frameCode", material, pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testValidateBin_RemoteMaterialServiceReturnsFailure() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final R<MaterialVO> materialVOR = R.fail();
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.validateBin("frameCode", material, pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testValidateBin_RemoteMasterDataServiceGetInfoByFrameIdReturnsNoItem() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(R.ok());

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.validateBin("frameCode", material, pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testValidateBin_RemoteMasterDataServiceGetInfoByFrameIdReturnsNoItems() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(R.ok(Collections.emptyList()));

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.validateBin("frameCode", material, pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testValidateBin_RemoteMasterDataServiceGetInfoByFrameIdReturnsFailure() {
        // Setup
        final MaterialVO material = new MaterialVO();
        material.setId(0L);
        material.setCode("code");
        material.setName("name");
        material.setMinPackageNumber(0L);
        material.setMaterialTypeId(0L);
        material.setMaterialType("materialType");
        material.setUnit("unit");
        material.setErrorProofingMethod("errorProofingMethod");
        material.setHasPallet(0);
        material.setBindPallet(0);

        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure RemoteMasterDataService.getFrameInfoByCode(...).
        final FrameVO frameVO = new FrameVO();
        frameVO.setId(0L);
        frameVO.setWareId(0L);
        frameVO.setWareCode("wareCode");
        frameVO.setWareName("wareName");
        frameVO.setAreaId(0L);
        frameVO.setAreaName("areaName");
        frameVO.setAreaCode("areaCode");
        frameVO.setCode("code");
        frameVO.setName("name");
        frameVO.setWidth(new BigDecimal("0.00"));
        final R<FrameVO> frameVOR = R.ok(frameVO);
        when(mockRemoteMasterDataService.getFrameInfoByCode("code")).thenReturn(frameVOR);

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final R<List<Bin>> listR = R.fail();
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.validateBin("frameCode", material, pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBins() {
        // Setup
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.getBins(0L, "frameCode", pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBins_BinInMapperReturnsNoItems() {
        // Setup
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.getBins(0L, "frameCode", pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBins_RemotePalletServiceReturnsNoItem() {
        // Setup
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        when(mockRemotePalletService.getByType("palletType")).thenReturn(R.ok());

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.getBins(0L, "frameCode", pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBins_RemotePalletServiceReturnsFailure() {
        // Setup
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final R<Pallet> palletR = R.fail();
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.getBins(0L, "frameCode", pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBins_RemoteMaterialServiceReturnsNoItem() {
        // Setup
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(R.ok());

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.getBins(0L, "frameCode", pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBins_RemoteMaterialServiceReturnsFailure() {
        // Setup
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final R<MaterialVO> materialVOR = R.fail();
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final Bin bin = new Bin();
        bin.setId(0L);
        bin.setFrameId(0L);
        bin.setName("name");
        bin.setCode("code");
        bin.setStatus(0L);
        final R<List<Bin>> listR = R.ok(Arrays.asList(bin));
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.getBins(0L, "frameCode", pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBins_RemoteMasterDataServiceReturnsNoItem() {
        // Setup
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(R.ok());

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.getBins(0L, "frameCode", pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBins_RemoteMasterDataServiceReturnsNoItems() {
        // Setup
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(R.ok(Collections.emptyList()));

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.getBins(0L, "frameCode", pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBins_RemoteMasterDataServiceReturnsFailure() {
        // Setup
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);

        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure BinInMapper.selectList(...).
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        when(mockBinInMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(binIns);

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet1 = new Pallet();
        pallet1.setId(0L);
        pallet1.setType("type");
        pallet1.setLength(new BigDecimal("0.00"));
        pallet1.setWidth(new BigDecimal("0.00"));
        pallet1.setHeight(new BigDecimal("0.00"));
        pallet1.setVirtualPrefixCode("virtualPrefixCode");
        pallet1.setStatus(0L);
        pallet1.setDeleteFlag(0);
        pallet1.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet1);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Configure RemoteMasterDataService.getInfoByFrameId(...).
        final R<List<Bin>> listR = R.fail();
        when(mockRemoteMasterDataService.getInfoByFrameId(0L)).thenReturn(listR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.getBins(0L, "frameCode", pallet);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetFrameRemain() {
        // Setup
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> actualBins = Arrays.asList(binIn);
        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.getFrameRemain(actualBins);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetFrameRemain_RemotePalletServiceReturnsNoItem() {
        // Setup
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> actualBins = Arrays.asList(binIn);
        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        when(mockRemotePalletService.getByType("palletType")).thenReturn(R.ok());

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.getFrameRemain(actualBins);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetFrameRemain_RemotePalletServiceReturnsFailure() {
        // Setup
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> actualBins = Arrays.asList(binIn);
        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure RemotePalletService.getByType(...).
        final R<Pallet> palletR = R.fail();
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final MaterialVO materialVO = new MaterialVO();
        materialVO.setId(0L);
        materialVO.setCode("code");
        materialVO.setName("name");
        materialVO.setMinPackageNumber(0L);
        materialVO.setMaterialTypeId(0L);
        materialVO.setMaterialType("materialType");
        materialVO.setUnit("unit");
        materialVO.setErrorProofingMethod("errorProofingMethod");
        materialVO.setHasPallet(0);
        materialVO.setBindPallet(0);
        final R<MaterialVO> materialVOR = R.ok(materialVO);
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.getFrameRemain(actualBins);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetFrameRemain_RemoteMaterialServiceReturnsNoItem() {
        // Setup
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> actualBins = Arrays.asList(binIn);
        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(R.ok());

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.getFrameRemain(actualBins);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetFrameRemain_RemoteMaterialServiceReturnsFailure() {
        // Setup
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> actualBins = Arrays.asList(binIn);
        final FrameRemainVO expectedResult = new FrameRemainVO();
        expectedResult.setFrameWidth(new BigDecimal("0.00"));
        expectedResult.setFrameBearWeight(new BigDecimal("0.00"));
        expectedResult.setRecommendBinCode("recommendBinCode");

        // Configure RemotePalletService.getByType(...).
        final Pallet pallet = new Pallet();
        pallet.setId(0L);
        pallet.setType("type");
        pallet.setLength(new BigDecimal("0.00"));
        pallet.setWidth(new BigDecimal("0.00"));
        pallet.setHeight(new BigDecimal("0.00"));
        pallet.setVirtualPrefixCode("virtualPrefixCode");
        pallet.setStatus(0L);
        pallet.setDeleteFlag(0);
        pallet.setIsVirtual(0);
        final R<Pallet> palletR = R.ok(pallet);
        when(mockRemotePalletService.getByType("palletType")).thenReturn(palletR);

        // Configure RemoteMaterialService.getInfoByMaterialCode(...).
        final R<MaterialVO> materialVOR = R.fail();
        when(mockRemoteMaterialService.getInfoByMaterialCode("materialCode")).thenReturn(materialVOR);

        // Run the test
        final FrameRemainVO result = binAssignmentServiceImplUnderTest.getFrameRemain(actualBins);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetUsedBins() {
        // Setup
        final BinIn binIn = new BinIn();
        binIn.setId(0L);
        binIn.setMaterialNb("materialNb");
        binIn.setBatchNb("batchNb");
        binIn.setExpireDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        binIn.setPlantNb("plantNb");
        binIn.setWareCode("wareCode");
        binIn.setRecommendBinCode("recommendBinCode");
        binIn.setActualBinCode("actualBinCode");
        binIn.setQuantity(0.0);
        binIn.setPalletCode("palletCode");
        final List<BinIn> binIns = Arrays.asList(binIn);
        final Map<String, String> expectedResult = new HashMap<>();

        // Run the test
        final Map<String, String> result = binAssignmentServiceImplUnderTest.getUsedBins(binIns);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }
}
