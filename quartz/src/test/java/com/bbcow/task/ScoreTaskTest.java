package com.bbcow.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    @Test
    public void testSort(){
        List<Integer> scores = new ArrayList<>();

        scores.add(9);
        scores.add(3);
        scores.add(6);
        scores.add(0);
        scores.add(2);
        scores.add(8);
        scores.add(9);

        Collections.sort(scores, (s1,s2) -> s2-s1);


        System.out.println(scores.get(scores.size() / 2 - 1));

    }
}
