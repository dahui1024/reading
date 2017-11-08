package com.bbcow.crawler.site;

import com.bbcow.crawler.CrawlerProperties;
import com.bbcow.service.impl.BookService;
import com.bbcow.service.impl.ScoreService;
import com.bbcow.service.mongo.entity.ScoreSite;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
import java.util.function.Predicate;

/**
 * Created by adan on 2017/10/18.
 */
@Component
public class ScoreCrawler{
    @Autowired
    ScoreService scoreService;
    @Autowired
    CrawlerProperties crawlerProperties;
    @Autowired
    BookService bookService;
    int basicScore = 10;

    public void crawl() throws Exception {
        if (!crawlerProperties.isScore()){
            return;
        }
        Date day = DateUtils.truncate(new Date(), Calendar.DATE);

        OOSpider bookSpider = new OOSpider(new ScoreCrawler.Processor());
        // 一天限制爬取一次
        scoreService.findEnableSite().stream().filter(scoreSite -> {
            if (scoreSite.getCrawlTime() == null)
                return true;
            return scoreSite.getCrawlTime().before(day);
        }).forEach(scoreSite -> bookSpider.addUrl("http://" + scoreSite.getHost()));
        bookSpider.setExitWhenComplete(true);
        bookSpider.thread(1).start();
    }

    @Scheduled(cron = "0 0 5 * * ?")
    public void dayCrawl(){
        try {
            crawl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Processor implements PageProcessor{

        @Override
        public void process(Page page) {
            Document document = page.getHtml().getDocument();
            Elements elements = document.select("a");
            URI uri = null;
            try {
                uri = new URI(page.getUrl().get());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            int usefulLinkCount = 0;
            Set<String> names = new HashSet<>();
            int score = elements.size() * basicScore;

            for (Element element : elements){
                String href = element.absUrl("href");
                String name = element.text();

                if (!names.contains(name) && bookService.existsWithName(name)){
                    scoreService.updateBookPageScore(name, score/elements.size());
                    names.add(name);

                    usefulLinkCount += 1;
                }else {
                    continue;
                }

                score -= basicScore;
            }

            scoreService.finishCrawl(uri.getHost(), usefulLinkCount);
        }

        @Override
        public Site getSite() {
            return Site.me();
        }
    }
}
