package com.he.service;

import com.he.entity.LoginParams;
import com.he.entity.Result;

public interface LoginService {
    Result login(LoginParams loginParams);

    Result getUserInfoByToken(String token);

    Result logout(String token);

    Result regist(LoginParams loginParams);
}
