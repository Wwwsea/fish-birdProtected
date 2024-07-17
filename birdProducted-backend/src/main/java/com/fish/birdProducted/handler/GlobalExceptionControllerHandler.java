package com.fish.birdProducted.handler;

import com.fish.birdProducted.domain.response.ResponseResult;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.fish.birdProducted.enums.RespEnum;

import java.util.Objects;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/20 9:14
 * 全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionControllerHandler {

    /**
     * 统一处理参数校验异常
     * @param e 异常
     * @return 响应结果
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResult<Void> handlerConstraintViolationException(ConstraintViolationException e){
        log.error("参数校验异常:{}({})", e.getMessage(), e.getStackTrace());
        return ResponseResult.failure(RespEnum.PARAM_ERROR.getCode(), e.getMessage().split(":")[1]);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<Void> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("参数校验异常:{}({})", e.getMessage(), e.getStackTrace());
        BindingResult bindingResult = e.getBindingResult();
        return ResponseResult.failure(RespEnum.PARAM_ERROR.getCode(), Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }

    // 最大的异常，防止出现其他不明异常无法处理
//    @ExceptionHandler(Exception.class)
//    public ResponseResult<Void> handlerException(Exception e){
//        log.error("系统异常:{}，异常:{}",e.getMessage(),e.getStackTrace());
//        return ResponseResult.failure(RespEnum.OTHER_ERROR.getCode(), e.getMessage());
//    }
}
