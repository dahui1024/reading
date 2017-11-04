package com.bbcow.crawler.site;

import com.bbcow.crawler.CrawlerProperties;
import com.bbcow.crawler.proxy.BookProxy;
import com.bbcow.service.impl.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by adan on 2017/10/18.
 */
@Component
public class BookCrawler implements CommandLineRunner{
    @Autowired
    BookService bookService;
    @Autowired
    CrawlerProperties crawlerProperties;
    BookProxy bookProxy;

    @Override
    public void run(String... strings) throws Exception {
        if (!crawlerProperties.isBook()){
            return;
        }
        OOSpider bookSpider = new OOSpider(new BookCrawler.Processor());
        bookSpider.addUrl("https://www.qidian.com");
        bookSpider.setExitWhenComplete(false);
        bookSpider.thread(4).start();

        bookProxy = new BookProxy(bookService.loadElements());

    }
    class Processor implements PageProcessor {

        @Override
        public void process(Page page) {
            try {
                bookService.save(bookProxy.getBook(page));
            }catch (Exception e){

            }finally {
                bookService.getUrlQueue().forEach(bookUrls -> {
                    bookService.finishUrl(bookUrls.getUrl());
                    page.addTargetRequest(bookUrls.getUrl());
                });

            }

        }

        @Override
        public Site getSite() {
            return Site.me();
        }
    }

}
