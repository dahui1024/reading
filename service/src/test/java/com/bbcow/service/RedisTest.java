package com.bbcow.service;

import com.bbcow.service.redis.po.UrlPO;
import com.bbcow.service.redis.template.LockRedisTemplate;
import com.bbcow.service.redis.template.UrlRedisTemplate;
import com.google.common.io.Resources;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by adan on 2017/10/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@SpringBootApplication
@ActiveProfiles("service-dev")
public class RedisTest {
    @Autowired
    UrlRedisTemplate urlRedisTemplate;
    @Autowired
    LockRedisTemplate lockRedisTemplate;

    @Test
    public void s(){
//        System.out.println(urlRedisTemplate.keys("*"));
        UrlPO urlPO = new UrlPO();
        urlPO.setUrl("www.baidu.com");
        urlRedisTemplate.convertAndSend("crawler:book", urlPO);
    }
    @Test
    public void luaLock(){
        System.out.println(lockRedisTemplate.lock("ttt"));
    }
    @Test
    public void releaseLuaLock(){
//        System.out.println(lockRedisTemplate.releaseLock("ttt"));

        lockRedisTemplate.delete(lockRedisTemplate.keys("counter*"));
    }
}
