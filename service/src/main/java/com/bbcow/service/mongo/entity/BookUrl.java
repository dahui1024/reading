package com.bbcow.service.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by adan on 2017/10/17.
 */
@Document(collection = "book_url")
public class BookUrl {
    @Id
    private String url;
    private String host;
    @Indexed
    private Date crawl_time;
    private Date create_time;
    private int crawl_count;

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

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public int getCrawl_count() {
        return crawl_count;
    }

    public void setCrawl_count(int crawl_count) {
        this.crawl_count = crawl_count;
    }
}
