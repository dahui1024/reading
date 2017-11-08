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
            book_record.setIsSign(book.getIsSign());
            if (!StringUtils.isEmpty(book.getDescription()))
                book_record.setDescription(book.getDescription());
            if (book.getTags() != null && !book.getTags().isEmpty())
                book_record.setTags(book.getTags());
            if (book.getCpImageUrl() != null)
                book_record.setCpImageUrl(book.getCpImageUrl());
            if (book.getCpName() != null)
                book_record.setCpName(book.getCpName());
            book_record.setUpdateTime(new Date());

            bookRepository.save(book_record);
        }
    }

    public boolean existsWithName(String name){
        return !bookRepository.findByName(name).isEmpty();
    }
    public int resetPageScore(String name, int score){
        List<Book> books = bookRepository.findByName(name);
        books.forEach(book -> {
            book.setPageScore(score);
        });
        return bookRepository.save(books).size();
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

    public List<Book> getTop50(){
        PageRequest pageRequest = new PageRequest(0, 50, Sort.Direction.DESC, "page_score");

        return bookRepository.findAll(pageRequest).getContent();
    }

    public List<Book> recommend(String author){
        PageRequest pageRequest = new PageRequest(0, 5);
        return bookRepository.findByAuthor(author, pageRequest).getContent();
    }

    public BookElement findElementByHost(String host){
        return bookElementRepository.findOne(host);
    }

    public void saveUrl(String url, String host){
        if (bookUrlRepository.findOne(url) == null){
            BookUrl bookUrl = new BookUrl();
            bookUrl.setHost(host);
            bookUrl.setUrl(url);
            bookUrl.setCreateTime(new Date());
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
