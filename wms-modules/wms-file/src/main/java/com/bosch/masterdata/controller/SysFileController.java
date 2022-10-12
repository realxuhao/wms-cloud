package com.bosch.masterdata.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.fastjson2.JSON;
import com.bosch.masterdata.utils.CSVUtil;
import com.bosch.masterdata.utils.EasyExcelUtil;
import com.bosch.storagein.api.domain.MaterialReceive;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件请求处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/file")
public class SysFileController {
    private static final Logger log = LoggerFactory.getLogger(SysFileController.class);

    @Autowired
    private ISysFileService sysFileService;

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
            List<T> read = EasyExcelUtil.read(file.getInputStream(), TClass);
            return R.ok(read);
        } catch (Exception e) {
            return R.fail("解析文件失败");
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
            List<MaterialReceive> csvData = CSVUtil.getCsvData(file.getInputStream(), MaterialReceive.class);
            return R.ok(csvData);
        } catch (Exception e) {
            return R.fail("解析文件失败");
        }

    }
}