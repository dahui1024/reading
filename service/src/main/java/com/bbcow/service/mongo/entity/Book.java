package com.bbcow.service.mongo.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
@Document(collection = "book")
public class Book {
    @Id
    private ObjectId id;
    @Indexed
    private String name;
    @Indexed
    private String author;
    @Indexed
    @Field("reference_key")
    private String referenceKey;
    @Field("is_vip")
    private int isVip;
    @Field("is_finish")
    private int isFinish;
    @Field("is_sign")
    private int isSign = 1;
    @Indexed
    @Field("page_score")
    private int pageScore = 0;
    @Field("page_count")
    private int pageCount = 0;
    private String description;
    @Field("cp_image_url")
    private String cpImageUrl;
    @Field("cp_host")
    private String cpHost;
    @Field("cp_name")
    private String cpName;
    @Field("cp_url")
    private String cpUrl;
    private List<String> tags;
    @Field("create_time")
    private Date createTime;
    @Field("update_time")
    private Date updateTime;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReferenceKey() {
        return referenceKey;
    }

    public void setReferenceKey(String referenceKey) {
        this.referenceKey = referenceKey;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public int getIsSign() {
        return isSign;
    }

    public void setIsSign(int isSign) {
        this.isSign = isSign;
    }

    public int getPageScore() {
        return pageScore;
    }

    public void setPageScore(int pageScore) {
        this.pageScore = pageScore;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCpImageUrl() {
        return cpImageUrl;
    }

    public void setCpImageUrl(String cpImageUrl) {
        this.cpImageUrl = cpImageUrl;
    }

    public String getCpHost() {
        return cpHost;
    }

    public void setCpHost(String cpHost) {
        this.cpHost = cpHost;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getCpUrl() {
        return cpUrl;
    }

    public void setCpUrl(String cpUrl) {
        this.cpUrl = cpUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
