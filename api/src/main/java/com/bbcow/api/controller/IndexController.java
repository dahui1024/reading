package com.bbcow.api.controller;

import com.bbcow.service.impl.BookService;
import com.bbcow.service.mongo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
@RestController
public class IndexController {
    @Autowired
    BookService bookService;
    @RequestMapping("/v1/sitemap/books")
    String books(HttpServletResponse response) {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        StringBuilder stringBuilder = new StringBuilder();

        List<Book> books = bookService.getAvailable();
        books.forEach(book -> stringBuilder.append("http://www.bbcow.com/books/"+book.getId().toString()+"\r\n"));

        return stringBuilder.toString();
    }
}
