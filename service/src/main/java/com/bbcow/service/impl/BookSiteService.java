package com.bbcow.service.impl;

import com.bbcow.service.mongo.entity.BookSiteChapter;
import com.bbcow.service.mongo.reporitory.BookSiteChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
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
    public BookSiteChapter findByIdAndStatus(String id){
        return bookSiteChapterRepository.findByIdAndStatus(id, 2);
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
            }
            return 1;
        }else {
            if (!bookSiteChapter.getSiteUrls().contains(url)){
                bookSiteChapter.getSiteUrls().add(url);
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
