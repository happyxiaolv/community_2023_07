package com.example.community.util;


public interface CommunityConstant {
    //激活成功
    int ACTIVATION_SUCCESS=0;
    //重复激活
    int ACTIVATION_REPEAT=1;
    //激活失败
    int ACTIVATION_FAILURE=2;

    //默认凭证超时时间(3600*12) 12H 为方便调试 暂时设置5min
    int DEFAULT_EXPIRED_SECONDS=60*5;
    //rememberme (3600*24*100)
    int REMEMBER_EXPIRED_SECONDS=60*10;

    //实体类型:帖子
    int ENTITY_TYPE_POST=1;
    //实体类型：评论
    int ENTITY_TYPE_COMMENT=2;
    //用户
    int ENTITY_TYPE_USER=3;

    /*主题：评论*/
    String TOPIC_COMMENT="comment";
    /*主题:点赞*/
    String TOPIC_LIKE="like";
    /*主题:关注*/
    String TOPIC_FOLLOW="follow";
    /*主题:发帖*/
    String TOPIC_PUBLISH="publish";
    /*主题:删帖*/
    String TOPIC_DELETE="delete";

    /*權限：普通用戶*/
    String AUTHORITY_USER="user";
    /*權限：管理員*/
    String AUTHORITY_ADMIN="admin";
    /*權限：版主*/
    String AUTHORITY_MODERATOR="moderator";


}
