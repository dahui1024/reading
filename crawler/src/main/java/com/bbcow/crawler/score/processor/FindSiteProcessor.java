package com.bbcow.crawler.score.processor;

import com.bbcow.service.impl.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.net.MalformedURLException;
import java.net.URL;

@Component
public class FindSiteProcessor implements PageProcessor{
    @Autowired
    ScoreService scoreService;
    private boolean isAdd = true;

    @Override
    public void process(Page page) {
        if (isAdd){
            page.getHtml().xpath("//*[@id=\"page\"]").links().all().forEach(s -> {
                page.addTargetRequest(new Request(s));
            });

            isAdd = false;
        }
        page.getHtml().xpath("//*[@class=\"result\"]//*[@class=\"c-showurl\"]/text()").all().forEach(s1 -> {
            try {
                URL url = new URL("http://"+s1.replace("...", ""));

                page.addTargetRequest(new Request("http://"+ url.getHost()));
                scoreService.addSite(url.getHost());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Site getSite() {
        return Site.me();
    }
}
