package com.example.community.dao;

import com.example.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    //通过id查找用户
    User selectById(int id);

    //通过name查找用户
    User selectByName(String username);

    //通过email查找用户
    User selectByEmail(String email);

    int insertUser(User user);

    //返回修改几条状态
    int updateStatus(int id,int status);

    int updateHeader(int id,String headerUrl);

    int updatePassword(int id,String password);

}
