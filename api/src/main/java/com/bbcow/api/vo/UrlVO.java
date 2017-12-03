package com.bbcow.api.vo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by adan on 2017/12/3.
 */
public class UrlVO {
    private String host;
    private String url;
    private String encodeUrl;

    public UrlVO(String host, String url) {
        this.host = host;
        this.url = url;
        try {
            this.encodeUrl = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEncodeUrl() {
        return encodeUrl;
    }

    public void setEncodeUrl(String encodeUrl) {
        this.encodeUrl = encodeUrl;
    }
}
