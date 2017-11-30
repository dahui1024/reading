package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.BookSiteChapter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


/**
 * Created by adan on 2017/10/17.
 */
public interface BookSiteChapterRepository extends MongoRepository<BookSiteChapter, String> , PagingAndSortingRepository<BookSiteChapter, String> {
    BookSiteChapter findOneByReferenceKey(String key, Sort sort);
    long countById(String id);
    BookSiteChapter findByIdAndStatus(String id, int status);
}
