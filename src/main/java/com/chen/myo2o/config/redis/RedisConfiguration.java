package com.chen.myo2o.config.redis;

import com.chen.myo2o.cache.JedisPoolWriper;
import com.chen.myo2o.cache.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {
    @Value("${redis.hostname}")
    private String hostanme;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.pool.maxActive}")
    private int maxTotal;
    @Value("${redis.pool.maxIdle}")
    private int maxIdle;
    @Value("${redis.pool.maxWait}")
    private long maxWaitMillis;
    @Value("${redis.pool.testOnBorrow}")
    private boolean testOnBorrow;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    @Autowired
    private JedisPoolWriper jedisPoolWriper;

    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 创建redis连接池的设置
     *
     * @return
     */
    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //控制一个pool可分配多少个jedis实例
        jedisPoolConfig.setMaxTotal(maxTotal);
        //连接池中最多可空闲maxIdle个连接
        //表示即使没有数据库连接时依然可以保持20空闲连接，而不被清除
        jedisPoolConfig.setMaxIdle(maxIdle);
        //最大等待时间：当前没有可用连接时，连接池等待连接被归还的最大时间，超时则抛出异常
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        //在获取连接的时候检查有效性
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);

        return jedisPoolConfig;
    }


    @Bean(name = "jedisPoolWriper")
    public JedisPoolWriper createJedisPoolWriper() {
        JedisPoolWriper jedisPoolWriper = new JedisPoolWriper(jedisPoolConfig, hostanme, port);
        return jedisPoolWriper;
    }

    @Bean(name = "jedisUtil")
    public JedisUtil createJedisUtil() {
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setJedisPool(jedisPoolWriper);
        return jedisUtil;
    }

    @Bean(name = "jedisKeys")
    public JedisUtil.Keys createKey() {
        JedisUtil.Keys jedisKeys = jedisUtil.new Keys();
        return jedisKeys;
    }

    @Bean(name = "jedisStrings")
    public JedisUtil.Strings createStrings() {
        JedisUtil.Strings jedisString = jedisUtil.new Strings();
        return jedisString;
    }
}
