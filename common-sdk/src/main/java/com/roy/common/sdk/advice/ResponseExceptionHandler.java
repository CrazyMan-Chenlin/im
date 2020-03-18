package com.roy.common.sdk.advice;

import com.roy.common.sdk.enums.ResultCode;
import com.roy.common.sdk.exception.ErrorResult;
import com.roy.common.sdk.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author chenlin
 */
@ControllerAdvice
@RestController
@Slf4j
public class ResponseExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result handlerException(Exception e, HttpServletRequest request) {
        log.error("出现异常, 请求的url为{}", request.getRequestURI(), e);
        //处理参数校验的异常
        if (e instanceof MethodArgumentNotValidException){
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            ObjectError objectError = bindingResult.getAllErrors().get(0);
            return Result.failure(ResultCode.SERVER_ERROR,objectError.getDefaultMessage());
        }
        if (!(e instanceof ErrorResult)){
            return Result.failure(ResultCode.SERVER_ERROR);
        }
        return Result.failure(((ErrorResult) e).getResultCode());
    }
}