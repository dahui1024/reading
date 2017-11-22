package com.bbcow.service.redis.template;

import com.google.common.io.Resources;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

@Component
public class LockRedisTemplate extends BasicRedisTemplate<Integer>{
    DefaultRedisScript<Long> lockScript = null;
    DefaultRedisScript<Long> releaseLockScript = null;

    public LockRedisTemplate() {
        super(Integer.class);
        URL lockURL = Resources.getResource("lua/UrlLock.lua");
        URL releaseLockURL = Resources.getResource("lua/ReleaseUrlLock.lua");
        try {
            List<String> lockLua = Resources.readLines(lockURL, Charset.defaultCharset());
            StringBuilder lockScriptSource = new StringBuilder();
            lockLua.forEach(s -> lockScriptSource.append(s).append("\r\n"));
            lockScript = new DefaultRedisScript(lockScriptSource.toString(), String.class);

            List<String> releaseLockLua = Resources.readLines(releaseLockURL, Charset.defaultCharset());
            StringBuilder releaseLockScriptSource = new StringBuilder();
            releaseLockLua.forEach(s -> releaseLockScriptSource.append(s).append("\r\n"));
            releaseLockScript = new DefaultRedisScript(releaseLockScriptSource.toString(), String.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int lock(String key){
        List<String> keys = new LinkedList<>();
        keys.add(key);
        return execute(lockScript, keys).intValue();
    }

    public int releaseLock(String key){
        List<String> keys = new LinkedList<>();
        keys.add(key);
        return execute(releaseLockScript, keys).intValue();
    }
}
