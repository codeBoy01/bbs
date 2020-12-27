package com.wang.dao;

import com.wang.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Mapper
@Repository
public interface UserMapper {
 //添加用户
 int addUser(User user);
 //搜索用户，核对账户密码
 User selectUser(String name);
 int updateUserImage(HashMap hashMap);
}
