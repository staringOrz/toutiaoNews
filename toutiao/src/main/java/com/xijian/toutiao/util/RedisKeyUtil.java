package com.xijian.toutiao.util;

import com.xijian.toutiao.aysnc.EventType;

import java.security.Key;

public class RedisKeyUtil {
    public static String SPLIT=":";
    public static String LIKE="LIKE";
    public static String DISLIKE="DISLIKE";
    public static String EVENT="ENVENT";
    public static String LOGIN_TICKET="LOGIN_TICKET";
    public static String  getLikeKey(int entityId,int entityTpye){
        return LIKE+SPLIT+ String.valueOf(entityId)+SPLIT+String.valueOf(entityTpye);
    }
    public static String  getDislikeKey(int entityId,int entityTpye){
        return DISLIKE+SPLIT+ String.valueOf(entityId)+SPLIT+String.valueOf(entityTpye);
    }
    public static String getEventQueueKey(){
        return EVENT;
    }
    public static String getLoginTicket(String ticket){
        return LOGIN_TICKET+SPLIT+ticket;
    }
}
