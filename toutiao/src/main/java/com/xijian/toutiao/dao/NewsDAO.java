package com.xijian.toutiao.dao;

import com.xijian.toutiao.bean.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NewsDAO {
    String TABLE_NAME ="news";
    String INSERT_FIELDS="title,link,image,like_count,comment_count,user_id,created_date";
    String SELECT_FIELDS ="id, "+INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,
            ") values(#{title},#{link},#{image},#{likeCount},#{commentCount},#{userId},#{createdDate})"})
    int InsertNews(News news);


    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset")int offset, @Param("limit")int limit);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME ,"where id=#{newsId}"})
    News getNewsById(int newsId);

    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    @Update({"update ", TABLE_NAME, " set like_count = #{LikeCount} where id=#{id}"})
    int updateLikeCount(@Param("id") int id, @Param("LikeCount") int LikeCount);

    @Select({"select user_id from ",TABLE_NAME ,"where id=#{newsId}"})
    String getUserId(int newsId);
}
