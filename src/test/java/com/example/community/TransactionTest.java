package com.example.community;


import com.example.community.controller.AlphaController;
import com.example.community.service.AlphaService;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class TransactionTest {
    @Autowired
    private AlphaController alphaController;

    @Test
    public void test(){
        Object o = alphaController.save1();
        System.out.println(o);
    }


}
