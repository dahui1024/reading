package com.bbcow.service.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    @Field("crawl_time")
    private Date crawlTime;
    @Field("create_time")
    private Date createTime;
    @Field("crawl_count")
    private int crawlCount;
    @Field("chapter_url")
    private String chapterUrl;
    @Indexed
    @Field("reference_key")
    private String referenceKey;
    // 0：待抓取，1：抓取完成，2：分析完成
    @Indexed
    @Field("chapter_status")
    private int chapterStatus;
    @Indexed
    @Field("chapter_crawl_time")
    private Date chapterCrawlTime;
    @Field("chapter_crawl_count")
    private int chapterCrawlCount;

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

    public Date getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(Date crawlTime) {
        this.crawlTime = crawlTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getCrawlCount() {
        return crawlCount;
    }

    public void setCrawlCount(int crawlCount) {
        this.crawlCount = crawlCount;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public String getReferenceKey() {
        return referenceKey;
    }

    public void setReferenceKey(String referenceKey) {
        this.referenceKey = referenceKey;
    }

    public Date getChapterCrawlTime() {
        return chapterCrawlTime;
    }

    public void setChapterCrawlTime(Date chapterCrawlTime) {
        this.chapterCrawlTime = chapterCrawlTime;
    }

    public int getChapterCrawlCount() {
        return chapterCrawlCount;
    }

    public void setChapterCrawlCount(int chapterCrawlCount) {
        this.chapterCrawlCount = chapterCrawlCount;
    }

    public int getChapterStatus() {
        return chapterStatus;
    }

    public void setChapterStatus(int chapterStatus) {
        this.chapterStatus = chapterStatus;
    }
}
