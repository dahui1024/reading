package com.bbcow.crawler.site;

import com.bbcow.crawler.CrawlerProperties;
import com.bbcow.service.impl.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by adan on 2017/10/18.
 */
@Component
public class SpiderCrawler implements CommandLineRunner{
    @Autowired
    ScoreService scoreService;
    static boolean isAdd = true;
    @Autowired
    CrawlerProperties crawlerProperties;
    @Override
    public void run(String... strings) throws Exception {
        if (!crawlerProperties.isSpider()){
            return;
        }
        OOSpider bookSpider = new OOSpider(new SpiderCrawler.Processor());
        bookSpider.addUrl("http://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=%E5%85%83%E5%B0%8A&oq=%25E5%2585%2583%25E5%25B0%258A&rsv_pq=f86c10040001b443&rsv_t=643cFdFlw9uN6fpL6Jx7tf%2F6Z2N2Qllf%2FuXaNUNqENw4OgDG%2B4tjkOmng5A&rqlang=cn&rsv_enter=0");
        bookSpider.setExitWhenComplete(true);
        bookSpider.thread(1).start();
    }
    class Processor implements PageProcessor{

        @Override
        public void process(Page page) {

            if (isAdd){
                page.getHtml().xpath("//*[@id=\"page\"]").links().all().forEach(s -> {
                    page.addTargetRequest(new Request(s));
                });

                isAdd = false;
            }
            page.getHtml().xpath("//*[@class=\"result\"]//*[@class=\"c-showurl\"]/text()").all().forEach(s1 -> {
                try {
                    URL url = new URL("http://"+s1.replace("...", ""));

                    page.addTargetRequest(new Request("http://"+ url.getHost()));
                    scoreService.addSite(url.getHost());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            });
            page.getHtml().getDocument().select("a").forEach(element -> {
                System.out.println(element.text()+element.absUrl("href"));
            });

        }

        @Override
        public Site getSite() {
            return Site.me();
        }
    }


    public static void main(String[] args) {
        SpiderCrawler spiderCrawler = new SpiderCrawler();
        try {
            spiderCrawler.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
