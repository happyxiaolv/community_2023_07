package com.example.community;


import org.junit.Test;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class QuartzTests {

    @Autowired
    private Scheduler scheduler;

//    @Test
//    public void testDelete(){
//        try {
//            boolean res = scheduler.deleteJob(new JobKey("alphaJob","alphaJobGroup"));
//            System.out.println(res);
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }
//    }
}
