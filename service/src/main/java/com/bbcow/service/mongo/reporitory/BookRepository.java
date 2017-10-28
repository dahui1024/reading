package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.Book;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by adan on 2017/10/17.
 */
public interface BookRepository extends MongoRepository<Book, ObjectId> , PagingAndSortingRepository<Book, ObjectId> {
    Book findByNameAndAuthor(String name, String author);
}
