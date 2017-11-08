package com.bbcow.crawler.site;

import com.bbcow.crawler.CrawlerProperties;
import com.bbcow.crawler.proxy.FindProxy;
import com.bbcow.service.impl.BookService;
import com.bbcow.service.impl.SiteService;
import com.bbcow.service.mongo.entity.BookUrl;
import com.bbcow.service.mongo.entity.SiteUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by adan on 2017/10/18.
 */
@Component
public class FindCrawler implements CommandLineRunner{
    @Autowired
    BookService bookService;
    @Autowired
    SiteService siteService;
    @Autowired
    CrawlerProperties crawlerProperties;
    FindProxy findProxy;

    @Override
    public void run(String... strings) throws Exception {
        if (!crawlerProperties.isFind()){
            return;
        }
        OOSpider bookSpider = new OOSpider(new Processor());
        siteService.load().forEach(site -> {
            bookSpider.addUrl(site.getStartUrl());
        });
        bookSpider.thread(4).start();

        findProxy = new FindProxy(siteService.loadElements());
    }
    class Processor implements PageProcessor{

        @Override
        public void process(Page page) {

            findProxy.getSites(page).forEach(siteUrl -> {
                siteService.saveUrl(siteUrl);
                page.addTargetRequest(siteUrl.getUrl());
            });

            findProxy.getBookUrls(page).forEach(bookUrl -> {
                bookService.saveUrl(bookUrl);
            });

        }

        @Override
        public Site getSite() {
            return Site.me();
        }
    }
}
