package com.bbcow.service.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by adan on 2017/10/17.
 */
@Document(collection = "book_urls")
public class BookUrls {
    @Id
    private String url;
    private String host;
    @Indexed
    private Date crawl_time;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Date getCrawl_time() {
        return crawl_time;
    }

    public void setCrawl_time(Date crawl_time) {
        this.crawl_time = crawl_time;
    }
}
