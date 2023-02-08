package com.bosch.masterdata.api;

import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.domain.dto.MaterialDTO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.factory.RemoteMaterialFallbackFactory;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.domain.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "remoteMaterialService", value = ServiceNameConstants.MASTER_DATA_SERVICE, fallbackFactory = RemoteMaterialFallbackFactory.class)
public interface RemoteMaterialService {


    @GetMapping(value = "/material/getByMaterialCode/{materialCode}")
    public R<MaterialVO> getInfoByMaterialCode(@PathVariable("materialCode") String materialCode);

    @GetMapping("/material/materialVOList")
    public R<PageVO<MaterialVO>> list(MaterialDTO materialDTO);


}
