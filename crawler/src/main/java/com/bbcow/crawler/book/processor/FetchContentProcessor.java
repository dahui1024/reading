package com.bbcow.crawler.book.processor;

import com.bbcow.crawler.book.proxy.FetchContentProxy;
import com.bbcow.service.impl.BookService;
import com.bbcow.service.mongo.entity.BookChapter;
import com.bbcow.service.util.MD5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class FetchContentProcessor implements PageProcessor {
    BookService bookService;
    FetchContentProxy fetchContentProxy;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    public FetchContentProcessor(@Autowired BookService bookService){
        this.bookService = bookService;
        this.fetchContentProxy = new FetchContentProxy(bookService.loadElements());
    }

    @Override
    public void process(Page page) {
        String url = page.getRequest().getUrl();
        String rk = null;

        String text = fetchContentProxy.getContent(page);
        if (StringUtils.isNotEmpty(text)){
            BookChapter bookChapter = bookService.updateChapterContent(url, text);
            rk = bookChapter.getReferenceKey();
        }

        if (stringRedisTemplate.opsForValue().increment("counter:rk:"+rk, -1) <= 0){
            bookService.finishChapterUrl(rk);
        }

    }

    @Override
    public Site getSite() {
        return Site.me();
    }
}
