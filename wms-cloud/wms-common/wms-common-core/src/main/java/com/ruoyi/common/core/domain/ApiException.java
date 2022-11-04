package com.ruoyi.common.core.domain;

import com.baomidou.mybatisplus.extension.api.IErrorCode;

public class ApiException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private long code;

    private String message;

    public ApiException(String message)
    {
        this.message = message;
    }

    public ApiException(String message, long code)
    {
        this.message = message;
        this.code = code;
    }

    public ApiException(long code, String message)
    {
        this.message = message;
        this.code = code;
    }


    public ApiException(String message, Throwable e)
    {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public long getCode()
    {
        return code;
    }
}

