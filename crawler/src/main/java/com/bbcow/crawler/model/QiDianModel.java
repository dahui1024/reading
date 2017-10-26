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
@TargetUrl("https://book.qidian.com/info/*")
@HelpUrl("https://www.qidian.com/rank/\\w+")
public class QiDianModel implements AfterExtractor{
    @ExtractBy(value = "/html/body/div[2]/div[6]/div[1]/div[2]/h1/em/text()")
    public String name;
    @ExtractBy(value = "/html/body/div[2]/div[6]/div[1]/div[2]/h1/span/a/text()")
    public String author;
    @ExtractBy(value = "/html/body/div[2]/div[6]/div[1]/div[2]/p[1]/a/text()")
    public List<String> tags;
    @ExtractBy(value = "/html/body/div[2]/div[6]/div[1]/div[2]/p[1]/span/text()")
    public List<String> statuses;

    @ExtractBy(value = "/html/body/div[2]/div[6]/div[4]/div[1]/div[1]/div[1]/p/tidyText()")
    public String description;
    @ExtractBy(value = "//*[@id=\"bookImg\"]/img/@abs:src")
    public String image_url;

    public String original_url;

    @Override
    public void afterProcess(Page page) {
        this.original_url = page.getUrl().get();
    }
}
