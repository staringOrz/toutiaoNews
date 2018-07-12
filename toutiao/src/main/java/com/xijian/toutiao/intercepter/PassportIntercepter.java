package com.xijian.toutiao.intercepter;

import com.alibaba.fastjson.JSON;
import com.xijian.toutiao.bean.HostHolder;
import com.xijian.toutiao.bean.LoginTicket;
import com.xijian.toutiao.bean.User;
import com.xijian.toutiao.dao.LoginTicketDAO;
import com.xijian.toutiao.dao.UserDAO;
import com.xijian.toutiao.util.JedisAdapter;
import com.xijian.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportIntercepter implements  HandlerInterceptor {
    @Autowired
    LoginTicketDAO loginTicketDAO;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserDAO userDAO;

    @Autowired
    JedisAdapter jedisAdapter;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket=null;
        if(request.getCookies()!=null){
            for(Cookie cookie:request.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket=cookie.getValue();
                    break;
                }
            }
        }
//        if(ticket!=null){
//            LoginTicket loginTicket=loginTicketDAO.selectByTicket(ticket);
//            if(loginTicket==null||loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!=0){
//                return true;
//            }
//            Date date=new Date();
//            date.setTime(date.getTime()+1000*3600*24*2);
//            loginTicketDAO.updateTicketExpired(loginTicket.getTicket(),date);
//            User user= userDAO.selectById(loginTicket.getUserId());
//            HostHolder.threadLocal.set(user);
//        }
        if(ticket!=null){
            String userTicket=jedisAdapter.sget(RedisKeyUtil.getLoginTicket(ticket));
            if(userTicket!=null){
                User user= JSON.parseObject(userTicket,User.class);
                HostHolder.threadLocal.set(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        if(modelAndView!=null&&hostHolder.getUser()!=null){
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        hostHolder.clear();
    }
}
