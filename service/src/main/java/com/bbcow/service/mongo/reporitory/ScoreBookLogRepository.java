package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.ScoreBookLog;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
public interface ScoreBookLogRepository extends MongoRepository<ScoreBookLog, ObjectId> , PagingAndSortingRepository<ScoreBookLog, ObjectId> {
    ScoreBookLog findByNameAndDay(String name, Date day);
    long countByName(String name);
    List<ScoreBookLog> findByDay(Date day);

    List<ScoreBookLog> findTop30ByName(String name, Sort sort);
    List<ScoreBookLog> findByPageScoreGreaterThan(int pageScore);
}
