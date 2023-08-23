package com.wr.exception;

import com.wr.constants.HttpStatus;
import com.wr.result.AjaxResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return AjaxResult.error(HttpStatus.BAD_REQUEST, "参数异常：" + e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public AjaxResult serviceException(ServiceException e) {
        if (e.getCode() != null){
            return AjaxResult.error(e.getCode(), e.getMessage());
        }
        return AjaxResult.error(HttpStatus.ERROR, e.getMessage());
    }
}
