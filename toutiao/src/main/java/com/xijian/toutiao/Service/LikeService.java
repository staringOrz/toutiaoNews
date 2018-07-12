package com.xijian.toutiao.Service;

import com.xijian.toutiao.util.JedisAdapter;
import com.xijian.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;
    public long getLikeStatus(int userId,int entityId,int entityType){
        String likeKey= RedisKeyUtil.getLikeKey(entityId,entityType);
        if(jedisAdapter.sismemeber(likeKey,String.valueOf(userId)))return 1;
        String disLikeKey= RedisKeyUtil.getDislikeKey(entityId,entityType);
        return jedisAdapter.sismemeber(disLikeKey,String.valueOf(userId))?-1:0;
    }
    public long like(int userId,int entityId,int entityType){
        String likeKey= RedisKeyUtil.getLikeKey(entityId,entityType);
        String dislikeKey= RedisKeyUtil.getDislikeKey(entityId,entityType);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));
        jedisAdapter.srem(dislikeKey,String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }
    public long disLike(int userId,int entityId,int entityType){
        String likeKey= RedisKeyUtil.getLikeKey(entityId,entityType);
        String dislikeKey= RedisKeyUtil.getDislikeKey(entityId,entityType);
        jedisAdapter.sadd(dislikeKey,String.valueOf(userId));
        jedisAdapter.srem(likeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }
}
