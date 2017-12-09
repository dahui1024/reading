package com.bbcow.crawler.score;

import com.bbcow.crawler.TaskCrawler;
import com.bbcow.crawler.scheduler.DefaultScheduler;
import com.bbcow.crawler.score.processor.FindSiteProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class FindSiteCrawler extends TaskCrawler<FindSiteProcessor> {

    public FindSiteCrawler(@Autowired FindSiteProcessor findSiteProcessor) {
        super(findSiteProcessor);
    }

    @Override
    public void execute() {
        spider.setScheduler(new DefaultScheduler());

        spider.addUrl("https://www.so.com/s?q=%E5%9C%A3%E5%A2%9F&src=srp&fr=none&psid=66cb487356b0c63ff4363c8f1a04efee");
        spider.addUrl("https://www.so.com/s?ie=utf-8&fr=none&src=360sou_newhome&q=%E5%A4%A7%E4%B8%BB%E5%AE%B0");
        spider.addUrl("https://www.so.com/s?ie=utf-8&fr=none&src=360sou_newhome&q=%E9%BE%99%E7%8E%8B%E4%BC%A0%E8%AF%B4");
        spider.addUrl("https://www.so.com/s?ie=utf-8&fr=none&src=360sou_newhome&q=%E7%A5%9E%E7%BA%A7%E6%8A%A4%E8%8A%B1%E4%BF%9D%E9%95%96");
        spider.addUrl("https://www.so.com/s?ie=utf-8&fr=none&src=360sou_newhome&q=%E9%9B%AA%E9%B9%B0%E9%A2%86%E4%B8%BB");
        spider.thread(1).start();
    }

    public static void main(String[] args) {
        FindSiteCrawler findSiteCrawler = new FindSiteCrawler(new FindSiteProcessor());
        findSiteCrawler.execute();
    }
}
