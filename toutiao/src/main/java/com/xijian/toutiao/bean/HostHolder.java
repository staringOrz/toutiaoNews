package com.xijian.toutiao.bean;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    public static ThreadLocal<User> threadLocal=new ThreadLocal<User>();
    public User getUser(){
        return threadLocal.get();
    }
    public void setUser(User user){
     threadLocal.set(user);
    }
    public void clear(){
        threadLocal.remove();
    }
}
