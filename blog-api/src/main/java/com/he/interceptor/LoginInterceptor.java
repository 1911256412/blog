package com.he.interceptor;

import com.alibaba.fastjson.JSON;
import com.he.entity.Result;
import com.he.entity.SysUser;
import com.he.entity.UserThreadLoacl;
import com.he.entity.vo.ErrorCode;
import com.he.entity.vo.LoginUserVo;
import com.he.entity.vo.UserVo;
import com.he.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(!(handler instanceof HandlerMethod)) {
            //判断是否为resouce的拦截器
            return true;
        }
        String token = request.getHeader("Authorization");
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        if(token==null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return  false;
        }
        //检查token是否存在
        Result userInfoByToken = loginService.getUserInfoByToken(token);
        if(userInfoByToken.getData()==null){
            //等于空未登录
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        SysUser sysUser=new SysUser();
        BeanUtils.copyProperties(userInfoByToken.getData(),sysUser);
        UserThreadLoacl.put(sysUser);
        //登录状态放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
       //防止内存泄漏
        UserThreadLoacl.remove();
    }

}
