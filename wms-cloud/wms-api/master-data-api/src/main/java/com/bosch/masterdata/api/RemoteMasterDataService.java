package com.bosch.masterdata.api;

import com.bosch.masterdata.api.domain.Bin;
import com.bosch.masterdata.api.domain.ProductFrame;
import com.bosch.masterdata.api.domain.Ware;
import com.bosch.masterdata.api.domain.dto.AreaDTO;
import com.bosch.masterdata.api.domain.dto.BlackDriverDTO;
import com.bosch.masterdata.api.domain.vo.*;
import com.bosch.masterdata.api.factory.RemoteMaterialFallbackFactory;
import com.ruoyi.common.core.config.FeignConfig;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(contextId = "remoteMasterDataService", configuration = FeignConfig.class,value = ServiceNameConstants.MASTER_DATA_SERVICE)
public interface RemoteMasterDataService {

    @GetMapping(value = "/bin/{id}")
    public R<BinVO> getBinInfo(@PathVariable("id") Long id);

    @GetMapping(value = "/bin/getInfoByCode/{code}")
    public R<BinVO> getBinInfoByCode(@PathVariable("code") String code);

    @GetMapping(value = "/materialBin/getListByMaterial")
    public R<List<MaterialBinVO>> getListByMaterial(@RequestParam("materialCode") String materialCode);

    //根据code和仓库
    @GetMapping(value = "/materialBin/getListByMaterialAndWare")
    public R<List<MaterialBinVO>> getListByMaterialAndWare(@RequestParam("materialCode") String materialCode, @RequestParam("wareCode") String wareCode);

    //根据code和仓库
    @GetMapping(value = "/productFrame/getProductFrame")
    public R<List<ProductFrameVO>> getProductFrame(@RequestParam("materialCode") String materialCode, @RequestParam("wareCode") String wareCode);


    @GetMapping(value = "/frame/getFrameInfo/{id}")
    public R<FrameVO> getFrameInfo(@PathVariable("id") Long id);

    @GetMapping(value = "/frame/getFrameInfoByCode/{code}")
    public R<FrameVO> getFrameInfoByCode(@PathVariable("code") String code);

    @GetMapping(value = "/frame/getFrameInfoByType/{type}")
    public R<List<FrameVO>> getFrameInfoByType(@PathVariable("type") String type, @RequestParam("wareCode") String wareCode);

    @GetMapping(value = "/bin/getInfoByFrameId/{frameId}")
    public R<List<Bin>> getInfoByFrameId(@PathVariable("frameId") Long frameId);

    //根据跨类型获取所有库位
    @GetMapping(value = "/bin/selectBinVOByFrameType/{typeCode}")
    public R<List<BinVO>> selectBinVOByFrameType(@PathVariable("typeCode") String typeCode);

    @GetMapping(value = "/ware/{id}")
    public R<Ware> getWareInfo(@PathVariable("id") String id);

    @GetMapping("/ware/getWareByCode/{wareCode}")
    public R<Ware> getWareByCode(@PathVariable("wareCode") String wareCode);

    @GetMapping(value = "/driver/black/{wechatId}")
    public R<List<BlackDriverVO>> getBlackDriverByWechatId(@PathVariable("wechatId") String wechatId, @RequestParam(required = false, value = "isBlack") boolean isBlack);

    @PostMapping("/driver/save")
    public R<Boolean> saveBlackDriver(@RequestBody BlackDriverDTO blackDriverDTO);

    @PostMapping(value = "/supplierInfo/selectInfo")
    public R<SupplierInfoVO> getSupplierInfoByCode(@RequestParam("code") String code);

    @GetMapping(value = "/supplierInfo/infoname/{name}")
    public R<SupplierInfoVO> getSupplierInfoByName(@PathVariable("name") String name);

    @GetMapping("/area/getByWareCode/{wareCode}")
    public R<List<AreaVO>> getByWareCode(@PathVariable("wareCode") String wareCode);

    @GetMapping("/area/getAreaListByDTO")
    public R<List<AreaVO>> getAreaListByDTO(@SpringQueryMap List<AreaDTO> areaDTOList);


    @GetMapping("/area/getByCode/{areaCode}")
    public R<AreaVO> getByCode(@PathVariable("areaCode") String areaCode);


    @GetMapping("/area/getByCodeAndPlant")
    public R<AreaVO> getByCodeAndPlant(@RequestParam("areaCode") String areaCode, @RequestParam("plantNb") String plantNb);
}