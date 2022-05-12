package com.he.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.he.entity.Admin;
import com.he.entity.Permission;
import com.he.mapper.AdminMapper;
import com.he.mapper.PermissionMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Slf4j
public class AuthService {

    @Resource
    private AdminMapper adminMapper;
    @Resource
    private PermissionMapper permissionMapper;

    public boolean auth(HttpServletRequest request, Authentication authentication) {
        String requestURI = request.getRequestURI();
        log.info("request url:{}", requestURI);
        //得到用户
        Object principal = authentication.getPrincipal();
        //判断是否登录
        if (principal == null || "anonymousUser".equals(principal)) {
            return false;
        }
        //用户名
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        //根据用户名查询用户 ，得到用户id从关联表中查询
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, username);
        Admin admin = adminMapper.selectOne(wrapper);
        //如果没查询到用户 信息 返回
        if (admin == null) {
            return false;
        }
        if (admin.getId() == 1) {
            //认为是超级管理员
            return true;
        }
        //根据用户id信息查询权限,用sql语句
        List<Permission> permissionList=permissionMapper.selectPermission(admin.getId());
        //保证肯定取到不带参数的部分,去除括号
        requestURI = StringUtils.split(requestURI,'?')[0];
        for (Permission permission : permissionList) {
                if(permission.getPath().equals(requestURI)){
                    return true;
                }
        }
        return false;
    }
}
