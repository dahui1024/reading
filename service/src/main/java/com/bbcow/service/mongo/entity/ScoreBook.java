package com.bbcow.service.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
@Document(collection = "score_book")
public class ScoreBook {
    @Id
    private String name;

    private long score;
    @Field("site_scores")
    private List<Integer> siteScores;
    private Date day;

    @Field("create_time")
    private Date createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<Integer> getSiteScores() {
        return siteScores;
    }

    public void setSiteScores(List<Integer> siteScores) {
        this.siteScores = siteScores;
    }
}
