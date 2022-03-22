package com.he.service.serviceImpl;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.he.entity.LoginParams;
import com.he.entity.Result;
import com.he.entity.SysUser;
import com.he.entity.vo.ErrorCode;
import com.he.entity.vo.LoginUserVo;
import com.he.service.LoginService;
import com.he.service.SysUserService;
import com.he.utils.JWTUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    private static final String slat = "mszlu!@#";
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SysUserService sysUserService;

    public Result login(LoginParams loginParams) {

        //如果用户名和密码都没有信息 返回错误代码，
        if (StringUtils.isEmpty(loginParams.getAccount()) || StringUtils.isEmpty(loginParams.getPassword())) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        String password = DigestUtils.md5Hex(loginParams.getPassword() + slat);
        //根据用户名密码查询用户是否存在
        SysUser sysUser = sysUserService.findUser(loginParams.getAccount(), password);
        if (sysUser == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        //如果不为空生成token值
        String token = JWTUtils.createToken(sysUser.getId());
        //把用户信息和token的值存到redis中
        redisTemplate.opsForValue().set("TOKEN" + token, JSON.toJSON(sysUser), 1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public Result getUserInfoByToken(String token) {

        Map<String, Object> map = JWTUtils.checkToken(token);

        if (map == null) {
            return Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
        }
        Object userjons =  redisTemplate.opsForValue().get("TOKEN" + token);

        if (StringUtils.isEmpty(userjons)) {
            return Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
        }
        String userjson=userjons.toString();
        SysUser sysUser = JSON.parseObject(userjson, SysUser.class);
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());
        return Result.success(loginUserVo);
    }

    @Override
    public Result logout(String token) {

        redisTemplate.delete("TOKEN"+token);
        return Result.success(null);
    }

    @Override
    public Result regist(LoginParams loginParams) {
        //判断是否为空
        if (StringUtils.isEmpty(loginParams.getAccount()) || StringUtils.isEmpty(loginParams.getPassword()) || StringUtils.isEmpty(loginParams.getNickname())) {
           return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //判断是否重复
        SysUser sysUser=sysUserService.findUserByCount(loginParams.getAccount());
        if(sysUser!=null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        //如果不重复，注册用户
        sysUser = new SysUser();
        sysUser.setNickname(loginParams.getNickname());
        sysUser.setAccount(loginParams.getAccount());
        sysUser.setPassword(DigestUtils.md5Hex(loginParams.getPassword()+slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        sysUserService.createUser(sysUser);
        //根据id创建token
        String token = JWTUtils.createToken(sysUser.getId());
        //把token存到数据库中
        redisTemplate.opsForValue().set("TOKEN"+token,JSON.toJSONString(sysUser),1,TimeUnit.DAYS);
        return Result.success(token);
    }
}
