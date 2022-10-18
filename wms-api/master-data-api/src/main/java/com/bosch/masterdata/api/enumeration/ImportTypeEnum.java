package com.bosch.masterdata.api.enumeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ImportTypeEnum {


    MATERIALDTO("MaterialDTO", new ArrayList<String>(Arrays.asList("物料名称","物料代码","物料类型","包装重量"
            ,"托盘重量","是否绑定托盘","是否带托盘","来料总重量","物料防错方式","允许负偏差比例","最小包装重量","最小包装数量","单位"))),
    MATERIALBINDTO("MaterialBinDTO", new ArrayList<String>(Arrays.asList())),
    SUPPLIERINFODTO("SupplierInfoDTO", new ArrayList<String>(Arrays.asList("供应商编码","供应商名称","供应商时间窗口"))),
    AREADTO("AreaDTO",new ArrayList<String>(Arrays.asList("仓库Code","存储区编码","存储区名称"))),
    FRAMEDTO("FrameDTO",new ArrayList<String>(Arrays.asList("区域Code","跨编码","跨名称","宽度","承重"))),
    BINDTO("BinDTO",new ArrayList<String>(Arrays.asList("跨Code","库位编码","库位名称")));

    private String desc;
    private List<String> strings;

    public static List<String>  getValue(String key) {
        for (ImportTypeEnum ele : values()) {
            if (ele.getDesc().equals(key)) {
                return ele.getCode();
            }
        }
        return null;
    }

    private ImportTypeEnum(String desc, List<String> strings) {
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
