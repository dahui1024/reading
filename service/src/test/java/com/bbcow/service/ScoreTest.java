package com.bbcow.service;

import com.bbcow.service.impl.ScoreService;
import com.bbcow.service.mongo.entity.ScoreBookLog;
import com.bbcow.service.mongo.entity.Site;
import com.bbcow.service.mongo.entity.SiteElement;
import com.bbcow.service.mongo.reporitory.ScoreBookLogRepository;
import com.bbcow.service.mongo.reporitory.ScoreSiteRepository;
import com.bbcow.service.mongo.reporitory.SiteElementRepository;
import com.bbcow.service.mongo.reporitory.SiteRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by adan on 2017/10/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootApplication
@ActiveProfiles("service-dev")
public class ScoreTest {
    @Autowired
    ScoreBookLogRepository scoreBookLogRepository;
    @Autowired
    ScoreSiteRepository scoreSiteRepository;

    @Test
    public void siteElement(){
        List<ScoreBookLog> scoreBookLogs = scoreBookLogRepository.findTop30ByName("元尊", new Sort(Sort.Direction.DESC, "day"));

        scoreBookLogs.forEach(scoreBookLog -> System.out.println(scoreBookLog.getDay()));

    }
    @Test
    public void one(){
        Date date = DateUtils.truncate(new Date(), Calendar.DATE);
        System.out.println(date);
        System.out.println(scoreSiteRepository.findByStatusAndCrawlTimeGreaterThan(1, date).size());
    }

}
