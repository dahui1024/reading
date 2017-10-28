package com.bbcow.service.impl;

import com.bbcow.service.mongo.entity.SearchWord;
import com.bbcow.service.mongo.reporitory.SearchWordRepository;
import com.bbcow.service.search.RemoteSearch;
import com.bbcow.service.search.SearchPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by adan on 2017/10/21.
 */
@Service
public class SearchService {

    @Autowired
    SearchWordRepository searchWordRepository;

    public List<SearchPO> search(String word){
        searchWordRepository.count(word);

        return RemoteSearch.search(word);
    }
    public List<SearchWord> getHotWords(){

        return searchWordRepository.getHotWords();

    }
}
