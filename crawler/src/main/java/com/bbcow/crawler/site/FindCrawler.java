package com.bbcow.crawler.site;

import com.bbcow.crawler.model.FindModel;
import com.bbcow.crawler.model.QiDianModel;
import com.bbcow.crawler.model.QiDianRankModel;
import com.bbcow.service.BookService;
import com.bbcow.service.BookUrlsService;
import com.bbcow.service.mongo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by adan on 2017/10/18.
 */
@Component
public class FindCrawler implements CommandLineRunner{
    @Autowired
    BookUrlsService bookUrlsService;
    public static int count = 0;

    @Override
    public void run(String... strings) throws Exception {
        OOSpider bookSpider = new OOSpider(new Processor());

        bookSpider.addUrl("https://www.qidian.com/all?orderId=&page=1&style=1&pageSize=50&siteid=1&pubflag=0&hiddenField=0");
        bookSpider.thread(2).start();

    }
    class Processor implements PageProcessor{

        @Override
        public void process(Page page) {
            List<String> links = page.getHtml().xpath("//*[@id=\"page-container\"]/div/ul").links().all();

            page.addTargetRequests(links);

            List<String> bookLinks = page.getHtml().links().regex(".*info.*").all();
            Set<String> uniqueLinks = new TreeSet<>(bookLinks);

            uniqueLinks.forEach(s -> {
                bookUrlsService.save(s, "www.qidian.com");
            });

        }

        @Override
        public Site getSite() {
            return Site.me();
        }
    }
}
