package com.bbcow.crawler.score.processor;

import com.bbcow.service.impl.BookService;
import com.bbcow.service.impl.BookSiteService;
import com.bbcow.service.impl.ScoreService;
import com.bbcow.service.mongo.entity.Book;
import java.util.List;
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
    BookSiteService bookSiteService;
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

            List<Book> books = bookService.getByName(name);

            if (!names.contains(name) && !books.isEmpty()){
                int finalScore = score/elements.size();
                scoreService.updateBookPageScore(name, href, finalScore);
                names.add(name);
                usefulLinkCount += 1;

                books.forEach(book -> {
                    bookSiteService.addSite(book.getReferenceKey(), finalScore, href);
                });

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
