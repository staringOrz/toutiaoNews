package com.xijian.toutiao.Service;

import com.xijian.toutiao.bean.News;
import com.xijian.toutiao.dao.NewsDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    @Autowired
    NewsDAO newsDAO;
    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);
    public List<News> getLatesNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public void addNews(News news) {
        newsDAO.InsertNews(news);
    }

    public News getNewsById(int newsId) {
        return newsDAO.getNewsById(newsId);
    }

    public String getUserId(int newsId) {
        return newsDAO.getUserId(newsId);
    }

    public int updateCommentCount(int id, int count) {
        return newsDAO.updateCommentCount(id, count);
    }

    public void updateLikeCount(int newId, long likeCount) {
        newsDAO.updateLikeCount(newId,(int)likeCount);
    }
//        public String saveImage(MultipartFile file) throws Exception{
//        int dopPos=file.getOriginalFilename().lastIndexOf(".");
//        if(dopPos<0){
//            return  null;
//        }
//        String fileExt =file.getOriginalFilename().substring(dopPos+1);
//        if(!ToutiaoUtil.IsFileAllowed(fileExt.toLowerCase())){
//            return null;
//        }
//        String fileName = UUID.randomUUID().toString().replace("-","")+"."+fileExt;
//        Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_DIR+fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
//
//        return ToutiaoUtil.TOUTIAN_DOMAIN+"image/?name="+fileName;
//    }
}
