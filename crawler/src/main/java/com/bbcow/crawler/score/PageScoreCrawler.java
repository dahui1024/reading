package com.bbcow.crawler.score;

import com.bbcow.crawler.TaskCrawler;
import com.bbcow.crawler.scheduler.DefaultScheduler;
import com.bbcow.crawler.score.processor.PageScoreProcessor;
import com.bbcow.service.impl.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class PageScoreCrawler extends TaskCrawler<PageScoreProcessor> {
    @Autowired
    ScoreService scoreService;

    public PageScoreCrawler(@Autowired PageScoreProcessor pageProcessor){
        super(pageProcessor);
    }
    @Override
    @Scheduled(cron = "0 0 * * * ?")
    public void task(){
        super.task();
    }

    @Override
    public void execute() {

        spider.setScheduler(new DefaultScheduler());

        scoreService.findEnableSite().forEach(scoreSite -> spider.addUrl("http://" + scoreSite.getHost()));

        spider.thread(1);
        spider.start();

    }

}
