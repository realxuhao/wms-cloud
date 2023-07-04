package com.bosch.masterdata.api.factory;

import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.RemotePalletService;
import com.bosch.masterdata.api.RemoteProductService;
import com.bosch.masterdata.api.domain.dto.MaterialDTO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.api.domain.vo.MdProductPackagingVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.ruoyi.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-20 11:04
 **/
@Component
public class RemoteProductFallbackFactory implements FallbackFactory<RemoteProductService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteProductFallbackFactory.class);

    @Override
    public RemoteProductService create(Throwable cause) {
        log.error("主数据服务调用失败:{}", cause.getMessage());
        return new RemoteProductService() {


            @Override
            public R<List<MdProductPackagingVO>> getByCell(String cell) {
                return null;
            }

            @Override
            public R<MdProductPackagingVO> getByCode(String code) {
                return null;
            }
        };
    }
}
