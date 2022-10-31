package com.bosch.masterdata.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bosch.masterdata.api.enumeration.ClassType;

import com.bosch.storagein.api.domain.MaterialReceive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.core.exception.ServiceException;

/**
 * excel工具类
 * easyexcel使用的3.0.2版本，跟以前版本有很大区别，且不兼容1.x版本
 */
public class EasyExcelUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(EasyExcelUtil.class);

    public static <T> List<T> read(String filePath, final Class<?> clazz, String className) {
        File f = new File(filePath);
        try (FileInputStream fis = new FileInputStream(f)) {
            return read(fis, clazz, className);
        } catch (FileNotFoundException e) {
            LOGGER.error("文件{}不存在", filePath, e);
        } catch (IOException e) {
            LOGGER.error("文件读取出错", e);
        }
        return null;
    }

    public static <T> List<T> read(InputStream inputStream, final Class<?> clazz, String className) {
        if (inputStream == null) {
            throw new ServiceException("解析出错了，文件流是null");
        }
        // 有个很重要的点 DataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        DataListener<T> listener = new DataListener<>();
        ExcelReaderBuilder read = EasyExcel.read(inputStream, clazz, listener);
        read.sheet().headRowNumber(1).doRead();
        List<String> head = listener.getHead();
        boolean equals = head.equals(ClassType.getValue(className));
        if (!equals){
            throw new ServiceException("excel模板不正确");
        }
            // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
            //EasyExcel.read(inputStream, clazz, listener).sheet().doRead();
        return listener.getRows();
    }


    public static void write(String outFile, List<?> list) {
        Class<?> clazz = list.get(0).getClass();
        EasyExcel.write(outFile, clazz).sheet().doWrite(list);
    }

    public static void write(String outFile, List<?> list, String sheetName) {
        Class<?> clazz = list.get(0).getClass();
        EasyExcel.write(outFile, clazz).sheet(sheetName).doWrite(list);
    }

    public static void write(OutputStream outputStream, List<?> list, String sheetName) {
        Class<?> clazz = list.get(0).getClass();
        // sheetName为sheet的名字，默认写第一个sheet
        EasyExcel.write(outputStream, clazz).sheet(sheetName).doWrite(list);
    }

    /**
     * 文件下载（失败了会返回一个有部分数据的Excel），用于直接把excel返回到浏览器下载
     */
    public static void download(HttpServletResponse response, List<?> list, String sheetName) throws IOException {
        Class<?> clazz = list.get(0).getClass();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode(sheetName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), clazz).head(clazz).sheet(sheetName).doWrite(list);
    }

    public static<T> boolean  check(List<T> list) {
        List<String> strings=new ArrayList<>();
        list.forEach(r->{
            strings.add(JSON.toJSONString(r));
        });
        List<String> collect = strings.stream().distinct().collect(Collectors.toList());
        return collect.size()==list.size();
    }
}

