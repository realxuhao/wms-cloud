package com.ruoyi.common.core.constant;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.ReturnCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {
    /**
     * 默认全局异常处理。
     * @param e the e
     * @return ResultData
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus
    public R<String> exception(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return R.fail(ReturnCode.RC500.getCode(), e.getMessage());
    }

}