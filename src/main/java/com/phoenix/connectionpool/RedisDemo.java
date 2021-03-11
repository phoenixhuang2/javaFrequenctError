package com.phoenix.connectionpool;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;

/**
 *
 */
@Slf4j
public class RedisDemo {
    /**
     * Jedis 继承了 BinaryJedis，BinaryJedis 中保存了单个 Client 的实例，
     * Client 最终继承了 Connection，Connection 中保存了单个 Socket 的实例，和 Socket 对应的两个读写流。
     * 因此，一个 Jedis 对应一个 Socket 连接
     * @throws InterruptedException
     */
    public void wrong() throws InterruptedException {

        Jedis jedis = new Jedis("8.136.120.114", 6379);
        jedis.auth("foobared");

        /**
         * 我们在多线程环境下复用 Jedis 对象，其实就是在复用 RedisOutputStream。如果多个线程在执行操作，那么既无法确保整条命令以一个原子操
         * 作写入 Socket，也无法确保写入后、读取前没有其他数据写到远端：
         */
        new Thread(()->{
            for (int i=0; i<1000; i++) {
                String result = jedis.get("a");
                if (!"1".equals(result)) {
                    log.warn("Expect a to be 1 but found {}", result);
                    return;
                }
            }
        }).start();

        new Thread(()->{
            for (int i=0; i<1000; i++) {
                String result = jedis.get("b");
                if (!"2".equals(result)) {
                    log.warn("Expect b to be 2 but found {}", result);
                    return;
                }
            }
        }).start();

        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * Jedis提供了线程安全的JedisPool来获取Jedis实例；
     * JedisPool 可以声明为 static 在多个线程之间共享，扮演连接池的角色。使用时，
     * 按需使用 try-with-resources 模式从 JedisPool 获得和归还 Jedis 实例。
     *
     * JedisPool 连接池和连接是分开的，需要获取和归还连接
     *
     * JedisPool是调用close方法归还连接
     * 直接连接是调用disconnect关闭连接
     *
     * JedisPool内部用的是Apache的GenericObjectPool
     */
    private static JedisPool jedisPool = new JedisPool("8.136.120.114", 6379);
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

        new Thread(()->{
            try (Jedis jedis = jedisPool.getResource();) {
                jedis.auth("foobared");
                for (int i = 0; i < 1000; i++) {
                    String result = jedis.get("b");
                    if (!"2".equals(result)) {
                        log.warn("Expect b to be 2 but found {}", result);
                        return;
                    }
                }
            }
        }).start();

    }


    public static void main(String[] args) {
        RedisDemo redisDemo = new RedisDemo();
//        try {
//            redisDemo.wrong();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        redisDemo.right();

    }
}
