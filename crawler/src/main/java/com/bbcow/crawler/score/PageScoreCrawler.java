package com.bbcow.crawler.score;

import com.bbcow.crawler.TaskCrawler;
import com.bbcow.crawler.score.processor.PageScoreProcessor;
import com.bbcow.service.impl.ScoreService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class PageScoreCrawler extends TaskCrawler<PageScoreProcessor> {
    @Autowired
    ScoreService scoreService;

    Date finishDay = null;

    public PageScoreCrawler(@Autowired PageScoreProcessor pageProcessor){
        super(pageProcessor);
    }

    @Override
    public void execute() {
        Date day = DateUtils.truncate(new Date(), Calendar.DATE);

        if (finishDay != null && !day.after(finishDay)){
            return;
        }

        // 一天限制爬取一次
        scoreService.findEnableSite().stream().filter(scoreSite -> {
//            if (scoreSite.getCrawlTime() == null)
//                return true;
//            return scoreSite.getCrawlTime().before(day);
            return true;
        }).forEach(scoreSite -> spider.addUrl("http://" + scoreSite.getHost()));
        spider.thread(1);
        spider.start();

        finishDay = day;
    }

}
