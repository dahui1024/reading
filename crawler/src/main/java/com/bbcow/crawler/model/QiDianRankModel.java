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
@TargetUrl("https://www.qidian.com/all")
public class QiDianRankModel implements AfterExtractor{

    @ExtractBy("//*[@id=\"page-container\"]/div/ul/li[8]/a/@data-page")
    public int count;



    @Override
    public void afterProcess(Page page) {
        System.out.println(count);
        System.out.println(page.getUrl().get());
    }
}
