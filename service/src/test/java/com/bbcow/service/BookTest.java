package com.bbcow.service;

import com.bbcow.service.mongo.entity.Book;
import com.bbcow.service.mongo.entity.BookElement;
import com.bbcow.service.mongo.entity.BookUrl;
import com.bbcow.service.mongo.entity.SiteElement;
import com.bbcow.service.mongo.reporitory.BookElementRepository;
import com.bbcow.service.mongo.reporitory.BookRepository;
import com.bbcow.service.mongo.reporitory.BookUrlRepository;
import com.bbcow.service.mongo.reporitory.SiteElementRepository;
import com.bbcow.service.search.RemoteUpload;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

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

    @Test
    public void s(){
        bookRepository.findOne(new ObjectId("59e74c37a316861935b14fff"));
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
