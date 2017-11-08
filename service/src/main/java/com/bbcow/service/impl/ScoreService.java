package com.bbcow.service.impl;

import com.bbcow.service.mongo.entity.ScoreBook;
import com.bbcow.service.mongo.entity.ScoreBookLog;
import com.bbcow.service.mongo.entity.ScoreSite;
import com.bbcow.service.mongo.reporitory.ScoreBookLogRepository;
import com.bbcow.service.mongo.reporitory.ScoreBookRepository;
import com.bbcow.service.mongo.reporitory.ScoreSiteRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ScoreService {
    @Autowired
    ScoreSiteRepository scoreSiteRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    ScoreBookRepository scoreBookRepository;
    @Autowired
    ScoreBookLogRepository scoreBookLogRepository;

    public void updateBookPageScore(String name, int score){
        Date date = new Date();
        Date day = DateUtils.truncate(date, Calendar.DATE);
        ScoreBook scoreBook = scoreBookRepository.findByNameAndDay(name, day);
        if (scoreBook == null){
            scoreBook = new ScoreBook();
            scoreBook.setName(name);
            scoreBook.setScore(score);
            scoreBook.setCreateTime(date);
            scoreBook.setDay(day);
            scoreBookRepository.save(scoreBook);
        }else {
            Update update = new Update();
            update.inc("score", score);
            mongoTemplate.updateFirst(Query.query(Criteria.where("name").is(name)), update, ScoreBook.class).getN();
        }
    }

    public List<ScoreBookLog> findTop30ByName(String name){
        return scoreBookLogRepository.findTop30ByName(name, new Sort(Sort.Direction.DESC, "day"));
    }
    public void addScoreLog(String name, Date day, int pageScore){
        ScoreBookLog scoreBookLog = new ScoreBookLog();
        scoreBookLog.setDay(day);
        scoreBookLog.setName(name);
        scoreBookLog.setPageScore(pageScore);

        scoreBookLogRepository.save(scoreBookLog);
    }

    public List<ScoreBook> findByDay(Date day){
        return scoreBookRepository.findByDay(day);
    }

    public void addSite(String host){
        if(scoreSiteRepository.findOne(host)== null){
            ScoreSite scoreSite = new ScoreSite();
            scoreSite.setHost(host);
            scoreSite.setCreateTime(new Date());
            scoreSite.setStatus(1);

            scoreSiteRepository.save(scoreSite);
        }
    }
    public List<ScoreSite> findEnableSite(){
        return scoreSiteRepository.findByStatus(1);
    }

    public int getFinishCrawlSiteCountToday(){
        Date date = DateUtils.truncate(new Date(), Calendar.DATE);
        List<ScoreSite> scoreSites = scoreSiteRepository.findByStatusAndCrawlTimeGreaterThan(1, date);

        if (scoreSites != null){
            return scoreSites.size();
        }
        return 0;
    }

    public void finishCrawl(String host, int usefulLinkCount){
        Update update = new Update();
        update.inc("crawl_count", 1);
        update.set("crawl_time", new Date());
        // 评级网站
        if (usefulLinkCount <= 0){
            update.set("status", 0);
            update.set("rank", 0);
        }else if (usefulLinkCount < 10){
            update.set("rank", 5);
        }else {
            update.set("rank", 10);
            update.set("status", 1);
        }
        mongoTemplate.updateFirst(Query.query(Criteria.where("host").is(host)), update, ScoreSite.class).getN();
    }
}
