package com.bbcow.crawler.book.processor;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class FetchCoverProcessor implements PageProcessor {
    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        return Site.me();
    }
}
