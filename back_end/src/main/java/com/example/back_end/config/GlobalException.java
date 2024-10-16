package com.example.back_end.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    /**
     * 全局异常处理
     * 此处处理所有Exception
     */

    @ExceptionHandler(Exception.class)
    public Result exceptionHandle() {
        return new Result(Code.CODE_SYSTEM_ERROR, "系统错误", null);
    }
}
