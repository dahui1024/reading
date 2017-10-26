package com.bbcow.service.mongo;

import com.bbcow.service.mongo.entity.SearchWord;
import com.bbcow.service.mongo.reporitory.SearchWordRepository;
import org.bson.BasicBSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by adan on 2017/10/22.
 */
@Component
public class SearchWordRepositoryImpl implements SearchWordRepository {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void count(String word) {
        BasicBSONObject basicBSONObject = new BasicBSONObject();

        Query query = new Query();
        query.addCriteria(Criteria.where("word").is(word));

        Update update = new Update();
        update.inc("count", 1);
        update.set("word", word);
        mongoTemplate.upsert(query, update, SearchWord.class);
    }

    @Override
    public List<SearchWord> getHotWords() {
        Query query = new Query();
        Sort sort = new Sort(Sort.Direction.DESC, "count");
        query.with(sort);
        query.limit(10);
        return mongoTemplate.find(query, SearchWord.class);
    }
}
