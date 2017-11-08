package com.bbcow.crawler;

import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by adan on 2017/11/8.
 */
public abstract class DefaultCrawler<T extends PageProcessor> implements CommandLineRunner {
    protected PageProcessor pageProcessor;
    protected OOSpider spider;

    public DefaultCrawler(T t){
        this.pageProcessor = t;
        spider = new OOSpider(t);
    }

    @Scheduled(cron = "0 * * * * ?")
    public void register(){
        System.out.println("======");
    }
}
