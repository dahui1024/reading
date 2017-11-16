package com.bbcow.crawler.book.proxy;

import com.bbcow.service.mongo.entity.*;
import com.bbcow.service.util.MD5;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by adan on 2017/10/18.
 */
public class FetchUrlProxy {

    private Map<String, SiteElement> elementMap;
    private Map<String, Integer> countMap = new HashMap<>();

    public FetchUrlProxy(Map<String, SiteElement> elementMap){
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

    public List<SiteUrl> getSites(Page page) {
        try {
            String host = new URL(page.getUrl().get()).getHost();
            SiteElement siteElement = elementMap.get(host);
            Html html = page.getHtml();
            List<String> links = html.xpath(siteElement.getUrl()).links().all();


            return urlProcessor(host, links);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return Collections.emptyList();
    }

    private List<SiteUrl> urlProcessor(String host, List<String> links){
        List<SiteUrl> siteUrls = new LinkedList<>();
        String template = null;

        int count = 0;

        if (!links.isEmpty()){
            for (String link : links){
                if (link.contains("page=")){
                    template = link.replaceFirst("page=[\\d]*", "page=@count");
                    int index = link.indexOf("page=") + 5;
                    int lastSymbolIndex = link.lastIndexOf("&");
                    if (lastSymbolIndex > index){
                        int endIndex = link.indexOf("&", index);
                        if (Integer.valueOf(link.substring(index, endIndex)) > count){
                            count = Integer.valueOf(link.substring(index, endIndex));
                        }
                    } else {
                        if (Integer.valueOf(link.substring(index)) > count){
                            count = Integer.valueOf(link.substring(index));
                        }
                    }
                }
            }
            // 初始化
            if (template != null && !countMap.containsKey(template)){
                countMap.put(template, 1);
            }
        }
        if (template != null && count > countMap.get(template)){
            for (int i = countMap.get(template); i < count; i++) {
                SiteUrl siteUrl = new SiteUrl();
                siteUrl.setHost(host);
                siteUrl.setUrl(template.replace("@count", i+""));
                siteUrls.add(siteUrl);
            }
            // 更新最大页数
            countMap.put(template, count);
        }else if (template != null && count <= countMap.get(template)){

        }else {
            links.forEach(link -> {
                if (!StringUtils.isEmpty(link)){
                    SiteUrl siteUrl = new SiteUrl();
                    siteUrl.setHost(host);
                    siteUrl.setUrl(link);

                    siteUrls.add(siteUrl);
                }
            });
        }

        return siteUrls;
    }
}
