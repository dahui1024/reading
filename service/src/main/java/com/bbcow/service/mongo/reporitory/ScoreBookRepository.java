package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.ScoreBook;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
public interface ScoreBookRepository extends MongoRepository<ScoreBook, String> , PagingAndSortingRepository<ScoreBook, String> {
    ScoreBook findByNameAndDay(String name, Date day);
    List<ScoreBook> findByDay(Date day);

    List<ScoreBook> findTop250ByDay(Date day, Sort sort);
}
