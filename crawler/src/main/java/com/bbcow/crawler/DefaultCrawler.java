package com.bbcow.crawler;

import com.bbcow.service.impl.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.UUID;

/**
 * Created by adan on 2017/11/8.
 */
public abstract class DefaultCrawler<T extends PageProcessor> implements CommandLineRunner {
    protected PageProcessor pageProcessor;
    protected OOSpider spider;
    private String uuid;

    @Autowired
    CrawlerService crawlerService;

    public DefaultCrawler(T t){
        this.pageProcessor = t;
        this.uuid = UUID.randomUUID().toString();
        spider = new OOSpider(t);
    }

    @Scheduled(cron = "0 * * * * ?")
    public void register(){
        crawlerService.update(uuid, pageProcessor.getClass().getSimpleName(), spider.getStatus().name());
    }
}
