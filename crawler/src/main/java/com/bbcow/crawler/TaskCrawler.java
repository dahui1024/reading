package com.bbcow.crawler;

import com.bbcow.service.impl.CrawlerService;
import com.bbcow.service.mongo.entity.CrawlerLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.UUID;

/**
 * Created by adan on 2017/11/8.
 */
public abstract class TaskCrawler<T extends PageProcessor> {
    protected PageProcessor pageProcessor;
    protected OOSpider spider;
    private String uuid;

    @Autowired
    CrawlerService crawlerService;

    public TaskCrawler(T t){
        this.pageProcessor = t;
        this.uuid = UUID.randomUUID().toString();
        spider = new OOSpider(t);
    }

    public abstract void execute();

    @Scheduled(cron = "30 0/5 * * * ?")
    public void task(){
        if (spider.getStatus() == Spider.Status.Running){
            return;
        }
        if (crawlerService.getCrawler(pageProcessor.getClass().getSimpleName()).getStatus() == 0){
            return;
        }
        execute();
    }

    @Scheduled(cron = "0 * * * * ?")
    public void register(){
        crawlerService.update(uuid, pageProcessor.getClass().getSimpleName(), spider.getStatus().name());
    }
}
