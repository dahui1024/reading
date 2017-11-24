package com.bbcow.crawler.site;

import com.bbcow.crawler.TaskCrawler;
import com.bbcow.crawler.site.processor.SiteChapterProcessor;
import com.bbcow.service.impl.BookService;
import com.bbcow.service.impl.BookSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by adan on 2017/11/23.
 */
@Component
public class SiteChapterCrawler extends TaskCrawler<SiteChapterProcessor> {
    @Autowired
    BookService bookService;

    public SiteChapterCrawler(@Autowired BookSiteService bookSiteService, @Autowired StringRedisTemplate stringRedisTemplate) {
        super(new SiteChapterProcessor(bookSiteService, stringRedisTemplate));
    }

    @Override
    public void execute() {
        bookService.getHotBookUrl().forEach(bookUrl -> {
            spider.addUrl(bookUrl.getChapterUrl());
        });
//        spider.addUrl("https://book.qidian.com/info/1004650252#Catalog");
//        spider.addUrl("http://www.77xs.co/book_41575/");
//        spider.addUrl("http://yc.ireader.com.cn/book/67409/chapters/");

//        spider.addUrl("https://m.qidian.com/book/1003723851/catalog");

        spider.thread(4).start();
    }
}
