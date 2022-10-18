package com.bosch.masterdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.file.api.domain.FileUpload;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 上传文件Mapper接口
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@Mapper
@Repository("fileUploadMapper")
public interface FileUploadMapper extends BaseMapper<FileUpload>
{
    public int saveFile(FileUpload fileUpload);
}
