package com.bbcow.crawler;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created by adan on 2017/10/18.
 */
@SpringBootApplication(scanBasePackages = {"com.bbcow"})
public class CrawlerBootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(CrawlerBootstrap.class).run(args);
    }
}
