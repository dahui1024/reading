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
import org.springframework.data.domain.PageRequest;
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
        long count = bookRepository.count();
        long page = count/100 + 1;

        for (int i = 0; i < page; i++) {
            List<Book> books = bookRepository.findAll(new PageRequest(i, 100)).getContent();
            RemoteUpload.upload(books);

            System.out.println("page: "+ i);
        }

    }

}
