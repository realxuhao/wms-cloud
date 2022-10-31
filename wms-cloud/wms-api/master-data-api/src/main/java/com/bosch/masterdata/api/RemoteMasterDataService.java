package com.bosch.masterdata.api;

import com.bosch.masterdata.api.domain.Bin;
import com.bosch.masterdata.api.domain.vo.BinVO;
import com.bosch.masterdata.api.domain.vo.FrameVO;
import com.bosch.masterdata.api.domain.vo.MaterialBinVO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
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

    @GetMapping(value = "/frame/getFrameInfo/{id}")
    public R<FrameVO> getFrameInfo(@PathVariable("id") Long id);

    @GetMapping(value = "/frame/getFrameInfoByCode/{code}")
    public R<FrameVO> getFrameInfoByCode(@PathVariable("code") String code);

    @GetMapping(value = "/bin/getInfoByFrameId/{frameId}")
    public R<List<Bin> > getInfoByFrameId(@PathVariable("frameId") Long frameId);
}