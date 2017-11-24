package com.bbcow.service;

import com.bbcow.service.impl.BookService;
import com.bbcow.service.impl.SiteService;
import com.bbcow.service.mongo.entity.Book;
import com.bbcow.service.mongo.entity.BookElement;
import com.bbcow.service.mongo.entity.BookUrl;
import com.bbcow.service.mongo.entity.SiteElement;
import com.bbcow.service.mongo.reporitory.BookElementRepository;
import com.bbcow.service.mongo.reporitory.BookRepository;
import com.bbcow.service.mongo.reporitory.BookUrlRepository;
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

import java.net.URL;
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
public class BookTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookElementRepository bookElementRepository;
    @Autowired
    BookUrlRepository bookUrlRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    SiteService siteService;
    @Autowired
    BookService bookService;

    @Test
    public void s(){
        bookRepository.findOne(new ObjectId("59e74c37a316861935b14fff"));
    }
    @Test
    public void init(){
        Query query = Query.query(Criteria.where("reference_key").exists(false));
        query.addCriteria(Criteria.where("host").is("www.qidian.com"));

        long count = mongoTemplate.count(query, BookUrl.class);

        long page = count/50 + 1;

        query.with(new PageRequest(0, 50));
        for (int i = 0; i < page; i++) {

            List<BookUrl> bookUrls = mongoTemplate.find(query, BookUrl.class);

            bookUrls.forEach(bookUrl -> {
                String url = bookUrl.getUrl();

                String chapterUrl = url.replace("book.", "m.").replace(".com/", ".com/book/").replace("info/", "")+"/catalog";

                bookUrl.setChapterUrl(chapterUrl);
                bookUrl.setChapterStatus(0);
                bookUrl.setReferenceKey(MD5.digest_16bit(bookUrl.getChapterUrl()));

                bookUrlRepository.save(bookUrl);
            });
        }
    }
    @Test
    public void initRK(){
        Query query = Query.query(Criteria.where("cp_host").is("book.qidian.com"));
//        query.addCriteria(Criteria.where("reference_key").exists(false));
        long count = mongoTemplate.count(query, Book.class);

        long page = count/50 + 1;

        query.with(new PageRequest(0, 50));
        for (int i = 0; i < page; i++) {
            System.out.println(i+"---");
            List<Book> books = mongoTemplate.find(query, Book.class);
            if (books.isEmpty()){
                continue;
            }

            books.forEach(book -> {

                BookUrl bookUrl = bookUrlRepository.findOne(book.getCpUrl());

                if (bookUrl == null){
                    System.out.println(book.getCpUrl());
                }else {
                    book.setReferenceKey(bookUrl.getReferenceKey());

                    bookRepository.save(book);
                }

            });

        }
    }
    @Test
    public void clean(){
        Map<String, SiteElement> elementMap= siteService.loadElements();
        long count = bookRepository.count();
        long page = count/50 + 1;
        for (int i = 0; i < page; i++) {
            List<Book> books = bookRepository.findAll(new PageRequest(i, 50)).getContent();

            books.forEach(book -> {

                BookUrl bookUrl = bookUrlRepository.findOne(book.getCpUrl());

                if (bookUrl == null){
                    bookUrl = new BookUrl();
                    bookUrl.setHost(book.getCpHost());
                    bookUrl.setUrl(book.getCpUrl());
                    bookUrl.setCrawlTime(new Date());
                    bookUrl.setCrawlCount(1);

                    if (elementMap.containsKey(book.getCpHost())) {
                        String chapterUrl = book.getCpUrl() + elementMap.get(book.getCpHost()).getChapterSuffix();
                        bookUrl.setChapterUrl(chapterUrl);
                        bookUrl.setChapterStatus(0);
                        bookUrl.setReferenceKey(MD5.digest_16bit(bookUrl.getChapterUrl()));
                    }

                    bookUrl.setCreateTime(new Date());

                    bookUrlRepository.save(bookUrl);

                }
            });
        }
    }
    @Test
    public void initIReader(){
        Query query = Query.query(Criteria.where("reference_key").exists(false));
        query.addCriteria(Criteria.where("host").is("yc.ireader.com.cn"));

        long count = mongoTemplate.count(query, BookUrl.class);

        long page = count/50 + 1;

        query.with(new PageRequest(0, 50));
        for (int i = 0; i < page; i++) {

            List<BookUrl> bookUrls = mongoTemplate.find(query, BookUrl.class);

            bookUrls.forEach(bookUrl -> {
                String url = bookUrl.getUrl();


                String chapterUrl = url+"chapters/";

                bookUrl.setChapterUrl(chapterUrl);
                bookUrl.setChapterStatus(0);
                bookUrl.setReferenceKey(MD5.digest_16bit(bookUrl.getChapterUrl()));

                bookUrlRepository.save(bookUrl);

                Query bookQuery = Query.query(Criteria.where("cp_url").is(url));
                Update bookUpdate = Update.update("reference_key", bookUrl.getReferenceKey());

                mongoTemplate.updateFirst(bookQuery,bookUpdate, Book.class);
            });
        }
    }
    @Test
    public void resetCp(){

        Query query = Query.query(Criteria.where("cp_host").is("yc.ireader.com.cn"));

        Update u = Update.update("cp_name", "掌阅");

        mongoTemplate.updateMulti(query, u, Book.class);
    }

    @Test
    public void initScore(){
        PageRequest pageRequest = new PageRequest(0, 1000000);
        List<Book> books = bookRepository.findByPageScoreBetween(1, 100, pageRequest).getContent();
        System.out.println("---"+books.size());
        books.forEach(book -> {
            BookUrl bookUrl = bookUrlRepository.findOne(book.getCpUrl());
            bookUrl.setPageScore(book.getPageScore());
            bookUrlRepository.save(bookUrl);
        });
    }
    @Test
    public void resetChapter(){
        Map<String, SiteElement> elementMap = siteService.loadElements();

        List<BookUrl> bookUrls = mongoTemplate.find(Query.query(Criteria.where("host").is("book.qidian.com")), BookUrl.class);

//        List<BookUrl> bookUrls = bookUrlRepository.existsByChapterUrl(null);

        bookUrls.forEach(bookUrl -> {
            String host = bookUrl.getHost();
            SiteElement siteElement = elementMap.get(host);
            String link = bookUrl.getUrl();

            if (siteElement.getChapterSuffix().contains("..")){
                String suffix = link.substring(link.lastIndexOf("/"));
                String prefix = link.replace(suffix, "");
                prefix = prefix.substring(0, prefix.lastIndexOf("/"));
                String chapterUrl = prefix + siteElement.getChapterSuffix().replace("../", "/") + suffix;

                bookUrl.setChapterUrl(chapterUrl);
            }else {
                String chapterUrl = link + siteElement.getChapterSuffix();

                bookUrl.setChapterUrl(chapterUrl);
            }
            System.out.println(bookUrl.getChapterUrl());

            bookUrl.setReferenceKey(MD5.digest_16bit(bookUrl.getChapterUrl()));

            bookUrlRepository.save(bookUrl);
        });

        System.out.println(bookUrls.size());

    }
    @Test
    public void test(){
        BookElement bookElement = new BookElement();
        bookElement.setAuthor("/html/body/div[2]/div[6]/div[1]/div[2]/h1/span/a/text()");
        bookElement.setCpName("起点");
        bookElement.setDescription("/html/body/div[2]/div[6]/div[4]/div[1]/div[1]/div[1]/p/tidyText()");
        bookElement.setStatus("/html/body/div[2]/div[6]/div[1]/div[2]/p[1]/span/text()");
        bookElement.setHost("book.qidian.com");
        bookElement.setImageUrl("//*[@id=\"bookImg\"]/img/@abs:src");
        bookElement.setName("/html/body/div[2]/div[6]/div[1]/div[2]/h1/em/text()");
        bookElement.setSign("/html/body/div[2]/div[6]/div[1]/div[2]/p[1]/span/text()");
        bookElement.setTag("/html/body/div[2]/div[6]/div[1]/div[2]/p[1]/a/text()");
        bookElement.setVip("/html/body/div[2]/div[6]/div[1]/div[2]/p[1]/span/text()");
        bookElement.setOther("/html/body/div[2]/div[6]/div[1]/div[2]/p[1]/span/text()");

        bookElementRepository.save(bookElement);
    }

    @Test
    public void zongheng(){
        BookElement bookElement = new BookElement();
        bookElement.setName("/html/body/div[4]/div/div[1]/div/div/div/div[2]/h1/a/text()");
        bookElement.setAuthor("/html/body/div[4]/div/div[1]/div/div/div/div[2]/div[1]/a[1]/text()");
        bookElement.setCpName("纵横中文网");
        bookElement.setDescription("/html/body/div[4]/div/div[1]/div/div/div/div[2]/div[2]/p/tidyText()");
        bookElement.setStatus("/html/body/div[4]/div/div[1]/div/div/div/div[2]/div[1]/a[2]/text()");
        bookElement.setHost("book.zongheng.com");
        bookElement.setImageUrl("/html/body/div[4]/div/div[1]/div/div/div/div[1]/p/a/img/@abs:src");
        bookElement.setSign("/html/body/div[4]/div/div[1]/div/div/div/div[2]/h1/em[@class=\"sign    \"]");
        bookElement.setTag("/html/body/div[4]/div/div[1]/div/div/div/div[2]/div[1]/a[2]/text()");
        bookElement.setVip("/html/body/div[4]/div/div[1]/div/div/div/div[2]/h1/em[@class=\"vip\"]");
        bookElement.setOther("/html/body/div[2]/div[1]/div[1]/div[3]/div[4]/p/a/text()");

        bookElementRepository.save(bookElement);
    }
    @Test
    public void zhangyue(){
        BookElement bookElement = new BookElement();
        bookElement.setName("/html/body/div[2]/div[1]/div[1]/div[3]/div[1]/h3/a/text()");
        bookElement.setAuthor("/html/body/div[2]/div[1]/div[1]/div[3]/p/span[1]/text()");
        bookElement.setCpName("纵横");
        bookElement.setDescription("//*[@id=\"book_desc\"]/tidyText()");
        bookElement.setStatus("/html/body/div[2]/div[1]/div[2]/div[2]/p[6]/text()");
        bookElement.setHost("yc.ireader.com.cn");
        bookElement.setImageUrl("/html/body/div[2]/div[1]/div[1]/div[2]/a/img/@abs:src");
        bookElement.setSign("/html/body/div[2]/div[1]/div[2]/div[2]/p[2]/text()");
        bookElement.setTag("/html/body/div[2]/div[1]/div[2]/div[2]/p[1]/a/text()");
        bookElement.setVip("");
        bookElement.setOther("/html/body/div[2]/div[1]/div[1]/div[3]/div[4]/p/a/text()");

        bookElementRepository.save(bookElement);
    }
    @Test
    public void site(){
//        Book book_record = bookRepository.findByNameAndAuthor("我真不是神探", "卓牧闲");
//        System.out.println(book_record.getId());

        List<BookUrl> bookUrls = bookUrlRepository.existsByCrawlTime(null);
        System.out.println(bookUrls.size());
    }
}
