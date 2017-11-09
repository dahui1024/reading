package com.bbcow.crawler.book.processor;

import com.bbcow.crawler.book.proxy.FindNewProxy;
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
public class FindNewProcessor implements PageProcessor {
    BookService bookService;
    SiteService siteService;
    FindNewProxy findNewProxy;
    public FindNewProcessor(@Autowired BookService bookService, @Autowired SiteService siteService){
        this.siteService = siteService;
        this.bookService = bookService;
        findNewProxy = new FindNewProxy(siteService.loadElements());
    }
    @Override
    public void process(Page page) {
        findNewProxy.getBookUrls(page).forEach(bookUrl -> {
            bookService.saveUrl(bookUrl);
        });
    }

    @Override
    public Site getSite() {
        return Site.me();
    }
}
