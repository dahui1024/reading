package com.bbcow.service;

import com.bbcow.service.mongo.entity.Book;
import com.bbcow.service.mongo.reporitory.BookRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by adan on 2017/10/17.
 */
@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    public void save(Book book){
        if (book.getName() == null || book.getAuthor() == null) {
            return;
        }
        bookRepository.save(book);
    }

    public Book getById(ObjectId id) {
        return bookRepository.find(id);
    }
}
