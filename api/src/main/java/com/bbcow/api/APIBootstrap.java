package com.bbcow.api;

import com.bbcow.service.ServiceBootstrap;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created by adan on 2017/10/17.
 */
@SpringBootApplication
public class APIBootstrap {

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder().sources(APIBootstrap.class, ServiceBootstrap.class).run(args);
    }
}
