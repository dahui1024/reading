package com.bbcow.service.impl;

import com.bbcow.service.mongo.entity.ScoreBook;
import com.bbcow.service.mongo.entity.ScoreBookLog;
import com.bbcow.service.mongo.entity.ScoreSite;
import com.bbcow.service.mongo.reporitory.ScoreBookLogRepository;
import com.bbcow.service.mongo.reporitory.ScoreBookRepository;
import com.bbcow.service.mongo.reporitory.ScoreSiteRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
            scoreBook.setCreate_time(date);
            scoreBook.setDay(day);
            scoreBookRepository.save(scoreBook);
        }else {
            Update update = new Update();
            update.inc("score", score);
            mongoTemplate.updateFirst(Query.query(Criteria.where("name").is(name)), update, ScoreBook.class).getN();
        }
    }

    public List<ScoreBookLog> findNewly30ByName(String name){
        Query query = Query.query(Criteria.where("name").is(name));

        return mongoTemplate.find(query, ScoreBookLog.class);
//        return scoreBookLogRepository.findNewly30ByName(name);
    }
    public void addScoreLog(String name, Date day, int pageScore){
        ScoreBookLog scoreBookLog = new ScoreBookLog();
        scoreBookLog.setDay(day);
        scoreBookLog.setName(name);
        scoreBookLog.setPageScore(pageScore);

        scoreBookLogRepository.save(scoreBookLog);
    }

    public long countScoreBook(){
        return scoreBookRepository.count();
    }

    public List<ScoreBook> findByDay(Date day){
        return scoreBookRepository.findByDay(day);
    }

    public void addSite(String host){
        if(scoreSiteRepository.findOne(host)== null){
            ScoreSite scoreSite = new ScoreSite();
            scoreSite.setHost(host);
            scoreSite.setCreate_time(new Date());
            scoreSite.setStatus(1);

            scoreSiteRepository.save(scoreSite);
        }
    }
    public List<ScoreSite> findEnabelSite(){
        return scoreSiteRepository.findByStatus(1);
    }

    public long getCrawlCount(){
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("name").sum("crawl_count").as("crawl_count"),
                Aggregation.project().andExclude("_id")
        );
        System.out.println(aggregation);
        return mongoTemplate.aggregate(aggregation, "score_site", ScoreSite.class).getUniqueMappedResult().getCrawl_count();
    }

    public void finishCrawl(String host){
        Update update = new Update();
        update.inc("crawl_count", 1);
        update.set("crawl_time", new Date());
        mongoTemplate.updateFirst(Query.query(Criteria.where("host").is(host)), update, ScoreSite.class).getN();
    }
}
