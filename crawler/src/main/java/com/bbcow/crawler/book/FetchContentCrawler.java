package com.bbcow.crawler.book;

import com.bbcow.crawler.TaskCrawler;
import com.bbcow.crawler.book.processor.FetchContentProcessor;
import com.bbcow.service.impl.BookService;
import com.bbcow.service.mongo.entity.BookChapter;
import com.bbcow.service.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class FetchContentCrawler extends TaskCrawler<FetchContentProcessor> {
    @Autowired
    BookService bookService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public FetchContentCrawler(@Autowired FetchContentProcessor fetchContentProcessor) {
        super(fetchContentProcessor);
    }

    @Override
    public void execute() {
        for (int i = 0; i < 8; i++) {
            String rk = stringRedisTemplate.opsForList().rightPop("queue:rk");
            if (rk == null){
                break;
            }
            List<BookChapter> bookChapters = bookService.getBookChapters(rk);
            bookChapters.forEach(bookChapter -> {
                if (bookChapter != null){
                    spider.addUrl(bookChapter.getUrl());
                }
            });
            stringRedisTemplate.opsForValue().increment("counter:rk:"+rk, bookChapters.size());
        }

        spider.thread(4).start();
    }

}
