package com.example.community.service;

import com.example.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/*
 *   点赞业务模块
 *     1.点赞:支持对帖子、评论点赞
 *     2.首页点赞数量  统计帖子的点赞数量
 *     3.详情页点赞数量 统计点赞数量 显示点赞状态
 *
 *     重构点赞功能  以用户为key,记录点赞数量 increment(key),decrement(key)
 *     开发个人主页  以用户为key，查询点赞数量
 */

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    //点赞   第一次点赞，第二次取消点赞
    public void like(int userId,int entityType,int entityId,int entityUserId){
//        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
//        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
//        if(isMember){   //集合中已经存在，取消点赞
//            redisTemplate.opsForSet().remove(entityLikeKey,userId);
//        }else{
//            redisTemplate.opsForSet().add(entityLikeKey,userId);
//        }

        //两次更新操作，需使用事务操作
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.geteUserLikeKey(entityUserId);
                Boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);

                operations.multi();
                if(isMember){   //已经存在了
                    operations.opsForSet().remove(entityLikeKey,userId);
                    operations.opsForValue().decrement(userLikeKey);
                }else{
                    operations.opsForSet().add(entityLikeKey,userId);
                    operations.opsForValue().increment(userLikeKey);
                }
                return operations.exec();
            }
        });
    }

    //查询实体点赞的数量
    public long findEntityLikeCount(int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //查询某人对实体的点赞状态 返回的int值 而不是布尔值是因为 后期可能会扩展，比如踩一下
    public int findjEntityLikeStatus(int userId,int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId)?1:0;
    }

    //查询某个用户获得的赞
    public int findUserLikeCount(int userId){
        String userLikeKey = RedisKeyUtil.geteUserLikeKey(userId);
        Integer count= (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count==null?0:count.intValue();
    }



}
