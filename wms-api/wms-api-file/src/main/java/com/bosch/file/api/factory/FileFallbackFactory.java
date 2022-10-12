package com.bosch.file.api.factory;


import com.bosch.file.api.FileService;
import com.ruoyi.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件服务降级处理
 *
 * @author ruoyi
 */
@Component
public class FileFallbackFactory implements FallbackFactory<FileService> {
    private static final Logger log = LoggerFactory.getLogger(FileFallbackFactory.class);

    @Override
    public FileService create(Throwable cause) {
        return new FileService() {
            @Override
            public <T> R<List<T>> masterDataImport(MultipartFile file, String className) {
               return R.fail("主数据文件服务调用失败");
            }

            @Override
            public <T> R<List<T>> materialReceiveImport(MultipartFile file, String className) {
                return R.fail("入库文件服务调用失败");
            }
        };
    }
}
