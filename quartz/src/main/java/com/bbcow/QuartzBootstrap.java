package com.bbcow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by adan on 2017/11/5.
 */
@SpringBootApplication(scanBasePackages = {"com.bbcow"})
@EnableScheduling
public class QuartzBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(QuartzBootstrap.class, args);
    }
}
