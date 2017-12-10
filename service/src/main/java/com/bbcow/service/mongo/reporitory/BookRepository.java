package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.Book;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
public interface BookRepository extends MongoRepository<Book, ObjectId> , PagingAndSortingRepository<Book, ObjectId> {
    Book findByNameAndAuthor(String name, String author);
    List<Book> findByName(String name);
    Book findByReferenceKey(String referenceKey);
    Page<Book> findByAuthor(String author, Pageable pageable);
    Page<Book> findByPageScoreBetween(int from, int to, Pageable pageable);
    Page<Book> findByPageScoreGreaterThanAndPageCountGreaterThan(int pageScore, int pageCount, Pageable pageable);
    List<Book> findTop50ByIsSignOrderByPageScoreDescPageCountDesc(int isSign);
    List<Book> findByIsFinishOrderByPageScoreDesc(int isFinish, Pageable pageable);
    Book findByCpUrl(String url);
}
