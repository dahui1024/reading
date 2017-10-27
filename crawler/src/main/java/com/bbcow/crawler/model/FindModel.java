package com.bbcow.crawler.model;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by adan on 2017/10/18.
 */
@TargetUrl("https://www.qidian.com/all")
public class FindModel implements AfterExtractor{
    public String original_url;

    @Override
    public void afterProcess(Page page) {
        List<String> links = page.getHtml().xpath("//*[@id=\"page-container\"]/div/ul").links().all();

        Set<String> uniqueLinks = new TreeSet<>(links);


        uniqueLinks.forEach(s -> System.out.println(s));

        System.out.println("------------------------------");

        page.addTargetRequests(links);
        this.original_url = page.getUrl().get();

    }
}
