package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.BookWord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by adan on 2017/10/17.
 */
public interface BookWordRepository extends MongoRepository<BookWord, String> , PagingAndSortingRepository<BookWord, String> {

}
