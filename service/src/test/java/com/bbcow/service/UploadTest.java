package com.bbcow.service;

import com.bbcow.service.mongo.reporitory.BookRepository;
import com.bbcow.service.search.RemoteUpload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by adan on 2017/10/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UploadTest {
    @Autowired
    BookRepository bookRepository;

    @Test
    public void test(){
        RemoteUpload.upload(bookRepository.find());
        System.out.println("----");
    }
}
