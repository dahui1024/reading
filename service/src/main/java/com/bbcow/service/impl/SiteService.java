package com.bbcow.service.impl;

import com.bbcow.service.mongo.entity.Site;
import com.bbcow.service.mongo.entity.SiteElement;
import com.bbcow.service.mongo.entity.SiteUrl;
import com.bbcow.service.mongo.reporitory.SiteElementRepository;
import com.bbcow.service.mongo.reporitory.SiteRepository;
import com.bbcow.service.mongo.reporitory.SiteUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by adan on 2017/10/28.
 */
@Component
public class SiteService {
    @Autowired
    SiteElementRepository siteElementRepository;
    @Autowired
    SiteUrlRepository siteUrlRepository;
    @Autowired
    SiteRepository siteRepository;

    public int save(String name, String website, String contact){
        Site site = new Site();
        site.setName(name);
        site.setContact(contact);

        try {
            URL url = new URL(website);
            site.setHost(url.getHost());
            site.setProtocol(url.getProtocol());
            site.setStatus(0);
            site.setStartUrl(website);
            site.setCompleteInit(1);
            siteRepository.save(site);
            return 1;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<Site> load(){
        return siteRepository.findByCompleteInitAndStatus(0, 1);
    }
    public Collection<String> loadFindUrl(){
        List<Site> sites = siteRepository.findAll();
        Set<String> strings = new HashSet<>();

        sites.forEach(site -> strings.add(site.getProtocol()+"://"+site.getHost()));

        return strings;
    }
    public Map<String, SiteElement> loadElements(){
        Map<String, SiteElement> elementMap = new TreeMap<>();
        siteElementRepository.findAll().forEach(siteElement -> {
            elementMap.put(siteElement.getHost(), siteElement);
        });
        return elementMap;
    }
    public void saveUrl(SiteUrl siteUrl){
        if (siteUrlRepository.findOne(siteUrl.getUrl()) == null) {
            siteUrlRepository.save(siteUrl);
        }
    }
}
