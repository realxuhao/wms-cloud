package com.bosch.masterdata.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.file.api.domain.FileUpload;

import com.bosch.masterdata.mapper.FileUploadMapper;
import com.bosch.masterdata.service.IFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileUploadServiceImpl extends ServiceImpl<FileUploadMapper, FileUpload> implements IFileUploadService {
    @Autowired
    private FileUploadMapper fileUploadMapper;

    @Override
    public int saveFile(FileUpload fileUpload){

        int i = fileUploadMapper.saveFile(fileUpload);

        return i;
    }
}
