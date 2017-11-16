package com.bbcow.crawler.score;

import com.bbcow.crawler.TaskCrawler;
import com.bbcow.crawler.scheduler.DefaultScheduler;
import com.bbcow.crawler.score.processor.FindSiteProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class FindSiteCrawler extends TaskCrawler<FindSiteProcessor> {

    public FindSiteCrawler(@Autowired FindSiteProcessor findSiteProcessor) {
        super(findSiteProcessor);
    }

    @Override
    public void execute() {
        spider.setScheduler(new DefaultScheduler());

        spider.addUrl("http://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E9%A3%9E%E5%89%91%E9%97%AE%E9%81%93&oq=%25E9%25A3%259E%25E5%2589%2591%25E9%2597%25AE%25E9%2581%2593&rsv_pq=8dfca9430000d33d&rsv_t=0a45PWCQEkZD67b%2B%2Bh61PYoy8e%2FmetjqCFLmsWXxdi5X6ruZ0u6TF13ChW4&rqlang=cn&rsv_enter=0");
        spider.thread(1).start();
    }
}
