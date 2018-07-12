package com.xijian.toutiao.bean;

import java.util.HashMap;

public class ViewObject {
    HashMap<String,Object> objs=new HashMap<String, Object>();
    public void set(String key,Object value){
        objs.put(key,value);
    }

    public Object get(String key){
        return objs.get(key);
    }
}
