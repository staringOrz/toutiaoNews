package com.xijian.toutiao.controller;

import com.xijian.toutiao.Service.*;
import com.xijian.toutiao.bean.*;
import com.xijian.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class NewsController {

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    QinniuService qinniuService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @ResponseBody
    @RequestMapping(path = {"/image"},method={RequestMethod.GET})
    public void downloadImage(@RequestParam("name") String imageName, HttpServletResponse response){
        try{
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new
                    File(ToutiaoUtil.IMAGE_DIR + imageName)), response.getOutputStream());
        }catch (Exception e){
            logger.error("读取图片错误！"+e.getMessage());
        }
    }
    @ResponseBody
    @RequestMapping(path = {"/uploadImage/"},method={RequestMethod.POST})
    public String uploadImage(@RequestParam("file") MultipartFile file){
        try{
            String fileUrl= qinniuService.saveImage(file);
            if(fileUrl==null){
                return ToutiaoUtil.getJSONString(1,"上传图片失败！");
            }
            return  ToutiaoUtil.getJSONString(0,fileUrl);
        }catch (Exception e){
            logger.error("上传图片失败");
            return ToutiaoUtil.getJSONString(1,"上传图片失败");
        }
    }


    @RequestMapping(path={"/news/{newsId}"},method={RequestMethod.GET,RequestMethod.POST})
    public String newsDetail( @PathVariable("newsId") int newsId,Model model){
        News news=newsService.getNewsById(newsId);
        if(news!=null){
            int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
            if (localUserId != 0) {
                model.addAttribute("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            } else {
                model.addAttribute("like", 0);
            }

            List<Comment> comments=commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOs=new ArrayList<ViewObject>();
            for(Comment comment:comments){
                ViewObject vo=new ViewObject();
                vo.set("comment",comment);
                vo.set("user",userService.getUserById(comment.getUserId()));
                commentVOs.add(vo);
            }
            model.addAttribute("comments",commentVOs);
        }
        model.addAttribute("news",news);
        model.addAttribute("owner",userService.getUserById(news.getUserId()));
        return "detail";
    }

    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,@RequestParam("content") String content){
        try {
            content = HtmlUtils.htmlEscape(content);
            // 过滤content
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);

            commentService.addComment(comment);
            // 更新news里的评论数量
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(), count);
            // 怎么异步化
        } catch (Exception e) {
            logger.error("增加评论失败" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }
    @ResponseBody
    @RequestMapping(path = {"/user/addNews/"},method={RequestMethod.POST})
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link){
        try{
                News news=new News();
                news.setTitle(title);
                news.setLink(link);
                news.setCreatedDate(new Date());
                if(hostHolder.getUser()!=null)
                news.setUserId(hostHolder.getUser().getId());
                else news.setUserId(1);//匿名用户
                news.setImage(image);
                newsService.addNews(news);
        }catch (Exception e){
            logger.error("资讯添加失败！"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"发布失败!");
        }
        return ToutiaoUtil.getJSONString(0,"发布成功!");
    }
}
