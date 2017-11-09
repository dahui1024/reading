package com.bbcow.crawler;

import com.bbcow.crawler.book.FindNewCrawler;
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
public class FindNewCrawlerTest {
    @Autowired
    FindNewCrawler findNewCrawler;

    @Test
    public void run() throws Exception {
        findNewCrawler.execute();
    }
}
