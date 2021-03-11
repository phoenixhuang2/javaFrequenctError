package com.phoenix.connectionpool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
public class JedisPoolDemo {
    private static JedisPool jedisPool;
    static {
        //连接池参数定制化
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //获取连接超时
        poolConfig.setMaxWaitMillis(10000);
        //连接超时
        jedisPool = new JedisPool(poolConfig, "8.136.120.114", 6379, 5000);
    }

    public void right() {
        new Thread(()->{
            try (Jedis jedis = jedisPool.getResource();) {
                jedis.auth("foobared");
                for (int i = 0; i < 1000; i++) {
                    String result = jedis.get("a");
                    if (!"1".equals(result)) {
                        log.warn("Expect a to be 1 but found {}", result);
                        return;
                    }
                }
            }
        }).start();
    }
}
