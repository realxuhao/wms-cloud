package com.bosch.masterdata.utils;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
            //in = new InputStreamReader(Files.newInputStream(Paths.get(filePath)), StandardCharsets.UTF_8);
            in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(clazz);
            csvToBean = new CsvToBeanBuilder<T>(in).withMappingStrategy(strategy).build();
        } catch (Exception e) {
            return null;
        }
        return csvToBean.parse();
    }
}
