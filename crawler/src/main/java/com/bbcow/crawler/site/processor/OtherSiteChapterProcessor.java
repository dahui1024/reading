package com.bbcow.crawler.site.processor;

import com.bbcow.crawler.site.core.ChapterLinkMarker;
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
public class OtherSiteChapterProcessor implements PageProcessor {
    Logger logger = LoggerFactory.getLogger(OtherSiteChapterProcessor.class);
    BookSiteService bookSiteService;
    StringRedisTemplate stringRedisTemplate;
    public OtherSiteChapterProcessor(BookSiteService bookSiteService, StringRedisTemplate stringRedisTemplate){
        this.bookSiteService = bookSiteService;
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Override
    public void process(Page page) {
        String rk = page.getRequest().getHeaders().get("rk");
        System.out.println("----"+rk);
        URL url = null;
        try {
            url = new URL(page.getRequest().getUrl());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ChapterLinkMarker.LinkNode root = ChapterLinkMarker.parseNode(page);

        List<BookSiteChapter> chapterLinks = ChapterLinkMarker.parseLink(root, url.getProtocol() +":/");

        save(rk, chapterLinks);

        logger.info(rk + " finished!");
    }

    public void save(String rk, List<BookSiteChapter> chapterLinks){
        List<BookSiteChapter> cleanChapterLinks = new LinkedList<>();
        chapterLinks.stream().filter(c -> StringUtils.containsAny(c.getName(), "第", "章", " ")).forEach(c -> {
            cleanChapterLinks.add(c);
        });
        AtomicInteger sn = new AtomicInteger(cleanChapterLinks.size());

        BookSiteChapter lastRecord = bookSiteService.getLastOne(rk);
        BookSiteChapter last = cleanChapterLinks.get(sn.get() - 1);
        if (lastRecord.getSn() != sn.get() || !lastRecord.getName().equals(last.getName())){
            logger.info("chapter not same!");
            List<String> rks = stringRedisTemplate.opsForList().range("site:queue:rk", 0, -1);
            rks.forEach(ork -> {
                if (ork.equals(rk)){
                    return;
                }
            });
            stringRedisTemplate.opsForList().leftPush("site:queue:rk", rk);
            return;
        }

        while (sn.decrementAndGet() >= 0){
            try{
                String id = rk+"_"+(sn.get()+1);

                BookSiteChapter record = cleanChapterLinks.get(sn.get());

                bookSiteService.updateSite(id, record.getName(), record.getUrl());
            }catch (Exception e){

            }
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