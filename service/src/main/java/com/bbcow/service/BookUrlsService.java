package com.bbcow.service;

import com.bbcow.service.mongo.entity.BookUrls;
import com.bbcow.service.mongo.reporitory.BookUrlsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
@Service
public class BookUrlsService {

    @Autowired
    BookUrlsRepository bookUrlsRepository;

    public void save(String url, String host){
        if (bookUrlsRepository.findOne(url) == null){
            BookUrls bookUrls = new BookUrls();
            bookUrls.setHost(host);
            bookUrls.setUrl(url);
            bookUrlsRepository.save(bookUrls);
        }
    }

    public void finish(String url){
        BookUrls bookUrls = bookUrlsRepository.findOne(url);
        bookUrls.setCrawl_time(new Date());
        bookUrlsRepository.save(bookUrls);
    }

    public List<BookUrls> getQueue(){
        PageRequest pageRequest = new PageRequest(1, 1, Sort.Direction.ASC, "crawl_time");
        return bookUrlsRepository.findAll(pageRequest).getContent();
    }


}
