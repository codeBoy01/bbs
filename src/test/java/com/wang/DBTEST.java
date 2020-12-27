package com.wang;

import com.wang.dao.MessageMapper;
import com.wang.pojo.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DBTEST {
    @Autowired
    private MessageMapper messageMapper;
    @Test
     void test1() {

        List<Message> list=messageMapper.queryMessageDel();
        for(Message m:list)
        {
            System.out.println(m);
        }




    }
}
