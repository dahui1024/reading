package com.bbcow.crawler.site.processor;

import com.bbcow.crawler.site.core.ChapterLinkMarker;
import com.bbcow.crawler.site.core.ChapterLinkMarker2;
import com.bbcow.service.impl.BookSiteService;
import com.bbcow.service.mongo.entity.BookSiteChapter;
import com.bbcow.service.util.MD5;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by adan on 2017/11/23.
 */
public class SiteChapterProcessor implements PageProcessor {
    Logger logger = LoggerFactory.getLogger(SiteChapterProcessor.class);
    BookSiteService bookSiteService;
    StringRedisTemplate stringRedisTemplate;
    public SiteChapterProcessor(BookSiteService bookSiteService, StringRedisTemplate stringRedisTemplate){
        this.bookSiteService = bookSiteService;
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Override
    public void process(Page page) {
        String rk = MD5.digest_16bit(page.getRequest().getUrl());
        URL url = null;
        try {
            url = new URL(page.getRequest().getUrl());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

//        ChapterLinkMarker.LinkNode root = ChapterLinkMarker.parseNode(page);
//
//        List<BookSiteChapter> chapterLinks = ChapterLinkMarker.parseLink(root, url.getProtocol() +":/");

        List<ChapterLinkMarker2.ChapterUrl> urls = ChapterLinkMarker2.getLinks(page.getHtml().getDocument());

        save(rk, urls);

        logger.info(rk + " finished!");
    }

    public void save(String rk, List<ChapterLinkMarker2.ChapterUrl> urls){
//        List<BookSiteChapter> cleanChapterLinks = new LinkedList<>();
//        chapterLinks.stream().filter(c -> StringUtils.containsAny(c.getName(), "第", "章", " ", "篇")).forEach(c -> {
//            cleanChapterLinks.add(c);
//        });
        AtomicInteger sn = new AtomicInteger(urls.size());
        int updateCount = 0;
        while (sn.decrementAndGet() >= 0){
            String id = rk+"_"+(sn.get()+1);

            BookSiteChapter record = bookSiteService.getChapter(id);

            if (record == null){
                BookSiteChapter newRecord = new BookSiteChapter();
                newRecord.setUrl(urls.get(sn.get()).url);
                newRecord.setName(urls.get(sn.get()).name);
                newRecord.setCreateTime(new Date());
                newRecord.setSn(sn.get()+1);
                newRecord.setId(rk+"_"+newRecord.getSn());
                newRecord.setReferenceKey(rk);
                newRecord.setStatus(1);
                newRecord.setUpdateTime(newRecord.getCreateTime());

                bookSiteService.save(newRecord);
                updateCount+=1;
            }else {
                break;
            }
        }
        if (updateCount > 0){
            stringRedisTemplate.opsForList().leftPush("site:queue:rk", rk);
            stringRedisTemplate.opsForValue().set("site:lock:"+rk, "0");
        }
    }


    @Override
    public Site getSite() {
        return Site.me();
    }



    public static void main(String[] args) {
        String uri = "http://www.77xs.co/book_41575/#footer";

        System.out.println(uri.replaceAll("([#][\\S]+)", ""));
    }
}
