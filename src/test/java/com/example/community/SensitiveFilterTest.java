package com.example.community;


import com.example.community.util.SensitiveFilter;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;



@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveFilterTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testsensitve(){
        String text="快来暗网，直播杀人，赌博啦!";
        String content = sensitiveFilter.filter(text);
        System.out.println(content);
    }
}
