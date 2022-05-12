package com.he.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.he.entity.Admin;
import com.he.mapper.AdminMapper;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityUserService implements UserDetailsService {

    @Resource
    private AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //查询数据库
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("username", s);
        Admin admin = adminMapper.selectOne(wrapper);
        if (admin == null) {
            throw new UsernameNotFoundException("用户名密码错误");
        };
        List<GrantedAuthority> authorities = new ArrayList<>();
        UserDetails userDetails = new User(admin.getUsername(), admin.getPassword(), authorities);
        //剩下的有框架帮我们完成
        return userDetails;
    }
}
