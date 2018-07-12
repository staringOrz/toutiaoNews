package com.xijian.toutiao.controller;

import com.xijian.toutiao.Service.LikeService;
import com.xijian.toutiao.Service.NewsService;
import com.xijian.toutiao.Service.UserService;
import com.xijian.toutiao.bean.EntityType;
import com.xijian.toutiao.bean.HostHolder;
import com.xijian.toutiao.bean.News;
import com.xijian.toutiao.bean.ViewObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;


    private List<ViewObject> getNews(int userId, int offset, int limit){
        List<News> newsList = newsService.getLatesNews(userId, offset, limit);
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<ViewObject> vos = new ArrayList<>();
        for (News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUserById(news.getUserId()));
            if (localUserId != 0) {
                vo.set("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            } else {
                vo.set("like", 0);
            }
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path={"/","/index"},method={RequestMethod.GET,RequestMethod.POST})
    public String index(Model model){
        model.addAttribute("vos",getNews(0,0,10));
        return "home";
    }

    @RequestMapping(path={"/user/{userId}"},method={RequestMethod.GET,RequestMethod.POST})
    public String userindex(Model model, @PathVariable("userId") int userid ,
                            @RequestParam(value = "pop" , defaultValue="0") int pop){
        model.addAttribute("vos",getNews(userid,0,10));
        model.addAttribute("pop",pop);
        return "home";
    }
}
