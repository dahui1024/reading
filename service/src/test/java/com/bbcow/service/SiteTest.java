package com.bbcow.service;

import com.bbcow.service.mongo.entity.BookElement;
import com.bbcow.service.mongo.entity.Site;
import com.bbcow.service.mongo.entity.SiteElement;
import com.bbcow.service.mongo.reporitory.BookElementRepository;
import com.bbcow.service.mongo.reporitory.BookRepository;
import com.bbcow.service.mongo.reporitory.SiteElementRepository;
import com.bbcow.service.mongo.reporitory.SiteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by adan on 2017/10/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("service-dev")
public class SiteTest {
    @Autowired
    SiteElementRepository siteElementRepository;
    @Autowired
    SiteRepository siteRepository;

    @Test
    public void siteElement(){
        SiteElement siteElement = new SiteElement();
        siteElement.setCreateTime(new Date());
        siteElement.setHost("www.qidian.com");
        siteElement.setUrl("//*[@id=\"page-container\"]/div/ul");

        siteElement.setTarget("/html/body/div[2]/div[5]/div[2]/div[2]/div/ul/li/div[1]/a");
        siteElementRepository.save(siteElement);
    }
    @Test
    public void zhangyue(){
        String host = "yc.ireader.com.cn";
        Site site = new Site();
        site.setHost(host);
        site.setStartUrl("http://yc.ireader.com.cn/books/");
        site.setCreateTime(new Date());

        siteRepository.save(site);

        SiteElement siteElement = new SiteElement();
        siteElement.setCreateTime(new Date());
        siteElement.setHost(host);
        siteElement.setUrl("/html/body/div[2]/div[3]/div/span");
        siteElement.setTarget("/html/body/div[2]/div[3]/ul/li/a");
        siteElementRepository.save(siteElement);
    }
    @Test
    public void zongheng(){
        String host = "www.zongheng.com";
        Site site = new Site();
        site.setHost(host);
        site.setStartUrl("http://book.zongheng.com/store/c0/c0/b9/u1/p1/v9/s9/t0/ALL.html");
        site.setCreateTime(new Date());

        siteRepository.save(site);

        SiteElement siteElement = new SiteElement();
        siteElement.setCreateTime(new Date());
        siteElement.setHost(host);
        siteElement.setUrl(null);
        siteElement.setTarget("/html/body/div[4]/div[7]/div/div[1]/div/ul/li/span[2]/a[1]");
        siteElementRepository.save(siteElement);
    }
    @Test
    public void site(){
        Site site = new Site();
        site.setHost("www.qidian.com");
        site.setStartUrl("https://www.qidian.com/mm/all");
        site.setCreateTime(new Date());

        siteRepository.save(site);
        site.setStartUrl("https://www.qidian.com/all");
        siteRepository.save(site);
    }
}
