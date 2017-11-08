package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.SearchWord;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by adan on 2017/10/22.
 */
public interface SearchWordRepository extends MongoRepository<SearchWord, String> , PagingAndSortingRepository<SearchWord, String> {

    List<SearchWord> findTop30ByCountGreaterThan(int count, Sort sort);
}
