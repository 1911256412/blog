package com.he.controller;

import com.he.entity.LoginParams;
import com.he.entity.Result;
import com.he.service.LoginService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/login")
public class loginController {

    @Resource
    private LoginService loginservice;
    @PostMapping
    public Result login(@RequestBody LoginParams loginParams)  {

        return loginservice.login(loginParams);
    }


}
