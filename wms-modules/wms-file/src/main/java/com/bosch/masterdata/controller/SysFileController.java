package com.bosch.masterdata.controller;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bosch.file.api.domain.FileUpload;
import com.bosch.masterdata.service.IFileUploadService;
import com.bosch.masterdata.utils.CSVUtil;
import com.bosch.masterdata.utils.EasyExcelUtil;
import com.bosch.storagein.api.domain.MaterialReceive;
import com.bosch.storagein.api.enumeration.ClassType;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.file.FileUtils;
import com.bosch.masterdata.service.ISysFileService;
import com.bosch.system.api.domain.SysFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件请求处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/file")
public class SysFileController {
    private static final Logger log = LoggerFactory.getLogger(SysFileController.class);

    @Resource(name = "minioSysFileServiceImpl")
    private ISysFileService sysFileService;
    @Autowired
    private IFileUploadService fileUploadService;

    /**
     * 文件上传请求
     */
    @PostMapping("upload")
    public R<SysFile> upload(MultipartFile file) {
        try {
            // 上传并返回访问地址
            String url = sysFileService.uploadFile(file);
            SysFile sysFile = new SysFile();
            sysFile.setName(FileUtils.getName(url));
            sysFile.setUrl(url);
            FileUpload fileUpload = new FileUpload();
            fileUpload.setFileName(sysFile.getName());
            fileUpload.setBusinessType("");
            fileUpload.setFileUrl(sysFile.getUrl());
            fileUpload.setCreateBy(SecurityUtils.getUsername());
            fileUpload.setCreateTime(DateUtils.getNowDate());
            fileUploadService.saveFile(fileUpload);
            Long id = fileUpload.getId();
            sysFile.setFileId(id);
            return R.ok(sysFile);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return R.fail(e.getMessage());
        }
    }

    /**
     * 主数据解析文件
     *
     * @param file 文件信息
     * @return 结果
     */
    @ApiOperation("解析excel表")
    @PostMapping(value = "/masterDataImport")
    public <T> R<List<T>> masterDataImport(@RequestPart(value = "file") MultipartFile file, @RequestParam(value =
            "className") String className) throws Exception {
        try {
            Class<?> TClass = Class.forName("com.bosch.masterdata.api.domain.dto." + className);
            List<T> read = EasyExcelUtil.read(file.getInputStream(), TClass,className);
            boolean check = EasyExcelUtil.check(read);
            if (!check){
                return R.fail("excel中存在重复数据");
            }
            if(CollectionUtils.isEmpty(read)){
                return R.fail("excel中无数据");
            }
            return R.ok(read);
        } catch (Exception e) {
            if(e.getMessage()=="excel模板不正确"){
                return R.fail(e.getMessage());
            }
            return R.fail("解析文件失败,文件类型不匹配");
        }

    }

    /**
     * 入库解析csv文件
     *
     * @param file 文件信息
     * @return 结果
     */
    @ApiOperation("解析csv表")
    @PostMapping(value = "/materialcsvReceiveImport")
    public R<List<MaterialReceive>> materialcsvReceiveImport(@RequestPart(value = "file") MultipartFile file,
                                                          @RequestParam(value = "className") String className) throws Exception {

        try {

            List<MaterialReceive> csvData = CSVUtil.getCsvData(file.getInputStream(), MaterialReceive.class);
            boolean check = CSVUtil.check(csvData);
            if (!check){
                return R.fail("excel中存在重复数据");
            }

            R<SysFile> upload = upload(file);
            csvData.forEach(r->r.setFileId(upload.getData().getFileId().toString()));
            return R.ok(csvData);
        } catch (Exception e) {
            return R.fail("解析文件失败,文件类型不匹配");
        }

    }


    /**
     * 入库解析文件
     *
     * @param file 文件信息
     * @return 结果
     */
    @ApiOperation("解析excel表")
    @PostMapping(value = "/materialReceiveImport")
    public R<List<MaterialReceive>> materialReceiveImport(@RequestPart(value = "file") MultipartFile file,
                                                          @RequestParam(value = "className") String className) throws Exception {

        try {
            Class<?> TClass = Class.forName("com.bosch.storagein.api.domain." + className);
            List<MaterialReceive> read = EasyExcelUtil.read(file.getInputStream(), MaterialReceive.class,className);

            if(CollectionUtils.isEmpty(read)){
                return R.fail("excel中无数据");
            }
            boolean check = EasyExcelUtil.check(read);
            if (!check){
                return R.fail("excel中存在重复数据");
            }
            boolean checkssccNumber = EasyExcelUtil.check(read.stream().map(MaterialReceive::getSsccNumber).collect(Collectors.toList()));
            if (!checkssccNumber){
                return R.fail("excel中存在重复ssccNumber");
            }
            R<SysFile> upload = upload(file);
            if (!upload.isSuccess()){
                return R.fail("上传文件服务失败");
            }
            read.forEach(r->r.setFileId(upload.getData().getFileId().toString()));
            return R.ok(read);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }

    }


    @ApiOperation("download表")
    @PostMapping(value = "/download")
    public R download(@RequestBody String params) throws Exception {
        try {
            JSONObject paramsJSONObject = JSONObject.parseObject(params);
            String fileName = paramsJSONObject.getString("fileName");
            if (fileName == null) {
                return R.fail("参数为null");
            }
            String fileUrl = sysFileService.downloadObject(fileName);
            
            return R.ok(fileUrl);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }

    }
}