package com.bbcow.service.mongo.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
@Document(collection = "score_book_log")
@CompoundIndex(def = "{'name': 1, 'day': 1}", unique = true, sparse = true)
public class ScoreBookLog {
    @Id
    private ObjectId id;
    private String name;
    @Field("page_score")
    private long pageScore;
    @Field("page_count")
    private long pageCount;
    private List<String> urls;
    @Field("is_new")
    private int isNew;

    private Date day;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPageScore() {
        return pageScore;
    }

    public void setPageScore(long pageScore) {
        this.pageScore = pageScore;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }
}
