package com.example.community.dao;

import com.example.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId,int offset,int limit,int orderMode);

    //@Param 注解用于给参数取别名
    //如果只有一个参数，并且在 <if>里使用，必须加别名
    int selectDiscussRows(@Param("userId") int userId);

    //发布帖子
    int insertDiscussPost(DiscussPost discussPost);

    //帖子详情
    DiscussPost findDiscussPostById(int id);

    //更新 添加帖子总数
    int updateComment(int id,int commentCount);

    int updateType(int id,int type);
    int updateStatus(int id,int status);
    int updateScore(int id,double score);


}
