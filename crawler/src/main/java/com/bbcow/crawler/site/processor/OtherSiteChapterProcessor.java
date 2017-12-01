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
import java.util.concurrent.TimeUnit;
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

        URL url = null;
        try {
            url = new URL(page.getRequest().getUrl());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ChapterLinkMarker.LinkNode root = ChapterLinkMarker.parseNode(page);

        List<BookSiteChapter> chapterLinks = ChapterLinkMarker.parseLink(root, url.getProtocol() +":/");

        save(rk, chapterLinks);

        logger.info(rk + " other finished!");
    }

    public void save(String rk, List<BookSiteChapter> chapterLinks){
        List<BookSiteChapter> cleanChapterLinks = new LinkedList<>();
        chapterLinks.stream().filter(c -> StringUtils.containsAny(c.getName(), "第", "章", " ")).forEach(c -> {
            cleanChapterLinks.add(c);
        });
        AtomicInteger sn = new AtomicInteger(cleanChapterLinks.size());

        BookSiteChapter lastRecord = bookSiteService.getLastOne(rk);
        if (sn.get() < 0 || cleanChapterLinks.isEmpty()){
            return;
        }
        if (lastRecord == null || lastRecord.getStatus() == 3){
            return;
        }
        BookSiteChapter last = cleanChapterLinks.get(sn.get() - 1);

        if (lastRecord.getSn() == sn.get() && lastRecord.getName().equals(last.getName())){

        }else {
            logger.info("chapter not same!");
            if (!stringRedisTemplate.hasKey("site:lock:"+rk)){
                stringRedisTemplate.opsForList().leftPush("site:queue:rk", rk);
                stringRedisTemplate.opsForValue().set("site:lock:"+rk, "0");
            }
            return;
        }

        while (sn.decrementAndGet() >= 0){
            try{
                String id = rk+"_"+(sn.get()+1);

                BookSiteChapter record = cleanChapterLinks.get(sn.get());

                int n = bookSiteService.updateSite(id, record.getName(), record.getUrl());
                // 放入章节内容抓取队列
//                if (n > 0 && !stringRedisTemplate.hasKey("chapter:lock:"+id)) {
//                    stringRedisTemplate.opsForList().leftPush("chapter:queue:id", id);
//                    stringRedisTemplate.opsForValue().set("chapter:lock:" + id, "0");
//                }
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
