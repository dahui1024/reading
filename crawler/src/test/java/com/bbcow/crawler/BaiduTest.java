package com.bbcow.crawler;

import com.bbcow.service.impl.BookService;
import com.bbcow.service.mongo.entity.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

/**
 * Created by adan on 2017/11/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BaiduTest {
    @Autowired
    BookService bookService;

    @Test
    public void testSDK(){
        List<Book> books = bookService.getTop50();
        Baidu example = new Baidu();
        try {
            books.forEach(book -> {
                String response = null;
                try {
                    response = example.post("http://data.zz.baidu.com/urls?appid=1584272942248118&token=VMF6nZrGqwQ1wwJr&type=realtime", "http://www.bbcow.com/books/" + book.getId().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println(response);
            });
        }catch (Exception e){

        }
    }
}
