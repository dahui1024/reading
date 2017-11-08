package com.bbcow.crawler;

import org.springframework.scheduling.annotation.Scheduled;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by adan on 2017/11/8.
 */
public abstract class TaskCrawler<T extends PageProcessor> {
    protected PageProcessor pageProcessor;
    protected OOSpider spider;

    public TaskCrawler(T t){
        this.pageProcessor = t;
        spider = new OOSpider(t);
    }

    public abstract void execute();

    @Scheduled(cron = "0 * * * * ?")
    public void register(){
        System.out.println("======");
    }
}
