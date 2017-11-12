package com.bbcow.task;

import com.bbcow.service.impl.BookService;
import com.bbcow.service.impl.ScoreService;
import com.bbcow.service.mongo.entity.ScoreBook;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by adan on 2017/11/5.
 */
@Component
public class ScoreTask {
    @Autowired
    ScoreService scoreService;
    @Autowired
    BookService bookService;
    boolean isRunning = false;
    private static final int BASIC_SCORE = 86;
    private static final int BASIC_COUNT = 11;

    @Scheduled(cron = "0 0 2 * * ?")
    public void pageScoreTask(){
        if (isRunning){
            return;
        }
        isRunning = true;

        Date day = DateUtils.truncate(new Date(), Calendar.DATE);

        List<ScoreBook> scoreBooks = scoreService.findByDay(day);
        if (scoreBooks != null && !scoreBooks.isEmpty()){
            for (ScoreBook scoreBook : scoreBooks){
                BigDecimal bigDecimal = new BigDecimal((scoreBook.getScore()+BASIC_SCORE) * 10);
                BigDecimal siteCount = new BigDecimal(scoreBook.getSiteScores().size() + BASIC_COUNT);
                BigDecimal result = bigDecimal.divide(siteCount, 0, BigDecimal.ROUND_FLOOR);

                if (result.intValue() <= 0){
                    continue;
                }
                int n = bookService.resetPageScore(scoreBook.getName(), result.intValue(), siteCount.intValue());
                if (n > 0) {
                    scoreService.addScoreLog(scoreBook.getName(), day, result.intValue(), siteCount.intValue());
                }
            }

        }

        System.out.println("page score task finished!");
        isRunning = false;
    }
}
