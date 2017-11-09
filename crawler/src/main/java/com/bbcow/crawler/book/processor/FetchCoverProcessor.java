package com.bbcow.crawler.book.processor;

import com.bbcow.crawler.book.proxy.FetchCoverProxy;
import com.bbcow.service.impl.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class FetchCoverProcessor implements PageProcessor {
    BookService bookService;
    FetchCoverProxy fetchCoverProxy;

    public FetchCoverProcessor(@Autowired BookService bookService){
        this.bookService = bookService;
        this.fetchCoverProxy = new FetchCoverProxy(bookService.loadElements());
    }

    @Override
    public void process(Page page) {
        try {
            bookService.save(fetchCoverProxy.getBook(page));
            bookService.finishUrl(page.getRequest().getUrl());
        }catch (Exception e){

        }
    }

    @Override
    public Site getSite() {
        return Site.me();
    }
}
