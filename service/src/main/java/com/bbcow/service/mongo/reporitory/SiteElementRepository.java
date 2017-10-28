package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.SiteElement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by adan on 2017/10/17.
 */
public interface SiteElementRepository extends MongoRepository<SiteElement, String> , PagingAndSortingRepository<SiteElement, String> {
}
