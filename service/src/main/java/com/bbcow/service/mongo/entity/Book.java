package com.bbcow.service.mongo.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private int is_vip;
    private int is_finish;
    private int is_sign = 1;
    private String description;
    private String cp_image_url;
    private String cp_host;
    private String cp_name;
    private String cp_url;
    private List<String> tags;
    private Date create_time;
    private Date update_time;

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

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public int getIs_finish() {
        return is_finish;
    }

    public void setIs_finish(int is_finish) {
        this.is_finish = is_finish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCp_image_url() {
        return cp_image_url;
    }

    public void setCp_image_url(String cp_image_url) {
        this.cp_image_url = cp_image_url;
    }

    public String getCp_host() {
        return cp_host;
    }

    public void setCp_host(String cp_host) {
        this.cp_host = cp_host;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public int getIs_sign() {
        return is_sign;
    }

    public void setIs_sign(int is_sign) {
        this.is_sign = is_sign;
    }
}
