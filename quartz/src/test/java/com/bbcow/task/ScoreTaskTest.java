package com.bbcow.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by adan on 2017/11/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ScoreTaskTest {
    @Autowired
    ScoreTask scoreTask;

    @Test
    public void test(){
        scoreTask.pageScoreTask();
        System.out.println("---");
    }
}
