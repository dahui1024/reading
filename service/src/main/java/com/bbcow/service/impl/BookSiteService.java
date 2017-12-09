package com.bbcow.service.impl;

import com.bbcow.service.mongo.entity.BookSite;
import com.bbcow.service.mongo.entity.BookSiteChapter;
import com.bbcow.service.mongo.reporitory.BookSiteChapterRepository;
import com.bbcow.service.mongo.reporitory.BookSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
@Service
public class BookSiteService {
    @Autowired
    BookSiteRepository bookSiteRepository;
    @Autowired
    BookSiteChapterRepository bookSiteChapterRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    public void addSite(String rk, int score, String url){
        BookSite bookSite = bookSiteRepository.findOneByReferenceKeyAndUrl(rk, url);
        if (bookSite == null){
            bookSite = new BookSite();
            bookSite.setCreateTime(new Date());
            bookSite.setUrl(url);
            bookSite.setCount(1);
            bookSite.setReferenceKey(rk);
            bookSite.setScore(score);
            bookSite.setStatus(1);
        }else {
            bookSite.setUpdateTime(new Date());
            bookSite.setScore(score);
            bookSite.setCount(bookSite.getCount() + 1);
        }
        bookSiteRepository.save(bookSite);
    }

    public List<BookSite> getAvailableSites(String rk){
        return bookSiteRepository.findByReferenceKeyAndStatusOrderByScoreDesc(rk, 1);
    }

    public void save(List<BookSiteChapter> chapters){
        bookSiteChapterRepository.save(chapters);
    }
    public void save(BookSiteChapter chapter){
        bookSiteChapterRepository.save(chapter);
    }
    public BookSiteChapter getChapter(String id){
        return bookSiteChapterRepository.findOne(id);
    }
    public BookSiteChapter getLastOne(String rk){
        return bookSiteChapterRepository.findOneByReferenceKey(rk, new Sort(Sort.Direction.DESC, "sn"));
    }
    public BookSiteChapter findByIdAndStatus(String id){
        return bookSiteChapterRepository.findByIdAndStatus(id, 2);
    }
    public List<BookSiteChapter> getChapters(String rk){
        Query query = Query.query(Criteria.where("reference_key").is(rk));
        query.fields().exclude("content");
        query.with(new Sort(Sort.Direction.DESC, "sn"));
        return mongoTemplate.find(query, BookSiteChapter.class);
    }
    public int updateSite(String id, String name, String url){
        BookSiteChapter bookSiteChapter = bookSiteChapterRepository.findOne(id);

        if (bookSiteChapter == null){
            return 0;
        }

        if (bookSiteChapter.getStatus() == 1){
            List<String> urls = new LinkedList<>();
            urls.add(url);
            bookSiteChapter.setSiteUrls(urls);
            bookSiteChapter.setStatus(2);
            bookSiteChapterRepository.save(bookSiteChapter);

            return 1;
        }else if (bookSiteChapter.getStatus() == 2){
            if (!bookSiteChapter.getSiteUrls().contains(url)){
                bookSiteChapter.getSiteUrls().add(url);
                bookSiteChapterRepository.save(bookSiteChapter);
                bookSiteChapter.setUpdateTime(new Date());
            }
            return 1;
        }else {
            if (!bookSiteChapter.getSiteUrls().contains(url)){
                bookSiteChapter.getSiteUrls().add(url);
                bookSiteChapter.setUpdateTime(new Date());
                bookSiteChapterRepository.save(bookSiteChapter);
            }
            return 1;
        }
    }
    public int updateContent(String id, String content){

        Update update = new Update();
        update.set("content", content);
        update.set("status", 3);
        Query query = Query.query(Criteria.where("_id").is(id));

        return mongoTemplate.updateFirst(query, update, BookSiteChapter.class).getN();
    }
}
