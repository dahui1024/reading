package com.bbcow.crawler.book.proxy;

import com.bbcow.service.mongo.entity.BookUrl;
import com.bbcow.service.mongo.entity.SiteElement;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class FindNewProxy {
    private Map<String, SiteElement> elementMap;

    public FindNewProxy(Map<String, SiteElement> elementMap){
        this.elementMap = elementMap;
    }
    public List<BookUrl> getBookUrls(Page page) {
        try {
            String host = new URL(page.getUrl().get()).getHost();
            SiteElement siteElement = elementMap.get(host);
            Html html = page.getHtml();
            List<String> links = html.links().regex(siteElement.getUrlRegex()).all();

            List<BookUrl> bookUrls = new LinkedList<>();

            Date now = new Date();
            links.forEach(link -> {
                BookUrl bookUrl = new BookUrl();
                bookUrl.setHost(host);
                bookUrl.setUrl(link);
                bookUrl.setCreateTime(now);
                bookUrls.add(bookUrl);

            });
            return bookUrls;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}
