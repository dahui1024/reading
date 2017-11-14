package com.bbcow.crawler.score;

import com.bbcow.crawler.TaskCrawler;
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
        // 关闭爬虫，清理重复队列
        if (spider.getStatus() == Spider.Status.Stopped){
            spider.close();
        }

        spider.addUrl("https://www.baidu.com/s?ie=utf-8&f=3&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E9%A3%9E%E5%89%91%E9%97%AE%E9%81%93&oq=%25E9%25A3%259E%25E5%2589%2591%25E9%2597%25AE%25E9%2581%2593&rsv_pq=bc5691bb0001f1cb&rsv_t=59a9AsziD6tMbWVeOD%2F1t4%2FxZFIpYw0InY4yVfQxAsasTVf11ziwvASfLLg&rqlang=cn&rsv_enter=0&inputT=29530&prefixsug=%25E9%25A3%259E%25E5%2589%2591%25E9%2597%25AE%25E9%2581%2593&rsp=0&rsv_sug4=29530");
        spider.thread(1).start();
    }
}
