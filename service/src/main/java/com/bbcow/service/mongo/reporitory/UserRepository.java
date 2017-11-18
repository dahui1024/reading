package com.bbcow.service.mongo.reporitory;

import com.bbcow.service.mongo.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by adan on 2017/10/17.
 */
public interface UserRepository extends MongoRepository<User, ObjectId> , PagingAndSortingRepository<User, ObjectId> {
    User findByName(String name);
    User findByPhone(String phone);
    User findByNameAndPassword(String name, String password);
    User findByPhoneAndPassword(String phone, String password);
}
