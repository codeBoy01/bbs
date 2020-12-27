package com.wang.dao;

import com.wang.pojo.Message;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface MessageMapper {
    //添加帖子
int insertMessage(Message message);
//查询帖子数量
Integer selectMessageNum(Map map);
//获取
List<Map> selectMessageList(Map map);
  //通过id获取
 Message selectMessage(Long msgId);
 //更新
 int updateMessage(HashMap hashMap);
//取出原文
  HashMap selectMessageMap(Integer id);
  //查询回帖列表
  List<Map> selectMessageReplyList(HashMap hashMap);
  //删除帖子，也就是更新message的Delref字段
    int delMessage(Long msgId);
    //返回delflag为1的帖子。
    List<Message> queryMessageDel();

    //真正地删除帖子
    int delMessage2(Long id);

}
