package com.bbcow.service;

import com.bbcow.service.impl.ScoreService;
import com.bbcow.service.mongo.entity.ScoreBookLog;
import com.bbcow.service.mongo.entity.Site;
import com.bbcow.service.mongo.entity.SiteElement;
import com.bbcow.service.mongo.reporitory.ScoreBookLogRepository;
import com.bbcow.service.mongo.reporitory.SiteElementRepository;
import com.bbcow.service.mongo.reporitory.SiteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * Created by adan on 2017/10/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ScoreBookLogRepository.class})
@ActiveProfiles("service-dev")
public class ScoreTest {
    @Autowired
    ScoreBookLogRepository scoreBookLogRepository;

    @Test
    public void siteElement(){
        List<ScoreBookLog> scoreBookLogs = scoreBookLogRepository.findNewly30ByName("元尊");

        System.out.println(scoreBookLogs.size());
    }

}
