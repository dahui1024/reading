package com.bbcow.crawler.score;

import com.bbcow.crawler.TaskCrawler;
import com.bbcow.crawler.score.processor.PageScoreProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class PageScoreCrawler extends TaskCrawler<PageScoreProcessor> {

    public PageScoreCrawler(@Autowired PageScoreProcessor pageProcessor){
        super(pageProcessor);
    }

    @Override
    public void execute() {
        spider.thread(1);

        System.out.println(pageProcessor+"----");
    }

}
