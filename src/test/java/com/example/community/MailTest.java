package com.example.community;

import com.example.community.util.MailClient;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {

    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testEmail(){
        mailClient.sendEmail("1372076247@qq.com","TEST","Welcome.");
    }

    @Test void test(){
        Context context=new Context();
        context.setVariable("username","sunny");
        String content = templateEngine.process("/mail/demo",context);
        System.out.println(content);
        mailClient.sendEmail("1372076247@qq.com","HTML",content);

    }
}
