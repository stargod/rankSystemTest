package org.example;
import redis.clients.jedis.Jedis;

public class RedisUtil {
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    private static Jedis jedis;

    public static Jedis getJedis() {
        if (jedis == null) {
            jedis = new Jedis(REDIS_HOST, REDIS_PORT);
        }
        return jedis;
    }
}