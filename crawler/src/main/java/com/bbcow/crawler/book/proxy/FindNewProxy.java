package com.bbcow.crawler.book.proxy;

import com.bbcow.service.mongo.entity.BookUrl;
import com.bbcow.service.mongo.entity.SiteElement;
import com.bbcow.service.util.MD5;
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
                if (siteElement.getChapterSuffix().contains("..")){
                    String suffix = link.substring(link.lastIndexOf("/"));
                    String prefix = link.replace(suffix, "");
                    prefix = prefix.substring(0, prefix.lastIndexOf("/"));
                    String chapterUrl = prefix + siteElement.getChapterSuffix().replace("../", "/") + suffix;

                    bookUrl.setChapterUrl(chapterUrl);
                }else {
                    String chapterUrl = link + siteElement.getChapterSuffix();

                    bookUrl.setChapterUrl(chapterUrl);
                }

                bookUrl.setChapterStatus(0);
                bookUrl.setReferenceKey(MD5.digest_16bit(bookUrl.getChapterUrl()));
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
