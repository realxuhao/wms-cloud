package com.bosch.masterdata.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.file.api.domain.FileUpload;


public interface IFileUploadService extends IService<FileUpload> {

    int saveFile(FileUpload fileUpload);
}
