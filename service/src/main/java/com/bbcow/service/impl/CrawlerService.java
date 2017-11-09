package com.bbcow.service.impl;

import com.bbcow.service.mongo.entity.*;
import com.bbcow.service.mongo.reporitory.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by adan on 2017/10/17.
 */
@Service
public class CrawlerService {
    @Autowired
    CrawlerLogRepository crawlerLogRepository;
    @Autowired
    CrawlerRepository crawlerRepository;

    public void update(String id, String name, String status){
        CrawlerLog crawlerLog = crawlerLogRepository.findOne(id);
        if (crawlerLog == null){
            crawlerLog = new CrawlerLog();
            crawlerLog.setId(id);
            crawlerLog.setCheckTime(new Date());
            crawlerLog.setRegisterTime(crawlerLog.getCheckTime());
            crawlerLog.setName(name);
            crawlerLog.setStatus(status);
        }else {
            crawlerLog.setCheckTime(new Date());
            crawlerLog.setStatus(status);
        }

        crawlerLogRepository.save(crawlerLog);
    }
    public Crawler getCrawler(String name){
        return crawlerRepository.findOne(name);
    }
}
