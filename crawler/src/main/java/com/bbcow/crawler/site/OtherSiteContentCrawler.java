package com.bbcow.crawler.site;

import com.bbcow.crawler.TaskCrawler;
import com.bbcow.crawler.scheduler.DefaultScheduler;
import com.bbcow.crawler.site.processor.OtherSiteChapterProcessor;
import com.bbcow.crawler.site.processor.OtherSiteContentProcessor;
import com.bbcow.service.impl.BookService;
import com.bbcow.service.impl.BookSiteService;
import com.bbcow.service.impl.ScoreService;
import com.bbcow.service.mongo.entity.Book;
import com.bbcow.service.mongo.entity.BookSiteChapter;
import com.bbcow.service.mongo.entity.ScoreBookLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;

/**
 * Created by adan on 2017/11/23.
 */
@Component
public class OtherSiteContentCrawler extends TaskCrawler<OtherSiteContentProcessor> {
    @Autowired
    BookService bookService;
    BookSiteService bookSiteService;
    StringRedisTemplate stringRedisTemplate;

    public OtherSiteContentCrawler(@Autowired BookSiteService bookSiteService, @Autowired StringRedisTemplate stringRedisTemplate) {
        super(new OtherSiteContentProcessor(bookSiteService, stringRedisTemplate));
        this.stringRedisTemplate = stringRedisTemplate;
        this.bookSiteService = bookSiteService;
    }

    @Override
    public void execute() {
        for (int i = 0; i < 100; i++) {
            String id = stringRedisTemplate.opsForList().rightPop("chapter:queue:id");
            try {
                stringRedisTemplate.delete("chapter:lock:"+id);

                if (id == null){
                    return;
                }
                BookSiteChapter bookSiteChapter = bookSiteService.findByIdAndStatus(id);
                if (bookSiteChapter == null){
                    continue;
                }

                bookSiteChapter.getSiteUrls().forEach(url -> {
                    Request request = new Request(url);
                    request.addHeader("id", id);
                    spider.addRequest(request);
                });
            }catch (Exception e){
                stringRedisTemplate.opsForList().leftPush("chapter:queue:id", id);
                stringRedisTemplate.opsForValue().set("chapter:lock:" + id, "0");
            }
        }

        spider.setScheduler(new DefaultScheduler());

        spider.thread(4).start();
    }

    public static void main(String[] args) {
        OtherSiteContentCrawler otherSiteContentCrawler = new OtherSiteContentCrawler(null, null);
        Request request = new Request("http://www.166xs.com/xiaoshuo/105/105004/31927354.html");
        request.addHeader("rk", "960c6ded0a55b72b");

        otherSiteContentCrawler.spider.addRequest(request);
        otherSiteContentCrawler.execute();
    }
}
