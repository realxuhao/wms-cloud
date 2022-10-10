package com.bosch.file.api;

import com.bosch.file.api.factory.FileFallbackFactory;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(contextId = "masterFileService", name = ServiceNameConstants.FILE_SERVICE)
public interface FileService {

    @PostMapping(value = "/file/read",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public <T> R<List<T>> read(@RequestPart(value = "file") MultipartFile file,@RequestParam(value = "className") String className);
}
