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
        bookSpider.addUrl("http://www.baidu.com/s?wd=%E6%96%97%E7%A0%B4%E8%8B%8D%E7%A9%B9%E5%B0%8F%E8%AF%B4&rsv_spt=1&rsv_iqid=0x9c949fce000298a1&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&rqlang=cn&tn=baiduhome_pg&rsv_enter=0&oq=%25E6%2596%2597%25E7%25A0%25B4%25E8%258B%258D%25E7%25A9%25B9%25E5%25B0%258F%25E8%25AF%25B4&rsv_t=b9ff2f6G2oQDRvmkP5H52thCLj%2BLzt%2FITgSGqI07Hyk1M63ekgEfr34ujkPO3bGFJAuM&rsv_pq=e9d0285600025eb1&rsv_sug=9");
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
