package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.SearchWord;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by adan on 2017/10/22.
 */
public interface SearchWordRepository extends Repository<SearchWord, String> {

    void count(String word);

    List<SearchWord> getHotWords();
}
