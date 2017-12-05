package com.bbcow.service;

import com.bbcow.service.impl.ScoreService;
import com.bbcow.service.mongo.entity.*;
import com.bbcow.service.mongo.reporitory.*;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

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
    @Autowired
    ScoreBookRepository scoreBookRepository;
    @Autowired
    ScoreService scoreService;

    @Test
    public void siteElement(){
        List<ScoreBookLog> scoreBookLogs = scoreBookLogRepository.findTop30ByName("元尊", new Sort(Sort.Direction.DESC, "day"));

        scoreBookLogs.forEach(scoreBookLog -> System.out.println(scoreBookLog.getDay()));

    }
    @Test
    public void one(){
        Date date = DateUtils.truncate(new Date(), Calendar.DATE);
        List<ScoreBook> scoreBooks = scoreBookRepository.findTop250ByDay(date, new Sort(Sort.Direction.DESC, "score"));
        int score = 0;
        int count = 0;
        for (ScoreBook scoreBook : scoreBooks){
            score += scoreBook.getScore();
            count += scoreBook.getSiteScores().size();
        }
        System.out.println(score/250.0);
        System.out.println(count/250.0);
        System.out.println(score/1.0/count);
    }
    @Test
    public void sort(){
        List<ScoreSite> sites = scoreService.findEnableSite();
        Map<String, Integer> siteMap = new HashMap<>();
        sites.forEach(site -> siteMap.put(site.getHost(), site.getRank()));

        Date date = DateUtils.truncate(new Date(), Calendar.DATE);

        ScoreBook scoreBook = scoreBookRepository.findByNameAndDay("元尊", date);

        List<String> urls = scoreBook.getUrls();
        try {
            Collections.sort(urls, (c1, c2) -> {
                int rank1 = 0;
                int rank2 = 0;
                try {
                    rank1 = siteMap.containsKey(new URI(c1).getHost())?siteMap.get(new URI(c1).getHost()):0;
                    rank2 = siteMap.containsKey(new URI(c2).getHost())?siteMap.get(new URI(c2).getHost()):0;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                return rank2 - rank1;
            });


            urls.forEach(s -> System.out.println(s));
        }catch (Exception e){
//            e.printStackTrace();
        }
    }

}
