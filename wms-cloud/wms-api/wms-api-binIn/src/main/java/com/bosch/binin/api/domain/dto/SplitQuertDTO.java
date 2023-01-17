package com.bosch.binin.api.domain.dto;

import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-01-17 10:44
 **/
@Data
public class SplitQuertDTO {
    /**
     * wareCode: '',
     * ssccNb: '',
     * materialNb: '',
     * sourceSscc: '',
     * targetAreaCode: '',
     * targetBinCode: '',
     * sourceAreaCode: '',
     * sourceBinCode: ''
     */
    private String wareCode;
    private String ssccNb;
    private String materialNb;
    private String sourceSscc;
    private String targetAreaCode;
    private String targetBinCode;
    private String sourceAreaCode;
    private String sourceBinCode;


}
