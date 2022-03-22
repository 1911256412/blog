package com.he.entity;


public class UserThreadLoacl {

    //私有化构造方法
    private UserThreadLoacl() {

    }
    private static final  ThreadLocal<SysUser> LOCAL=new ThreadLocal<>();

    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }
    public static SysUser get(){
       return LOCAL.get();
    }
    public static void remove(){
        LOCAL.remove();
    }
}
