package com.example.community.dao;

import com.example.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId,int offset,int limit);

    //@Param 注解用于给参数取别名
    //如果只有一个参数，并且在 <if>里使用，必须加别名
    int selectDiscussRows(@Param("userId") int userId);
}
