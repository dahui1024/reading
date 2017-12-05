package com.bbcow.task;

import com.bbcow.service.impl.BookService;
import com.bbcow.service.impl.ScoreService;
import com.bbcow.service.mongo.entity.ScoreBook;
import com.bbcow.service.mongo.entity.ScoreSite;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by adan on 2017/11/5.
 */
@Component
public class ScoreTask {
    Logger logger = LoggerFactory.getLogger(ScoreTask.class);
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

        List<ScoreSite> sites = scoreService.findEnableSite();
        Map<String, Integer> siteMap = new HashMap<>();
        sites.forEach(site -> siteMap.put(site.getHost(), site.getRank()));

        List<ScoreBook> scoreBooks = scoreService.findByDay(day);
        if (scoreBooks != null && !scoreBooks.isEmpty()){
            for (ScoreBook scoreBook : scoreBooks){
                try {
                    BigDecimal bigDecimal = new BigDecimal((scoreBook.getScore()+BASIC_SCORE) * 10);
                    BigDecimal siteCount = new BigDecimal(scoreBook.getSiteScores().size() + BASIC_COUNT);
                    BigDecimal result = bigDecimal.divide(siteCount, 0, BigDecimal.ROUND_FLOOR);

                    if (result.intValue() <= 0){
                        continue;
                    }
                    List<String> urls = scoreBook.getUrls();
                    try {
                        Collections.sort(urls, (c1, c2) -> {
                            String h1 = getHost(c1);
                            String h2 = getHost(c2);
                            int rank1 = siteMap.containsKey(h1) ? siteMap.get(h1) : 0;
                            int rank2 = siteMap.containsKey(h2) ? siteMap.get(h2) : 0;
                            return rank2 - rank1;
                        });
                    }catch (Exception e){

                    }

                    int n = bookService.resetPageScore(scoreBook.getName(), result.intValue(), siteCount.intValue(), urls);
                    if (n > 0) {
                        scoreService.addScoreLog(scoreBook.getName(), day, scoreBook.getUrls(), result.intValue(), siteCount.intValue());
                    }
                }catch (Exception e){
                    logger.error(e.getMessage(), e);
                }

            }

        }

        System.out.println("page score task finished!");
        isRunning = false;
    }

    private String getHost(String url){
        try {
            URI uri = new URI(url);
            return uri.getHost();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
