package com.bbcow.crawler.proxy;

import com.bbcow.service.mongo.entity.*;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by adan on 2017/10/18.
 */
public class FindProxy {

    private Map<String, SiteElement> elementMap;

    public FindProxy(Map<String, SiteElement> elementMap){
        this.elementMap = elementMap;
    }
    public List<BookUrl> getBookUrls(Page page) {
        try {
            String host = new URL(page.getUrl().get()).getHost();
            SiteElement siteElement = elementMap.get(host);
            Html html = page.getHtml();
            List<String> links = html.xpath(siteElement.getTarget()).links().all();

            List<BookUrl> bookUrls = new LinkedList<>();


            Date now = new Date();
            links.forEach(link -> {
                BookUrl bookUrl = new BookUrl();
                bookUrl.setHost(host);
                bookUrl.setUrl(link);
                bookUrl.setCreate_time(now);
                bookUrls.add(bookUrl);

            });
            return bookUrls;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<SiteUrl> getSites(Page page) {
        try {
            String host = new URL(page.getUrl().get()).getHost();
            SiteElement siteElement = elementMap.get(host);
            Html html = page.getHtml();
            List<String> links = html.xpath(siteElement.getUrl()).links().all();

            List<SiteUrl> siteUrls = new LinkedList<>();


            links.forEach(link -> {
                if (!StringUtils.isEmpty(link)){
                    SiteUrl siteUrl = new SiteUrl();
                    siteUrl.setHost(host);
                    siteUrl.setUrl(link);

                    siteUrls.add(siteUrl);
                }
            });

            return siteUrls;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return Collections.emptyList();
    }
}
