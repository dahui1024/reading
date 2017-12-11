package com.bbcow.service.impl;

import com.bbcow.service.mongo.entity.*;
import com.bbcow.service.mongo.reporitory.*;
import com.bbcow.service.util.MD5;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Autowired
    BookChapterRepository bookChapterRepository;
    @Autowired
    BookWordRepository bookWordRepository;

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

    public List<Book> getByName(String name){
        return bookRepository.findByName(name);
    }
    public int resetPageScore(String name, int score, int count, List<String> urls){
        List<Book> books = bookRepository.findByName(name);
        books.forEach(book -> {
            book.setPageScore(score);
            book.setPageCount(count);
//            if (urls != null && !urls.isEmpty()){
//                book.setSiteUrls(urls);
//            }

            BookUrl bookUrl = bookUrlRepository.findOne(book.getCpUrl());
            if (bookUrl != null){
                bookUrl.setPageScore(score);
                bookUrlRepository.save(bookUrl);
            }
        });
        return bookRepository.save(books).size();
    }

    public Book getById(ObjectId id) {
        return bookRepository.findOne(id);
    }
    public Book getByReferenceKey(String rk) {
        return bookRepository.findByReferenceKey(rk);
    }

    public Map<String, BookElement> loadElements(){
        Map<String, BookElement> elementMap = new TreeMap<>();
        bookElementRepository.findAll().forEach(bookElement -> {
            elementMap.put(bookElement.getHost(), bookElement);
        });
        return elementMap;
    }

    public List<Book> getTop50(){
        return bookRepository.findTop50ByIsSignAndPageCountGreaterThanOrderByPageScoreDesc(1,  10);
    }

    public List<Book> getFinishRank(int page){
        if (page < 1){
            page = 1;
        }
        PageRequest pageRequest = new PageRequest(page - 1, 50);
        return bookRepository.findByIsFinishOrderByPageScoreDesc(1, pageRequest);
    }
    public List<Book> getAvailable(){

        Query query = Query.query(Criteria.where("page_score").gt(1));
        query.fields().include("id");
        query.with(new Sort(Sort.Direction.DESC, "page_score"));
        return mongoTemplate.find(query, Book.class);

    }
    public List<Book> getBookWithScore(int page){
        if (page < 1){
            page = 1;
        }
        PageRequest pageRequest = new PageRequest(page - 1, 50);
        return bookRepository.findByPageScoreGreaterThanAndPageCountGreaterThan(1, 1, pageRequest).getContent();
    }
    public List<Book> getBooksWithScoreScope(int from, int to, int page){
        if (page < 1){
            page = 1;
        }
        PageRequest pageRequest = new PageRequest(page - 1, 50);
        return bookRepository.findByPageScoreBetween(from, to, pageRequest).getContent();
    }

    public List<Book> recommend(String name, String author){
        PageRequest pageRequest = new PageRequest(0, 5);
        List<Book> books = bookRepository.findByAuthor(author, pageRequest).getContent();
        List<Book> result = new LinkedList<>();
        books.stream().filter(book -> !name.equals(book.getName())).forEach(book -> {
            result.add(book);
        });

        return result;
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
    public void disableChapterUrl(String referenceKey){
        Update update = new Update();
        update.inc("chapter_crawl_count", 1);
        update.set("chapter_crawl_time", new Date());
        update.set("chapter_status", -1);

        mongoTemplate.updateFirst(Query.query(Criteria.where("reference_key").is(referenceKey)), update, BookUrl.class);
    }
    public void finishChapterUrl(String referenceKey){
        Update update = new Update();
        update.inc("chapter_crawl_count", 1);
        update.set("chapter_crawl_time", new Date());
        update.set("chapter_status", 1);

        BookUrl bookUrl = bookUrlRepository.findByReferenceKey(referenceKey);

        mongoTemplate.updateFirst(Query.query(Criteria.where("reference_key").is(referenceKey)), update, BookUrl.class);

        Book book = bookRepository.findByCpUrl(bookUrl.getUrl());
        book.setReferenceKey(referenceKey);
        book.setUpdateTime(new Date());
        bookRepository.save(book);
    }
    public void finishChapterWord(String referenceKey){
        Update update = new Update();
        update.set("chapter_status", 2);
        mongoTemplate.updateFirst(Query.query(Criteria.where("reference_key").is(referenceKey)), update, BookUrl.class).getN();
    }
    public List<BookUrl> getNewBookUrl(){
        return bookUrlRepository.existsByCrawlTime(null);
    }
    public List<BookUrl> getHotBookUrl(){
        return bookUrlRepository.findByPageScoreGreaterThan(80, new Sort(Sort.Direction.DESC, "page_score"));
    }
    public List<BookUrl> getNewBookChapterUrl(){
        PageRequest pageRequest = new PageRequest(0, 50, new Sort(Sort.Direction.DESC, "page_score"));
        return bookUrlRepository.findByChapterStatus(0, pageRequest);
    }
    public List<BookUrl> getFinishBookChapter(){
        PageRequest pageRequest = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "page_score"));
        return bookUrlRepository.findByChapterStatus(1, pageRequest);
    }
    public List<BookChapter> getBookChapters(String referenceKey){
        return bookChapterRepository.findByReferenceKey(referenceKey);
    }
    public void saveChapterUrl(int sn, String url, String referenceKey){
        BookChapter bookChapter = new BookChapter();
        bookChapter.setSn(sn);
        bookChapter.setReferenceKey(referenceKey);
        bookChapter.setId(MD5.digest_16bit(url));
        bookChapter.setUrl(url);

        bookChapterRepository.save(bookChapter);
    }
    public BookChapter updateChapterContent(String url, String text){
        BookChapter bookChapter = bookChapterRepository.findOne(MD5.digest_16bit(url));
        bookChapter.setContent(text);
        bookChapterRepository.save(bookChapter);
        return bookChapter;
    }
    public void saveWords(Iterable<BookWord> bookWords){
        bookWords.forEach(bookWord -> {
            if (bookWord.getTag() != null && bookWord.getCount() > 1){
                bookWordRepository.save(bookWord);
            }
            if (bookWord.getTag() == null && bookWord.getCount() > 10){
                bookWordRepository.save(bookWord);
            }
        });
    }

    public List<BookWord> getBookPerson(String referenceKey){
        if (StringUtils.isBlank(referenceKey)){
            return Collections.EMPTY_LIST;
        }
        return bookWordRepository.findByReferenceKeyAndTagOrderByCountDesc(referenceKey, "PER");
    }

}
