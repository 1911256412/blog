package com.he.hander;

import com.he.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GolabException {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result Exception(Exception ex) {
        ex.printStackTrace();
        return Result.fail(201, "系统异常");
    }

}
