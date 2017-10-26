package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.Book;
import org.bson.types.ObjectId;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
public interface BookRepository extends Repository<Book, ObjectId> {
     List<Book> find();
     Book find(ObjectId id);
     Book find(String name, String author);
     void save(Book book);
}
