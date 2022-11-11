package com.bosch.binin.api.enumeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum BinInFileTypeEnum {

    MATERIALCALL("MaterialCallDTO", new ArrayList<String>(Arrays.asList("订单号","物料代码","物料名称","数量","单位","备注")));

    private String desc;
    private List<String> strings;

    public static List<String>  getValue(String key) {
        for (BinInFileTypeEnum ele : values()) {
            if (ele.getDesc().equals(key)) {
                return ele.getCode();
            }
        }
        return null;
    }

    private BinInFileTypeEnum(String desc, List<String> strings) {
        this.desc = desc;
        this.strings = strings;
    }

    public String getDesc() {
        return this.desc;
    }

    public List<String> getCode() {
        return this.strings;
    }
}
