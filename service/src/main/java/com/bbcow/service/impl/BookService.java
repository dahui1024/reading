package com.bbcow.service.impl;

import com.bbcow.service.mongo.entity.Book;
import com.bbcow.service.mongo.entity.BookElement;
import com.bbcow.service.mongo.entity.BookUrl;
import com.bbcow.service.mongo.reporitory.BookElementRepository;
import com.bbcow.service.mongo.reporitory.BookRepository;
import com.bbcow.service.mongo.reporitory.BookUrlRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
public class BookService {
    @Autowired
    BookUrlRepository bookUrlRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookElementRepository bookElementRepository;

    public void save(Book book){
        if (book.getName() == null || book.getAuthor() == null) {
            return;
        }
        Book book_record = bookRepository.findByNameAndAuthor(book.getName(), book.getAuthor());
        if (book_record == null){
            bookRepository.save(book);
        } else {
            book_record.setIs_sign(book.getIs_sign());
            if (!StringUtils.isEmpty(book.getDescription()))
                book_record.setDescription(book.getDescription());
            if (book.getTags() != null && !book.getTags().isEmpty())
                book_record.setTags(book.getTags());
            if (book.getCp_image_url() != null)
                book_record.setCp_image_url(book.getCp_image_url());
            if (book.getCp_name() != null)
                book_record.setCp_name(book.getCp_name());
            book_record.setUpdate_time(new Date());

            bookRepository.save(book_record);
        }
    }

    public int resetPageScore(String name, int score){
        Update bookUpdate = new Update();
        bookUpdate.set("page_score", score);
        return mongoTemplate.updateFirst(Query.query(Criteria.where("name").is(name)), bookUpdate, Book.class).getN();
    }

    public Book getById(ObjectId id) {
        return bookRepository.findOne(id);
    }

    public Map<String, BookElement> loadElements(){
        Map<String, BookElement> elementMap = new TreeMap<>();
        bookElementRepository.findAll().forEach(bookElement -> {
            elementMap.put(bookElement.getHost(), bookElement);
        });
        return elementMap;
    }


    public BookElement findElementByHost(String host){
        return bookElementRepository.findOne(host);
    }

    public void saveUrl(String url, String host){
        if (bookUrlRepository.findOne(url) == null){
            BookUrl bookUrl = new BookUrl();
            bookUrl.setHost(host);
            bookUrl.setUrl(url);
            bookUrl.setCreate_time(new Date());
            bookUrlRepository.save(bookUrl);
        }
    }
    public void saveUrl(BookUrl bookUrl) {
        if (bookUrlRepository.findOne(bookUrl.getUrl()) == null){
            bookUrlRepository.save(bookUrl);
        }
    }

    public void finishUrl(String url){
        Update update = new Update();
        update.inc("crawl_count", 1);
        update.set("crawl_time", new Date());

        int n = mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(url)), update, BookUrl.class).getN();
    }

    public List<BookUrl> getUrlQueue(){
        PageRequest pageRequest = new PageRequest(1, 4, Sort.Direction.ASC, "crawl_time");
        return bookUrlRepository.findAll(pageRequest).getContent();
    }
}
