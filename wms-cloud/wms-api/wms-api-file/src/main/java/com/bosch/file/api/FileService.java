package com.bosch.file.api;

import com.bosch.file.api.factory.FileFallbackFactory;
import com.bosch.file.api.factory.StockFeignServiceFallback;
import com.bosch.system.api.domain.SysFile;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@FeignClient(contextId = "masterFileService", name = ServiceNameConstants.FILE_SERVICE,fallback = FileFallbackFactory.class)
public interface FileService {

    @PostMapping(value = "/file/masterDataImport",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public <T> R<List<T>> masterDataImport(@RequestPart(value = "file") MultipartFile file, @RequestParam(value = "className") String className);

    @PostMapping(value = "/file/IQCDataImport",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public <T> R<List<T>> IQCDataImport(@RequestPart(value = "file") MultipartFile file, @RequestParam(value = "className") String className);

    @PostMapping(value = "/file/materialReceiveImport",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public <T> R<List<T>> materialReceiveImport(@RequestPart(value = "file") MultipartFile file,@RequestParam(value = "className") String className);

    @PostMapping(value = "/file/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<SysFile> upload(@RequestPart(value = "file") MultipartFile file);

    @PostMapping(value = "/file/download")
    public R download(@RequestParam(value = "fileName") String fileName);

    @PostMapping(value = "/file/materialCallImport",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public <T> R<List<T>> materialCallImport(@RequestPart(value = "file") MultipartFile file,@RequestParam(value = "className") String className);

}
