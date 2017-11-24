package com.bbcow.service.impl;

import com.bbcow.service.mongo.entity.BookSiteChapter;
import com.bbcow.service.mongo.entity.ScoreSite;
import com.bbcow.service.mongo.reporitory.BookSiteChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
@Service
public class BookSiteService {
    @Autowired
    BookSiteChapterRepository bookSiteChapterRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    public void save(List<BookSiteChapter> chapters){
        bookSiteChapterRepository.save(chapters);
    }
    public void save(BookSiteChapter chapter){
        bookSiteChapterRepository.save(chapter);
    }
    public BookSiteChapter getLastChapter(String id){
        return bookSiteChapterRepository.findOne(id);
    }
    public BookSiteChapter getLastOne(String rk){
        return bookSiteChapterRepository.findOneByReferenceKey(rk, new Sort(Sort.Direction.DESC, "sn"));
    }
    public void updateSite(String id, String name, String url){

        Update update = new Update();
        update.addToSet("site_urls", url);
        Query query = Query.query(Criteria.where("_id").is(id));
        query.addCriteria(Criteria.where("name").is(name));

        mongoTemplate.updateFirst(query, update, BookSiteChapter.class).getN();
    }
}
