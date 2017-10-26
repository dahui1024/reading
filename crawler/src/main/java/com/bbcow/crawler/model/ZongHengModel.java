package com.bbcow.crawler.model;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;

/**
 * Created by adan on 2017/10/18.
 */
@TargetUrl("http://book.zongheng.com/book/*")
@HelpUrl("http://book.zongheng.com/quanben/c0/c0/b9/u1/p1/v9/s1/t0/ALL.html")
public class ZongHengModel implements AfterExtractor{
    @ExtractBy(value = "/html/body/div[4]/div/div[1]/div/div/div/div[2]/h1/a/text()")
    public String name;
    @ExtractBy(value = "/html/body/div[4]/div/div[1]/div/div/div/div[2]/div[1]/a[1]/text()")
    public String author;
    @ExtractBy(value = "/html/body/div[4]/div/div[1]/div/div/div/div[2]/div[1]/a[2]/text()")
    public List<String> tags;
    @ExtractBy(value = "/html/body/div[4]/div/div[1]/div/div/div/div[2]/h1/em/@class")
    public List<String> statuses;

    @ExtractBy(value = "/html/body/div[4]/div/div[1]/div/div/div/div[2]/div[2]/p/tidyText()")
    public String description;
    @ExtractBy(value = "/html/body/div[4]/div/div[1]/div/div/div/div[1]/p/a/img/@abs:src")
    public String image_url;

    @ExtractBy(value = "/html/body/div[4]/div/div[1]/div/div/div/div[1]/span/@class")
    public String status;

    public String original_url;

    @Override
    public void afterProcess(Page page) {
        this.original_url = page.getUrl().get();
    }
}
