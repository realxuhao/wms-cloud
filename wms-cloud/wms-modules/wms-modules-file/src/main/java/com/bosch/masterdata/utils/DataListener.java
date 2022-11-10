package com.bosch.masterdata.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Slf4j
public class DataListener<T> extends AnalysisEventListener<T> {

    public final List<T> importList = new ArrayList<>();
    public  Map<Integer, String> headMap=new HashMap<>();

    @Override
    public void invoke(T data, AnalysisContext context) {
        log.info("解析到的一条数据: excelRow = {}", data);
        importList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 解析完所有excel行, 保存到数据库或进行业务处理
        log.info("解析的所有数据 list = {}", importList);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("表头数据 excelHead= {}", headMap);
        this.headMap=headMap;
    }

    public List<T> getRows() {
        return importList;
    }
    public List<String> getHead() {

        return new ArrayList<>(headMap.values());
    }
}
