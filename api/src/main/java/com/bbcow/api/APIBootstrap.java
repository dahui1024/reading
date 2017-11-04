package com.bbcow.api;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created by adan on 2017/10/17.
 */
@SpringBootApplication(scanBasePackages = {"com.bbcow"})
public class APIBootstrap {

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder().sources(APIBootstrap.class).run(args);
    }
}
