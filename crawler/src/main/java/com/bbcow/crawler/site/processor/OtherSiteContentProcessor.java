package com.bbcow.crawler.site.processor;

import com.bbcow.crawler.site.core.ChapterContentMarker;
import com.bbcow.crawler.site.core.ChapterLinkMarker;
import com.bbcow.service.impl.BookSiteService;
import com.bbcow.service.mongo.entity.BookSiteChapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by adan on 2017/11/23.
 */
public class OtherSiteContentProcessor implements PageProcessor {
    Logger logger = LoggerFactory.getLogger(OtherSiteContentProcessor.class);
    BookSiteService bookSiteService;
    StringRedisTemplate stringRedisTemplate;
    public OtherSiteContentProcessor(BookSiteService bookSiteService, StringRedisTemplate stringRedisTemplate){
        this.bookSiteService = bookSiteService;
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Override
    public void process(Page page) {
        String id = page.getRequest().getHeaders().get("id");

        String content = ChapterContentMarker.getContent(page);

        if (StringUtils.isBlank(content)){

            // 放入章节内容抓取队列
            if (!stringRedisTemplate.hasKey("chapter:lock:"+id)) {
                stringRedisTemplate.opsForList().leftPush("chapter:queue:id", id);
                stringRedisTemplate.opsForValue().set("chapter:lock:" + id, "0");
            }
        }

        bookSiteService.updateContent(id, content);
    }


    @Override
    public Site getSite() {
        return Site.me();
    }

}
