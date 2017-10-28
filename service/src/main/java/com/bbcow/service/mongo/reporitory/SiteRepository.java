package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.Site;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by adan on 2017/10/17.
 */
public interface SiteRepository extends MongoRepository<Site, String> , PagingAndSortingRepository<Site, String> {
}
