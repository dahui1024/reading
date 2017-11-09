package com.bbcow.service;

import com.bbcow.service.mongo.entity.Crawler;
import com.bbcow.service.mongo.entity.Site;
import com.bbcow.service.mongo.entity.SiteElement;
import com.bbcow.service.mongo.reporitory.CrawlerRepository;
import com.bbcow.service.mongo.reporitory.SiteElementRepository;
import com.bbcow.service.mongo.reporitory.SiteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by adan on 2017/10/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("dev,service-dev")
public class CrawlerTest {
    @Autowired
    CrawlerRepository crawlerRepository;

    @Test
    public void siteElement(){
        Crawler crawler = new Crawler();
        crawler.setName("PageScoreProcessor");
        crawler.setStatus(1);
        crawlerRepository.save(crawler);
    }

}
