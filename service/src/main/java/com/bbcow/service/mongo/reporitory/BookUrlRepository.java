package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.BookUrl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
public interface BookUrlRepository extends MongoRepository<BookUrl, String> , PagingAndSortingRepository<BookUrl, String> {
    List<BookUrl> existsByCrawlTime(Date date);
}
