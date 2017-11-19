package com.bbcow.crawler.book.processor;

import com.bbcow.crawler.book.proxy.FetchChapterProxy;
import com.bbcow.service.impl.BookService;
import com.bbcow.service.util.MD5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Collections;
import java.util.List;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class FetchChapterProcessor implements PageProcessor {
    BookService bookService;
    FetchChapterProxy fetchChapterProxy;
    public FetchChapterProcessor(@Autowired BookService bookService){
        this.bookService = bookService;
        this.fetchChapterProxy = new FetchChapterProxy(bookService.loadElements());
    }

    @Override
    public void process(Page page) {
        String url = page.getRequest().getUrl();

        List<String> urls;
        try {
            urls = fetchChapterProxy.getUrl(page);

            if (!urls.isEmpty()){
                for (int i = 0; i < urls.size(); i++) {
                    page.addTargetRequest(urls.get(i));
                    bookService.saveChapterUrl(i+1, urls.get(i), MD5.digest_16bit(url));
                }
                bookService.finishChapterUrl(MD5.digest_16bit(url));
            }
        }catch (Exception e){
            bookService.disableChapterUrl(MD5.digest_16bit(url));
            return;
        }

        String text = fetchChapterProxy.getContent(page);
        if (StringUtils.isNotEmpty(text)){
            bookService.updateChapterContent(url, text);
        }
    }

    @Override
    public Site getSite() {
        return Site.me();
    }
}
