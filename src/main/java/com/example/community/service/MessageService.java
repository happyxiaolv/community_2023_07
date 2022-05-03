package com.example.community.service;

import com.example.community.dao.MessageMapper;
import com.example.community.entity.Message;
import com.example.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    //查询当前用户的会话
    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }
    //
    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    //查 某会话所含的私信
    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }
    //会话包含几条私信
    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }
    //可以查询 所有会话的未读Id + 具体某个会话的未读Id
    public int findLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insert(message);
    }

    //更新messge状态 置为已读
    public int readMessage(List<Integer> ids){
        return messageMapper.updateStatus(ids,1);
    }

    /*显示系统消息*/
    //查询某个主题下最新的通知
    public Message findLatestNotice(int userId,String topic){
        return messageMapper.selectLatestNotice(userId,topic);
    }
    //查询某个主题所含的通知数量
    public int findNoticeCount(int userId,String topic){
        return messageMapper.selectNoticeCount(userId,topic);
    }
    //查询未读的通知的数量
    public int findNoticeUnreadCount(int userId,String topic){
        return messageMapper.selectNoticeUnreadCount(userId,topic);
    }
    public List<Message> findNotices(int userId,String topic,int offset,int limit){
        return messageMapper.selectNotices(userId, topic, offset, limit);
    }



}
