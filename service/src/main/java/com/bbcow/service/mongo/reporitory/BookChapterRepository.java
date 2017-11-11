package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.BookChapter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
public interface BookChapterRepository extends MongoRepository<BookChapter, String> , PagingAndSortingRepository<BookChapter, String> {
    BookChapter findByUrl(String url);
    List<BookChapter> findByReferenceKey(String referenceKey);
}
