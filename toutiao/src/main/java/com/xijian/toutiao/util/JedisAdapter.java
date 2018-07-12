package com.xijian.toutiao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Component
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool jedisPool=null;
    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool=new JedisPool();
    }
    public long sadd(String key,String value){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }
    public boolean sismemeber(String key,String value){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return false;
    }
    public long scard(String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }
    public long srem(String key,String value){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }

    public long lpush(String key, String value) {
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.lpush(key,value);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }

    public List<String> brpop(int timeOut,String key) {
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.brpop(timeOut,key);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;
    }

    public void saddTicket(String loginTicket, String value) {
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
             jedis.set(loginTicket,value);
             jedis.expire(loginTicket,3600*24);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public String sget(String ticket) {
        Jedis jedis=null;
        String Userticket=null;
        try{
            jedis=jedisPool.getResource();
            Userticket= jedis.get(ticket);
            if(Userticket!=null)
            jedis.expire(ticket,3600*24);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return Userticket;
    }
    public void del(String ticket) {
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            jedis.del(ticket);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
}
