package com.b;

import com.bbcow.crawler.site.SpiderCrawler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CrawlerTest {
    @Autowired
    SpiderCrawler spiderCrawler;
    @Test
    public void test(){
        try {
            spiderCrawler.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
