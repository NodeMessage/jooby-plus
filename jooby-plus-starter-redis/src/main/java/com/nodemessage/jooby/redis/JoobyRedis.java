package com.nodemessage.jooby.redis;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author wjsmc
 * @date 2022/8/10 13:36
 * @description
 **/
public class JoobyRedis {

    public static JedisPool jedisPool;

    static {
        Config config = ConfigFactory.load("application.conf");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(config.getInt("redis.maxIdle"));
        jedisPoolConfig.setMinIdle(config.getInt("redis.minIdle"));
        jedisPoolConfig.setMaxWaitMillis(config.getInt("redis.maxWaitMillis"));
        jedisPoolConfig.setTestOnBorrow(config.getBoolean("redis.testOnBorrow"));
        jedisPoolConfig.setTestOnReturn(config.getBoolean("redis.testOnReturn"));
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(config.getInt("redis.timeBetweenEvictionRunsMillis"));
        jedisPoolConfig.setMinEvictableIdleTimeMillis(config.getInt("redis.minEvictableIdleTimeMillis"));
        jedisPoolConfig.setNumTestsPerEvictionRun(config.getInt("redis.numTestsPerEvictionRun"));
        jedisPool = new JedisPool(jedisPoolConfig, config.getString("redis.host"), (config.getInt("redis.port")));
    }

    public static Jedis getConnectJedis() {
        return jedisPool.getResource();
    }

}
