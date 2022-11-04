package com.ruoyi.common.core.constant;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.ApiException;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.Charset;
/**
 * 解决Feign的异常包装，统一返回结果
 * @author 公众号：JAVA日知录
 */
@Configuration
@Slf4j
public class OpenFeignErrorDecoder implements ErrorDecoder {
    /**
     * Feign异常解析
     * @param methodKey 方法名
     * @param response 响应体
     * @return BizException
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("feign client error,response is {}:",response);
        try {
            //获取数据
            String errorContent = IOUtils.toString(response.body().asInputStream());
            String body = Util.toString(response.body().asReader(Charset.defaultCharset()));

            R<?> resultData = JSON.parseObject(body,R.class);
            if(!resultData.isSuccess()){
                return new ServiceException(resultData.getMsg(),resultData.getCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ServiceException("Feign client 调用异常");
    }
}
