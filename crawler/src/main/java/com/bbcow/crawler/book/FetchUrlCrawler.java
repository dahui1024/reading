package com.bbcow.crawler.book;

import com.bbcow.crawler.TaskCrawler;
import com.bbcow.crawler.book.processor.FetchUrlProcessor;
import com.bbcow.service.impl.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class FetchUrlCrawler extends TaskCrawler<FetchUrlProcessor> {
    @Autowired
    SiteService siteService;

    public FetchUrlCrawler(FetchUrlProcessor fetchUrlProcessor) {
        super(fetchUrlProcessor);
    }

    @Override
    public void execute() {
        siteService.load().forEach(site -> {
            spider.addUrl(site.getStartUrl());
        });
        spider.thread(1).start();
    }

}
