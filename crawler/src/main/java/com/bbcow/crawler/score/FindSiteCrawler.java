package com.bbcow.crawler.score;

import com.bbcow.crawler.TaskCrawler;
import com.bbcow.crawler.score.processor.FindSiteProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        spider.addUrl("http://www.baidu.com/s?wd=%E6%96%97%E7%A0%B4%E8%8B%8D%E7%A9%B9%E5%B0%8F%E8%AF%B4&rsv_spt=1&rsv_iqid=0x9c949fce000298a1&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&rqlang=cn&tn=baiduhome_pg&rsv_enter=0&oq=%25E6%2596%2597%25E7%25A0%25B4%25E8%258B%258D%25E7%25A9%25B9%25E5%25B0%258F%25E8%25AF%25B4&rsv_t=b9ff2f6G2oQDRvmkP5H52thCLj%2BLzt%2FITgSGqI07Hyk1M63ekgEfr34ujkPO3bGFJAuM&rsv_pq=e9d0285600025eb1&rsv_sug=9");
        spider.thread(1).start();
    }
}
