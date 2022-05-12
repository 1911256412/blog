package com.he.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.he.entity.SysUser;
import com.he.mapper.SysUserMapper;
import com.he.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysUserServiceImpl implements SysUserService {


    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser findAuthor(String  authorId) {

        return sysUserMapper.selectById(authorId);
    }

    @Override
    public SysUser findUser(String account, String password) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("account", account);
        wrapper.eq("password", password);
        wrapper.select("id", "account", "nickname", "avatar");

        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        return sysUser;
    }

    @Override
    public SysUser findUserByCount(String account) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getAccount, account);
        SysUser sysUser = this.sysUserMapper.selectOne(wrapper);
        return sysUser;
    }

    @Override
    public void createUser(SysUser sysUser) {
        this.sysUserMapper.insert(sysUser);
    }




}
