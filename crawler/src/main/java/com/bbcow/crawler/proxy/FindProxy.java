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
    private Map<String, Integer> countMap = new HashMap<>();

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

        siteUrls.forEach(link -> System.out.println(link.getUrl()));
        System.out.println("------------");
        return siteUrls;
    }
}
