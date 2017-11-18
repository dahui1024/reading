package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.UserToken;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by adan on 2017/10/17.
 */
public interface UserTokenRepository extends MongoRepository<UserToken, ObjectId> , PagingAndSortingRepository<UserToken, ObjectId> {
    UserToken findByToken(String token);
}
