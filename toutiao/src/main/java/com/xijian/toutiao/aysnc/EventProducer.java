package com.xijian.toutiao.aysnc;

import com.alibaba.fastjson.JSONObject;
import com.xijian.toutiao.util.JedisAdapter;
import com.xijian.toutiao.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);
    public boolean fireEvent(EventModel eventModel){
        String json= JSONObject.toJSONString(eventModel);
        String key = RedisKeyUtil.getEventQueueKey();
        try{
            jedisAdapter.lpush(key,json);
            return true;
        }catch (Exception e){
            logger.error("事件插入失败！"+e.getMessage());
            return false;
        }
    }
}
