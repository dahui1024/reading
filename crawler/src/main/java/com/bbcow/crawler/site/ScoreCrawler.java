package com.bbcow.crawler.site;

import com.bbcow.crawler.CrawlerProperties;
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
    int basicScore = 10;

    public void crawl() throws Exception {
        if (!crawlerProperties.isScore()){
            return;
        }
        Date day = DateUtils.truncate(new Date(), Calendar.DATE);

        OOSpider bookSpider = new OOSpider(new ScoreCrawler.Processor());
        // 一天限制爬取一次
        scoreService.findEnabelSite().stream().filter(new Predicate<ScoreSite>() {
            @Override
            public boolean test(ScoreSite scoreSite) {
                if (scoreSite.getCrawl_time() == null)
                    return true;
                return scoreSite.getCrawl_time().before(day);
            }
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

            URI uri;
            try {
                uri = new URI(page.getUrl().get());
                scoreService.finishCrawl(uri.getHost());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            Set<String> names = new HashSet<>();

            Elements elements = document.select("a");
            int score = elements.size() * basicScore;

            for (Element element : elements){
                String href = element.absUrl("href");
                String name = element.text();
                if (StringUtils.length(name) > 20 || StringUtils.startsWith(name, "第") || StringUtils.contains(name, "章 ") || StringUtils.endsWithAny(name, "下载", "网", "小说")){
                    continue;
                }

                    score -= basicScore;
                if (!names.contains(name) && StringUtils.isNotEmpty(name)){
                    scoreService.updateBookPageScore(name, score/elements.size());
                    names.add(name);
                    System.out.println(name+"--"+score/elements.size());
                }

            }

        }

        @Override
        public Site getSite() {
            return Site.me();
        }
    }
}
