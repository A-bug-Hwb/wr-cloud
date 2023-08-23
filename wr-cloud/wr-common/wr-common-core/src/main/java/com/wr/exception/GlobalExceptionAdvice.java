package com.wr.exception;

import com.wr.constants.HttpStatus;
import com.wr.result.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestControllerAdvice
@Slf4j
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
