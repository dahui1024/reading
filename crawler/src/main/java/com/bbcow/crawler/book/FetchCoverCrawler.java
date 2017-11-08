package com.bbcow.crawler.book;

import com.bbcow.crawler.DefaultCrawler;
import com.bbcow.crawler.book.processor.FetchCoverProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class FetchCoverCrawler extends DefaultCrawler<FetchCoverProcessor> {

    public FetchCoverCrawler(@Autowired FetchCoverProcessor fetchCoverProcessor) {
        super(fetchCoverProcessor);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("auto run");
    }
}
