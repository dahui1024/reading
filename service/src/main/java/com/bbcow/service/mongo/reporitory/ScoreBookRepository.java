package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.Book;
import com.bbcow.service.mongo.entity.ScoreBook;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by adan on 2017/10/17.
 */
public interface ScoreBookRepository extends MongoRepository<ScoreBook, String> , PagingAndSortingRepository<ScoreBook, String> {
    Book findByName(String name);
}
