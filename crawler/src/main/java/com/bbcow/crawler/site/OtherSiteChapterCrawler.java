package com.bbcow.crawler.site;

import com.bbcow.crawler.TaskCrawler;
import com.bbcow.crawler.site.processor.OtherSiteChapterProcessor;
import com.bbcow.service.impl.BookService;
import com.bbcow.service.impl.BookSiteService;
import com.bbcow.service.impl.ScoreService;
import com.bbcow.service.mongo.entity.Book;
import com.bbcow.service.mongo.entity.ScoreBookLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;

/**
 * Created by adan on 2017/11/23.
 */
@Component
public class OtherSiteChapterCrawler extends TaskCrawler<OtherSiteChapterProcessor> {
    @Autowired
    BookService bookService;
    @Autowired
    ScoreService scoreService;
    StringRedisTemplate stringRedisTemplate;

    public OtherSiteChapterCrawler(@Autowired BookSiteService bookSiteService, @Autowired StringRedisTemplate stringRedisTemplate) {
        super(new OtherSiteChapterProcessor(bookSiteService, stringRedisTemplate));
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void execute() {
        String rk = stringRedisTemplate.opsForList().rightPop("site:queue:rk");

        if (rk == null){
            return;
        }

        Book book = bookService.getByReferenceKey(rk);

        ScoreBookLog scoreBookLog = scoreService.findTodayBookLog(book.getName());

        scoreBookLog.getUrls().forEach(url -> {
            Request request = new Request(url);
            request.addHeader("rk", rk);

            System.out.println(url);
            spider.addRequest(request);
        });

        spider.thread(4).start();
    }
}
