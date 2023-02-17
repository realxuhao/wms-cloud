package com.bosch.masterdata.api;

import com.bosch.masterdata.api.domain.Bin;
import com.bosch.masterdata.api.domain.Ware;
import com.bosch.masterdata.api.domain.vo.*;
import com.bosch.masterdata.api.factory.RemoteMaterialFallbackFactory;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "remoteMasterDataService", value = ServiceNameConstants.MASTER_DATA_SERVICE)
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

    @GetMapping(value = "/frame/getFrameInfo/{id}")
    public R<FrameVO> getFrameInfo(@PathVariable("id") Long id);

    @GetMapping(value = "/frame/getFrameInfoByCode/{code}")
    public R<FrameVO> getFrameInfoByCode(@PathVariable("code") String code);

    @GetMapping(value = "/frame/getFrameInfoByType/{type}")
    public R<List<FrameVO>> getFrameInfoByType(@PathVariable("type") String type);

    @GetMapping(value = "/bin/getInfoByFrameId/{frameId}")
    public R<List<Bin>> getInfoByFrameId(@PathVariable("frameId") Long frameId);

    //根据跨类型获取所有库位
    @GetMapping(value = "/bin/selectBinVOByFrameType/{typeCode}")
    public R<List<BinVO>> selectBinVOByFrameType(@PathVariable("typeCode") String typeCode);

    @GetMapping(value = "/ware/{id}")
    public R<Ware> getWareInfo(@PathVariable("id") String id);

    @GetMapping(value = "/driver/black/{name}")
    public R<List<BlackDriverVO>> getInfoByName(@PathVariable("name") String name);
}