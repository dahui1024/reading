package com.bbcow.crawler.book.processor;

import com.bbcow.crawler.book.proxy.FetchUrlProxy;
import com.bbcow.service.impl.BookService;
import com.bbcow.service.impl.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class FetchUrlProcessor implements PageProcessor {
    BookService bookService;
    SiteService siteService;
    FetchUrlProxy fetchUrlProxy;

    public FetchUrlProcessor(@Autowired BookService bookService, @Autowired SiteService siteService){
       this.bookService = bookService;
       this.siteService = siteService;
       fetchUrlProxy = new FetchUrlProxy(siteService.loadElements());
    }
    @Override
    public void process(Page page) {
        fetchUrlProxy.getSites(page).forEach(siteUrl -> {
            siteService.saveUrl(siteUrl);
            page.addTargetRequest(siteUrl.getUrl());
        });

        fetchUrlProxy.getBookUrls(page).forEach(bookUrl -> {
            bookService.saveUrl(bookUrl);
        });
    }

    @Override
    public Site getSite() {
        return Site.me();
    }
}
