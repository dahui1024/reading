package com.bbcow.service.redis.listeners;

import com.bbcow.service.redis.po.UrlPO;
import com.bbcow.service.redis.template.UrlRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

//@Component
public class SiteListener implements MessageListener{
    @Autowired
    UrlRedisTemplate urlRedisTemplate;

    public SiteListener(@Autowired RedisMessageListenerContainer redisContainer){
        redisContainer.addMessageListener(this, new ChannelTopic("crawler:book"));
    }
    @Override
    public void onMessage(Message message, byte[] bytes) {

        UrlPO urlPO = (UrlPO) urlRedisTemplate.getValueSerializer().deserialize(message.getBody());


        System.out.println(new String(message.getChannel())+"=====");
    }
}
