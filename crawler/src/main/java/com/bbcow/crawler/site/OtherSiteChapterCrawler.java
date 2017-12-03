package com.bbcow.crawler.site;

import com.bbcow.crawler.TaskCrawler;
import com.bbcow.crawler.scheduler.DefaultScheduler;
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
        for (int i = 0; i < 20; i++) {
            String rk = stringRedisTemplate.opsForList().rightPop("site:queue:rk");
            try {
                stringRedisTemplate.delete("site:lock:"+rk);

                if (rk == null){
                    continue;
                }

                Book book = bookService.getByReferenceKey(rk);

                if (book == null){
                    continue;
                }
                ScoreBookLog scoreBookLog = scoreService.findTodayBookLog(book.getName());

                int size = scoreBookLog.getUrls().size() > 5 ? 5 : scoreBookLog.getUrls().size();

                for (int j = 0; j < size; j++) {
                    Request request = new Request(scoreBookLog.getUrls().get(i));
                    request.addHeader("rk", rk);
                    spider.addRequest(request);
                }

            }catch (Exception e){
                stringRedisTemplate.opsForList().leftPush("site:queue:rk", rk);
                stringRedisTemplate.opsForValue().set("site:lock:"+rk, "0");
            }
        }

        spider.setScheduler(new DefaultScheduler());

        spider.thread(4).start();
    }
}
