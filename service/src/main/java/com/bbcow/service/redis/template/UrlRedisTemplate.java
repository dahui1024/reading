package com.bbcow.service.redis.template;

import com.bbcow.service.redis.po.UrlPO;
import org.springframework.stereotype.Component;

@Component
public class UrlRedisTemplate extends BasicRedisTemplate<UrlPO> {
    public UrlRedisTemplate() {
        super(UrlPO.class);
    }
}
