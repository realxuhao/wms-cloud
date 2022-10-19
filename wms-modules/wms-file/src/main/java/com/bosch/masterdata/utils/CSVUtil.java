package com.bosch.masterdata.utils;

import com.alibaba.fastjson2.JSON;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.storagein.api.domain.MaterialReceive;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvException;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.security.utils.SecurityUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CSVUtil {
    /**
     * 获取CSV文件数据
     * @param clazz 按文件标题映射的类，属性使用注解@CsvBindByName(column = "xxx")
     * @return 返回解析后的数据集合
     * @param <T>
     */
    public static <T> List<T> getCsvData(InputStream inputStream, Class<T> clazz) {
        InputStreamReader in;
        CsvToBean<T> csvToBean;
        try {

            ByteArrayOutputStream  baos=cloneInputStream(inputStream);
            // 打开两个新的输入流
            InputStream stream1 = new ByteArrayInputStream(baos.toByteArray());
            InputStream stream2 = new ByteArrayInputStream(baos.toByteArray());

            CSVReader reader1 = new CSVReader(new InputStreamReader(stream1));
            String[] strings = reader1.readAll().get(0);
            boolean equals = compare(Arrays.asList(strings).stream().filter(string -> !string.isEmpty()).collect(Collectors.toList()), ClassType.getValue("MaterialReceive"));
            if (!equals){
                throw new ServiceException("excel模板不正确");
            }
            //in = new InputStreamReader(Files.newInputStream(Paths.get(filePath)), StandardCharsets.UTF_8);
            in = new InputStreamReader(stream2, StandardCharsets.UTF_8);
            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(clazz);
            csvToBean = new CsvToBeanBuilder<T>(in).withMappingStrategy(strategy).build();
        } catch (Exception e) {
            return null;
        }
        return csvToBean.parse();
    }

    public static boolean validate(InputStream inputStream)  {
        List<String> list=new ArrayList<>();
        try {

            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            String[] strings = reader.readAll().get(0);
            list = Arrays.asList(strings);
        }catch (Exception e){
            return false;
        }
        boolean equals = compare(list,ClassType.getValue("MaterialReceive"));
        return equals;
    }
    private static ByteArrayOutputStream cloneInputStream(InputStream input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean compare(List<String> a, List<String> b) {
        if(a.size() != b.size())
            return false;
//        Collections.sort(a);
//        Collections.sort(b);
        for(int i=0;i<a.size();i++){
            String replace = a.get(i).replace("\uFEFF", "");
            if(!replace.equals(b.get(i)))
                return false;
        }
        return true;
    }


    public static boolean check(List<MaterialReceive> list) {
       List<String> strings=new ArrayList<>();
       list.forEach(r->{
           strings.add(JSON.toJSONString(r));
       });
        List<String> collect = strings.stream().distinct().collect(Collectors.toList());
        return collect.size()==list.size();
    }
}

