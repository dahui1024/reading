package com.bbcow.service;

import com.bbcow.service.impl.SiteService;
import com.bbcow.service.mongo.entity.Book;
import com.bbcow.service.mongo.entity.BookElement;
import com.bbcow.service.mongo.entity.BookUrl;
import com.bbcow.service.mongo.entity.SiteElement;
import com.bbcow.service.mongo.reporitory.BookElementRepository;
import com.bbcow.service.mongo.reporitory.BookRepository;
import com.bbcow.service.mongo.reporitory.BookUrlRepository;
import com.bbcow.service.redis.po.UrlPO;
import com.bbcow.service.redis.template.UrlRedisTemplate;
import com.bbcow.service.util.MD5;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by adan on 2017/10/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@SpringBootApplication
@ActiveProfiles("service-dev")
public class RedisTest {
    @Autowired
    UrlRedisTemplate urlRedisTemplate;

    @Test
    public void s(){
//        System.out.println(urlRedisTemplate.keys("*"));
        UrlPO urlPO = new UrlPO();
        urlPO.setUrl("www.baidu.com");
        urlRedisTemplate.convertAndSend("crawler:book", urlPO);
    }

}
