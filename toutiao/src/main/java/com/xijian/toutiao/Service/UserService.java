package com.xijian.toutiao.Service;

import com.alibaba.fastjson.JSON;
import com.xijian.toutiao.bean.LoginTicket;
import com.xijian.toutiao.bean.User;
import com.xijian.toutiao.dao.LoginTicketDAO;
import com.xijian.toutiao.dao.UserDAO;
import com.xijian.toutiao.util.JedisAdapter;
import com.xijian.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.util.*;

import static com.xijian.toutiao.util.ToutiaoUtil.MD5;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    LoginTicketDAO loginTicketDAO;
    public Map<String,Object> register(String username, String password){
        Map<String,Object> map=new HashMap<String, Object>();
        if(StringUtils.isBlank(username)) {
            map.put("msgname","用户名不能为空！");
            return map;
        }

        if(StringUtils.isBlank(password)) {
            map.put("msgpwd","密码不能为空！");
            return map;
        }

        User user=userDAO.selectByName(username);
        if(user!=null){
            map.put("msgname","用户已经被注册！");
            return map;
        }
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(MD5(password+user.getSalt()));
        userDAO.addUser(user);

        String ticket=addLoginTicket(user);
        map.put("ticket",ticket);
        return map;
    }
    public Map<String,Object> login(String username,String password){
        Map<String,Object> map=new HashMap<String, Object>();
        if(StringUtils.isBlank(username)) {
            map.put("msgname","用户名不能为空！");
            return map;
        }

        if(StringUtils.isBlank(password)) {
            map.put("msgpwd","密码不能为空！");
            return map;
        }

        User user=userDAO.selectByName(username);
        if(!MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd","用户密码错误！");
            return map;
        }

        String ticket=addLoginTicket(user);
        map.put("ticket",ticket);
        return map;
    }

//    private String addLoginTicket(int userId){
//        LoginTicket loginTicket=new LoginTicket();
//        loginTicket.setUserId(userId);
//        Date date=new Date();
//        date.setTime(date.getTime()+1000*3600*24*2);
//        loginTicket.setExpired(date);
//        loginTicket.setStatus(0);
//        loginTicket.setTicket(UUID.randomUUID().toString().replace("-",""));
//        loginTicketDAO.addTicket(loginTicket);
//
//        return loginTicket.getTicket();
//    }

    private String addLoginTicket(User user){
        String ticket= UUID.randomUUID().toString().replace("-","");
        jedisAdapter.saddTicket(RedisKeyUtil.getLoginTicket(ticket), JSON.toJSONString(user));
        return ticket;
    }
    public User getUserById(int UserId){
        return userDAO.selectById(UserId);
    }

    public void logout(String ticket) {
        jedisAdapter.del(RedisKeyUtil.getLoginTicket(ticket));
    }
}
