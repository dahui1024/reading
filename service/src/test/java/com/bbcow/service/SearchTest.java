package com.bbcow.service;

import com.bbcow.service.mongo.entity.Book;
import com.bbcow.service.mongo.entity.BookElement;
import com.bbcow.service.mongo.reporitory.BookElementRepository;
import com.bbcow.service.mongo.reporitory.BookRepository;
import com.bbcow.service.search.RemoteUpload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by adan on 2017/10/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("service-dev")
public class SearchTest {
    @Autowired
    BookRepository bookRepository;

    @Test
    public void test(){
        List<Book> books = bookRepository.findAll();

        RemoteUpload.upload(books);
    }

}
