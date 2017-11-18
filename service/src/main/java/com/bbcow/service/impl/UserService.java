package com.bbcow.service.impl;

import com.bbcow.service.mongo.entity.User;
import com.bbcow.service.mongo.entity.UserToken;
import com.bbcow.service.mongo.reporitory.UserRepository;
import com.bbcow.service.mongo.reporitory.UserTokenRepository;
import com.bbcow.service.util.JWTUtil;
import com.bbcow.service.util.MD5;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserTokenRepository userTokenRepository;

    public UserToken register(String name, String phone, String password, String confirmPassword, int code) {

        if (userRepository.findByName(name) == null && userRepository.findByPhone(phone) == null){
            User user = new User();
            user.setName(name);
            user.setPhone(phone);
            user.setPassword(MD5.digest("bbcow@"+password));
            user.setRegisterTime(new Date());
            user.setStatus(1);
            userRepository.save(user);

            UserToken userToken = new UserToken();
            userToken.setUserId(user.getId());
            userToken.setStatus(1);
            userToken.setLoginTime(user.getRegisterTime());
            userToken.setToken(JWTUtil.generateToken(user.getId().toString(), 180));
            userToken.setRefreshTime(DateUtils.addHours(userToken.getLoginTime(), 3));
            userToken.setExpireTime(DateUtils.addDays(userToken.getLoginTime(), 30));

            userTokenRepository.save(userToken);

            return userToken;
        }

        return null;
    }

    public UserToken loginWithPhone(String phone, String password){
        User user = userRepository.findByPhoneAndPassword(phone, password);
        if (user != null){
            UserToken userToken = userTokenRepository.findOne(user.getId());

            if (userToken == null){
                userToken = new UserToken();
                userToken.setUserId(user.getId());
                userToken.setStatus(1);
            }
            userToken.setLoginTime(new Date());
            userToken.setToken(JWTUtil.generateToken(user.getId().toString(), 180));
            userToken.setRefreshTime(DateUtils.addHours(userToken.getLoginTime(), 3));
            userToken.setExpireTime(DateUtils.addDays(userToken.getLoginTime(), 30));

            return userToken;
        }else {
            return null;
        }
    }
    public UserToken loginWithName(String name, String password){
        User user = userRepository.findByNameAndPassword(name, password);
        if (user != null){
            UserToken userToken = userTokenRepository.findOne(user.getId());

            if (userToken == null){
                userToken = new UserToken();
                userToken.setUserId(user.getId());
                userToken.setStatus(1);
            }
            userToken.setLoginTime(new Date());
            userToken.setToken(JWTUtil.generateToken(user.getId().toString(), 180));
            userToken.setRefreshTime(DateUtils.addHours(userToken.getLoginTime(), 3));
            userToken.setExpireTime(DateUtils.addDays(userToken.getLoginTime(), 30));

            return userToken;
        }else {
            return null;
        }
    }

    public int reset(String phone, String password, String confirmPassword, int code){
        User user = userRepository.findByPhone(phone);
        if (user != null){
            user.setPassword(MD5.digest("bbcow@"+password));

            userRepository.save(user);

            return 1;
        }else {
            register(phone, phone, password, confirmPassword, code);

            return 1;
        }
    }
}
