package com.bbcow.service;

import com.bbcow.service.mongo.entity.Book;
import com.bbcow.service.mongo.reporitory.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by adan on 2017/10/17.
 */
@Service
public class HelloService {

    @Autowired
    BookRepository bookRepository;

    public void say(){
        Book book = bookRepository.find("大主宰","天蚕土豆");
        System.out.println("---------"+book.getId());
    }
}
