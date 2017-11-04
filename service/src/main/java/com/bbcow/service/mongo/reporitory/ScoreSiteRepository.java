package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.ScoreSite;
import com.bbcow.service.mongo.entity.Site;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
public interface ScoreSiteRepository extends MongoRepository<ScoreSite, String> , PagingAndSortingRepository<ScoreSite, String> {
    List<ScoreSite> findByStatus(int status);
}
