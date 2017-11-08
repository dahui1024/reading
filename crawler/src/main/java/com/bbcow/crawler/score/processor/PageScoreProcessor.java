package com.bbcow.crawler.score.processor;

import com.bbcow.crawler.CrawlerProperties;
import com.bbcow.service.impl.BookService;
import com.bbcow.service.impl.ScoreService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by adan on 2017/11/8.
 */
@Component
public class PageScoreProcessor implements PageProcessor {
    @Autowired
    ScoreService scoreService;
    @Autowired
    BookService bookService;
    int basicScore = 10;

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
