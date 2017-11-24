package com.bbcow.crawler.book;

import com.bbcow.crawler.TaskCrawler;
import com.bbcow.crawler.book.processor.FetchChapterProcessor;
import com.bbcow.service.impl.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class FetchChapterCrawler extends TaskCrawler<FetchChapterProcessor> {
    @Autowired
    BookService bookService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    public FetchChapterCrawler(@Autowired FetchChapterProcessor fetchChapterProcessor) {
        super(fetchChapterProcessor);
    }

    @Override
    public void execute() {
        bookService.getNewBookChapterUrl().forEach(url -> {
            if (url.getChapterUrl() != null && !stringRedisTemplate.opsForHash().hasKey("filter:rk", url.getReferenceKey())){
                spider.addUrl(url.getChapterUrl());
            }
        });

        spider.thread(4).start();
    }

    public static void main(String[] args) {
        FetchChapterCrawler fetchChapterCrawler = new FetchChapterCrawler(new FetchChapterProcessor(null));

        fetchChapterCrawler.execute();
    }
}
