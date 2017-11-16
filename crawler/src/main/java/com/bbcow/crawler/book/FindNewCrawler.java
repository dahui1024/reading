package com.bbcow.crawler.book;

import com.bbcow.crawler.TaskCrawler;
import com.bbcow.crawler.book.processor.FindNewProcessor;
import com.bbcow.crawler.scheduler.DefaultScheduler;
import com.bbcow.service.impl.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class FindNewCrawler extends TaskCrawler<FindNewProcessor> {
    @Autowired
    SiteService siteService;

    public FindNewCrawler(FindNewProcessor findNewProcessor) {
        super(findNewProcessor);
    }

    @Override
    public void execute() {
        spider.setScheduler(new DefaultScheduler());
        siteService.loadFindUrl().forEach(s -> spider.addUrl(s));

        spider.thread(1).start();
    }
}
