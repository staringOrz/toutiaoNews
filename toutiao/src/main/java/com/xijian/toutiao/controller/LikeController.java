package com.xijian.toutiao.controller;

import com.xijian.toutiao.Service.LikeService;
import com.xijian.toutiao.Service.NewsService;
import com.xijian.toutiao.aysnc.EventModel;
import com.xijian.toutiao.aysnc.EventProducer;
import com.xijian.toutiao.aysnc.EventType;
import com.xijian.toutiao.bean.EntityType;
import com.xijian.toutiao.bean.HostHolder;
import com.xijian.toutiao.bean.News;
import com.xijian.toutiao.bean.User;
import com.xijian.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class LikeController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;
    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(Model model , @RequestParam("newsId") int newsId,
                           HttpServletResponse response){
        User user=hostHolder.getUser();
        if(user==null){
            return ToutiaoUtil.getJSONString(1,"请登录后点赞！");
        }
        int userId=hostHolder.getUser().getId();
        long  likeCount=likeService.like(userId, EntityType.ENTITY_NEWS,newsService.getNewsById(newsId).getId());
        News news=newsService.getNewsById(newsId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE).
                setActorId(userId).setEntityId(newsId).
                setEntityType(EntityType.ENTITY_NEWS).
                setEntityOwnerId(news.getUserId()).
                setExt("newsId",String.valueOf(newsId))
                .setExt("likeCount",String.valueOf(likeCount)));
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String disLike(Model model , @RequestParam("newsId") int newsId,
                           HttpServletResponse response){
        User user=hostHolder.getUser();
        if(user==null){
            return ToutiaoUtil.getJSONString(1,"请登录后点赞！");
        }
        int userId=hostHolder.getUser().getId();
        long  likeCount=likeService.disLike(userId, EntityType.ENTITY_NEWS,newsService.getNewsById(newsId).getId());
        newsService.updateLikeCount(newsId,likeCount);
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }
}
