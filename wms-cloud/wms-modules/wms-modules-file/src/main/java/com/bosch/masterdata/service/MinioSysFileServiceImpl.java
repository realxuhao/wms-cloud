package com.bosch.masterdata.service;

import io.minio.DownloadObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.http.Method;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.bosch.masterdata.config.MinioConfig;
import com.bosch.masterdata.utils.FileUploadUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

/**
 * Minio 文件存储
 * 
 * @author ruoyi
 */
@Service
public class MinioSysFileServiceImpl implements ISysFileService
{
    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient client;

    /**
     * 本地文件上传接口
     * 
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception
    {
        String fileName = FileUploadUtils.extractFilename(file);
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        client.putObject(args);
        return minioConfig.getUrl() + "/" + minioConfig.getBucketName() + "/" + fileName;
    }

    @Override
    @SneakyThrows
    public String downloadObject(String fileName){
        DownloadObjectArgs.Builder builder = DownloadObjectArgs.builder();

        builder.bucket(minioConfig.getBucketName());
        builder.object(fileName);
        builder.filename(minioConfig.getDownloadPath()+fileName);
        DownloadObjectArgs downloadObjectArgs = builder.build();

        // 指定一个GET请求，返回获取文件对象的URL，此URL过期时间为一天
        String url =
                client.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(minioConfig.getBucketName())
                                .object(minioConfig.getDownloadPath()+fileName)
                                .expiry(60 * 60 * 24)
                                .build());

        String replacement = "https://www.nutricia-home.com/templateExcel/wms";

        String replacedString = url.replaceFirst(".*/wms", replacement);

        return replacedString;
    }

}
