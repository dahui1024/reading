package com.bbcow.crawler.book;

import com.bbcow.crawler.TaskCrawler;
import com.bbcow.crawler.book.processor.FetchCoverProcessor;
import com.bbcow.service.impl.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class FetchCoverCrawler extends TaskCrawler<FetchCoverProcessor> {
    @Autowired
    BookService bookService;

    public FetchCoverCrawler(@Autowired FetchCoverProcessor fetchCoverProcessor) {
        super(fetchCoverProcessor);
    }

    @Override
    public void execute() {
        bookService.getNewBookUrl().forEach(bookUrls -> {
            spider.addUrl(bookUrls.getUrl());
        });

        spider.thread(1).start();
    }

}
