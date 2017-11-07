package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.ScoreBook;
import com.bbcow.service.mongo.entity.ScoreBookLog;
import org.bson.types.ObjectId;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
public interface ScoreBookLogRepository extends MongoRepository<ScoreBookLog, ObjectId> , PagingAndSortingRepository<ScoreBookLog, ObjectId> {
    ScoreBook findByNameAndDay(String name, Date day);
    List<ScoreBookLog> findByDay(Date day);

    default List<ScoreBookLog> findNewly30ByName(String name){
        Pageable pageable = new PageRequest(0, 30, Sort.Direction.ASC, "day");

        ScoreBookLog scoreBookLog = new ScoreBookLog();
        scoreBookLog.setName(name);


        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());


        Example<ScoreBookLog> example = Example.of(scoreBookLog, exampleMatcher);
        System.out.println(example);
        return findAll(example, pageable).getContent();

    }
}
