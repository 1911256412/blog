package com.he.controller;

import com.he.entity.LoginParams;
import com.he.entity.Result;
import com.he.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/register")
public class RegistController {

    @Resource
    private LoginService loginService;
    @PostMapping
    public Result register(@RequestBody  LoginParams loginParams){
        return loginService.regist(loginParams);
    }
}
