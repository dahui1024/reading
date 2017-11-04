package com.bbcow.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by adan on 2017/10/17.
 */
@Configuration
@EnableMongoRepositories(basePackages = {"com.bbcow.service.mongo.reporitory"})
public class ServiceConfiguration {
}
