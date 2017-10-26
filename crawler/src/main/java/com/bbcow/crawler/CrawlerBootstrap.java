package com.bbcow.crawler;

import com.bbcow.service.ServiceBootstrap;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by adan on 2017/10/18.
 */
@SpringBootApplication
@PropertySource("classpath:/crawler.properties")
public class CrawlerBootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(ServiceBootstrap.class, CrawlerBootstrap.class).run(args);
    }
}
