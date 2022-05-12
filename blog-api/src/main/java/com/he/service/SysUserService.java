package com.he.service;

import com.he.entity.SysUser;

public interface SysUserService  {
    SysUser findAuthor(String  authorId);

    SysUser findUser(String account, String password);

    SysUser findUserByCount(String account);

    void createUser(SysUser sysUser);


}
