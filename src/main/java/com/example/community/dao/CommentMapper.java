package com.example.community.dao;

import com.example.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<Comment> selectCommentByEntity(int entityType,int entityId,int offset,int limit);

    int countComment(int entityType,int entityId);

    //增加评论
    int insertComment(Comment comment);

    Comment findCommentById(int id);

}
