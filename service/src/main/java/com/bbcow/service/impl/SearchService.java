package com.bbcow.service.impl;

import com.bbcow.service.mongo.entity.SearchWord;
import com.bbcow.service.mongo.reporitory.SearchWordRepository;
import com.bbcow.service.search.RemoteSearch;
import com.bbcow.service.search.SearchPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by adan on 2017/10/21.
 */
@Service
public class SearchService {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    SearchWordRepository searchWordRepository;

    public List<SearchPO> search(String word){

        Query query = new Query();
        query.addCriteria(Criteria.where("word").is(word));

        Update update = new Update();
        update.inc("count", 1);
        update.set("word", word);
        mongoTemplate.upsert(query, update, SearchWord.class);

        return RemoteSearch.search(word);
    }
    public List<SearchWord> getHotWords(){

        return searchWordRepository.findTop30ByCountGreaterThan(0, new Sort(Sort.Direction.DESC, "count"));
    }
}
