package com.bosch.masterdata.controller;

import com.bosch.masterdata.domain.dto.WareDTO;
import com.bosch.masterdata.utils.EasyExcelUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.file.FileUtils;
import com.bosch.masterdata.service.ISysFileService;
import com.bosch.system.api.domain.SysFile;

import java.io.IOException;
import java.util.List;

/**
 * 文件请求处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/file")
public class SysFileController
{
    private static final Logger log = LoggerFactory.getLogger(SysFileController.class);

    @Autowired
    private ISysFileService sysFileService;

    /**
     * 文件上传请求
     */
    @PostMapping("upload")
    public R<SysFile> upload(MultipartFile file)
    {
        try
        {
            // 上传并返回访问地址
            String url = sysFileService.uploadFile(file);
            SysFile sysFile = new SysFile();
            sysFile.setName(FileUtils.getName(url));
            sysFile.setUrl(url);
            return R.ok(sysFile);
        }
        catch (Exception e)
        {
            log.error("上传文件失败", e);
            return R.fail(e.getMessage());
        }
    }


    @ApiOperation("读取出库excel表")
    @PostMapping(value = "/importExcel", headers = "content-type=multipart/form-data")
    public R importExcelSubject(@RequestParam(value = "file", required = true)MultipartFile file) throws IOException {
        List<WareDTO> list = EasyExcelUtil.read(file.getInputStream(),
                WareDTO.class);
        //outEvalDeclareService.saveBatchTopic(list);
        return R.ok(list);
    }
    /**
     * 解析文件
     *
     * @param file 文件信息
     * @return 结果
     */
    @ApiOperation("解析excel表")
    @PostMapping(value = "/read")
    public <T> R<List<T>> read(@RequestPart(value = "file") MultipartFile file,@RequestParam(value = "className") String className)throws Exception{

        Class<?> TClass = Class.forName("com.bosch.masterdata.domain.dto."+className);
        List<T> read = EasyExcelUtil.read(file.getInputStream(), TClass);
        return R.ok(read);
    }
}