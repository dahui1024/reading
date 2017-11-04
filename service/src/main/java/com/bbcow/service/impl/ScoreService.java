package com.bbcow.service.impl;

import com.bbcow.service.mongo.entity.ScoreBook;
import com.bbcow.service.mongo.entity.ScoreSite;
import com.bbcow.service.mongo.reporitory.ScoreBookRepository;
import com.bbcow.service.mongo.reporitory.ScoreSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

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

    public void updateBookPageScore(String name, int score){
        ScoreBook scoreBook = scoreBookRepository.findOne(name);
        if (scoreBook == null){
            scoreBook = new ScoreBook();
            scoreBook.setName(name);
            scoreBook.setScore(score);
            scoreBook.setCreate_time(new Date());
            scoreBookRepository.save(scoreBook);
        }else {
            Update update = new Update();
            update.inc("score", score);
            mongoTemplate.updateFirst(Query.query(Criteria.where("name").is(name)), update, ScoreBook.class).getN();
        }
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
    public List<ScoreSite> findAll(){
        return scoreSiteRepository.findByStatus(1);
    }

    public void finishCrawl(String host){
        Update update = new Update();
        update.inc("crawl_count", 1);
        update.set("crawl_time", new Date());
        mongoTemplate.updateFirst(Query.query(Criteria.where("host").is(host)), update, ScoreSite.class).getN();
    }
}
