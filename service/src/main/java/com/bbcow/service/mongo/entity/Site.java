package com.bbcow.service.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by adan on 2017/10/17.
 */
@Document(collection = "site")
public class Site {
    @Id
    private String start_url;
    private String host;
    private int status;
    private Date create_time;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getStart_url() {
        return start_url;
    }

    public void setStart_url(String start_url) {
        this.start_url = start_url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
