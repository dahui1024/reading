package com.bbcow.crawler.site;

import com.bbcow.crawler.TaskCrawler;
import com.bbcow.crawler.scheduler.DefaultScheduler;
import com.bbcow.crawler.site.processor.SiteChapterProcessor;
import com.bbcow.service.impl.BookService;
import com.bbcow.service.impl.BookSiteService;
import com.google.common.io.Files;
import com.google.common.io.LineReader;
import com.google.common.io.Resources;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

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
        spider.setScheduler(new DefaultScheduler());
//        spider.addUrl("https://book.qidian.com/info/1004650252#Catalog");
//        spider.addUrl("http://www.77xs.co/book_41575/");
//        spider.addUrl("http://yc.ireader.com.cn/book/67409/chapters/");

//        spider.addUrl("https://m.qidian.com/book/1003723851/catalog");

        spider.thread(8).start();
    }

    public static void main(String[] args) {
        URL url = Resources.getResource("js/book.js");
        System.out.println(url.getPath());

        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("/Users/adan/Downloads/phantomjs-2.1.1-macosx/bin/phantomjs "+url.getPath()+" https://book.qidian.com/info/2750457#Catalog");
            InputStream inputStream = process.getInputStream();
            List<String> lines = IOUtils.readLines(inputStream);
            lines.forEach(line -> System.out.println(line));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
