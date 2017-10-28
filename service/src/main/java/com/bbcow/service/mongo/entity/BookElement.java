package com.bbcow.service.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
@Document(collection = "book_element")
public class BookElement {
    @Id
    private String host;
    private String name;
    private String author;
    private String vip;
    private String status;
    private String sign;
    private String description;
    private String image_url;
    private String cp_name;
    private String cp_url;
    private String tag;
    private String other;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCp_name() {
        return cp_name;
    }

    public void setCp_name(String cp_name) {
        this.cp_name = cp_name;
    }

    public String getCp_url() {
        return cp_url;
    }

    public void setCp_url(String cp_url) {
        this.cp_url = cp_url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
