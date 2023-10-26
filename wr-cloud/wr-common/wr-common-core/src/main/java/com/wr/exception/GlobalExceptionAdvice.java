package com.wr.exception;

import com.wr.constants.HttpStatus;
import com.wr.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return R.fail(HttpStatus.BAD_REQUEST, "参数异常：" + e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public R serviceException(ServiceException e) {
        if (e.getCode() != null){
            return R.fail(e.getCode(), e.getMessage());
        }
        return R.fail(HttpStatus.ERROR, e.getMessage());
    }
}
