package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.BookSite;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
public interface BookSiteRepository extends MongoRepository<BookSite, ObjectId> , PagingAndSortingRepository<BookSite, ObjectId> {
    BookSite findOneByReferenceKeyAndUrl(String rk, String url);
    List<BookSite> findByReferenceKeyAndStatusOrderByScoreDesc(String rk, int status);
}
