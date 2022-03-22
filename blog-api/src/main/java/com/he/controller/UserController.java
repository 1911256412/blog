package com.he.controller;

import com.he.entity.Result;
import com.he.service.LoginService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    private LoginService loginservice;
    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
        return loginservice.getUserInfoByToken(token);
    }
}
