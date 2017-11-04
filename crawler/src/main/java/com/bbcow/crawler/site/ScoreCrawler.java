package com.bbcow.crawler.site;

import com.bbcow.crawler.CrawlerProperties;
import com.bbcow.service.impl.ScoreService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by adan on 2017/10/18.
 */
@Component
public class ScoreCrawler implements CommandLineRunner{
    @Autowired
    ScoreService scoreService;
    @Autowired
    CrawlerProperties crawlerProperties;
    int basicScore = 10;
    @Override
    public void run(String... strings) throws Exception {
        if (!crawlerProperties.isScore()){
            return;
        }
        OOSpider bookSpider = new OOSpider(new ScoreCrawler.Processor());
        scoreService.findAll().forEach(scoreSite -> bookSpider.addUrl("http://" + scoreSite.getHost()));
        bookSpider.setExitWhenComplete(true);
        bookSpider.thread(1).start();
    }
    class Processor implements PageProcessor{

        @Override
        public void process(Page page) {
            Document document = page.getHtml().getDocument();

            Set<String> names = new HashSet<>();

            Elements elements = document.select("a");
            int score = elements.size() * basicScore;

            for (Element element : elements){
                String href = element.absUrl("href");
                String name = element.text();

                score -= basicScore;

                if (!names.contains(name) && StringUtils.isNotEmpty(name)){
                    scoreService.updateBookPageScore(name, score/elements.size());
                    names.add(name);
                    System.out.println(name+"--"+score/elements.size());
                }

            }

            try {
                URI uri = new URI(page.getUrl().get());
                scoreService.finishCrawl(uri.getHost());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }

        @Override
        public Site getSite() {
            return Site.me();
        }
    }
}
