package com.bbcow.crawler.site;

import com.bbcow.crawler.model.QiDianModel;
import com.bbcow.crawler.model.QiDianRankModel;
import com.bbcow.service.BookService;
import com.bbcow.service.BookUrlsService;
import com.bbcow.service.mongo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.*;

/**
 * Created by adan on 2017/10/18.
 */
@Component
public class QiDianCrawler implements CommandLineRunner{
    @Autowired
    BookService bookService;
    @Autowired
    BookUrlsService bookUrlsService;

    @Override
    public void run(String... strings) throws Exception {
        OOSpider bookSpider = new OOSpider(new QiDianCrawler.Processor());
        bookSpider.addUrl("https://book.qidian.com/info/1003694333");
        bookSpider.thread(2).start();

    }
    static class Processor implements PageProcessor {

        @Override
        public void process(Page page) {
            Book book = new Book();
            book.setName(page.getHtml().xpath("/html/body/div[2]/div[6]/div[1]/div[2]/h1/em/text()").get());
            book.setAuthor(page.getHtml().xpath("/html/body/div[2]/div[6]/div[1]/div[2]/h1/em/text()").get());
            book.setTags(page.getHtml().xpath("/html/body/div[2]/div[6]/div[1]/div[2]/h1/em/text()").all());

            System.out.println(book.getName());
        }

        @Override
        public Site getSite() {
            return Site.me();
        }
    }

    public static void main(String[] args) {
        OOSpider bookSpider = new OOSpider(new QiDianCrawler.Processor());

        bookSpider.thread(2).start();
    }
}
