package com.example.community.event;

import com.alibaba.fastjson.JSONObject;
import com.example.community.entity.DiscussPost;
import com.example.community.entity.Event;
import com.example.community.entity.Message;
import com.example.community.service.DiscussPostService;
import com.example.community.service.ElasticsearchService;
import com.example.community.service.MessageService;
import com.example.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class EventConsumer implements CommunityConstant {
    //记录日志
    private static final Logger logger= LoggerFactory.getLogger(EventConsumer.class);
    @Autowired
    private MessageService messageService;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private ElasticsearchService elasticsearchService;

    //消费 评论、关注、点赞
    @KafkaListener(topics = {TOPIC_COMMENT,TOPIC_FOLLOW,TOPIC_LIKE})
    public void handleCommentMessage(ConsumerRecord record){
        if(record==null||record.value()==null){
            logger.error("消息的内容为空");
            return ;
        }
        Event event= JSONObject.parseObject(record.value().toString(),Event.class);
        if(event==null){
            logger.error("消息格式错误");
            return ;
        }
        //发送站内通知
        Message message=new Message();
        //系统消息 ，规定都从SYSTEM_USER_ID 发送userId=1
        message.setFromId(1);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());
        //将想要添加的其他信息 ，封装为map集合 作为内容插入到数据库中去
        Map<String,Object> content=new HashMap<>();
        content.put("userId",event.getUserId());
        content.put("entityType",event.getEntityType());
        content.put("entityId",event.getEntityId());
        if(!event.getData().isEmpty()){
            for(Map.Entry<String,Object> entry:event.getData().entrySet()){
                content.put(entry.getKey(),entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }

    //消费发帖事件
    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(ConsumerRecord record){
        if(record==null||record.value()==null){
            logger.error("消息的内容为空");
            return ;
        }
        Event event= JSONObject.parseObject(record.value().toString(),Event.class);
        if(event==null){
            logger.error("消息格式错误");
            return ;
        }
        DiscussPost post=discussPostService.findDiscussPostById(event.getEntityId());
        elasticsearchService.saveDiscussPost(post);
        System.out.println("已经消费发帖事件");
    }

    //消费删帖事件
    @KafkaListener(topics = {TOPIC_DELETE})
    public void handleDeleteMessage(ConsumerRecord record){
        if(record==null||record.value()==null){
            logger.error("消息的内容为空");
            return ;
        }
        Event event= JSONObject.parseObject(record.value().toString(),Event.class);
        if(event==null){
            logger.error("消息格式错误");
            return ;
        }
        DiscussPost post=discussPostService.findDiscussPostById(event.getEntityId());
        elasticsearchService.deleteDiscussPost(event.getEntityId());
    }

}
