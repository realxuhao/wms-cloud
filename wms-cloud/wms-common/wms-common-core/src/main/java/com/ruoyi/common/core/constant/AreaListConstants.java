package com.ruoyi.common.core.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-05-31 12:30
 **/
public class AreaListConstants {

    public static final List<String> mainAreaList = Arrays.asList("0100", "0101", "0102", "0103");

    public static final List<String> noQualifiedAreaList = Arrays.asList("0005");


    public static boolean noQualifiedArea(String area) {
        for (int i = 0; i < noQualifiedAreaList.size(); i++) {
            if (area.contains(noQualifiedAreaList.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean mainArea(String area) {
        for (int i = 0; i < mainAreaList.size(); i++) {
            if (area.contains(mainAreaList.get(i))) {
                return true;
            }
        }
        return false;
    }

}
