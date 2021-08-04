package com.example.redisdemo.config;

import com.example.redisdemo.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 使用jedis整合redis
 * @author qzz
 */

@Configuration
@PropertySource("classpath:application.properties")
public class RedisConfig {
    private static Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.redisPassword}")
    private String redisPassword;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.database}")
    private String database;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWait;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.block-when-exhausted}")
    private boolean blockWhenExhausted;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Bean
    public JedisPool redisPoolFactory() throws Exception{
        logger.info("JedisPool 注入成功！");
        logger.info("redis地址："+host+":"+port);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        //连接耗尽时是否阻塞，false报异常，true阻塞直到超时，默认true
        jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
        //是否启用pool的jmx管理功能，默认true
        jedisPoolConfig.setJmxEnabled(true);

        JedisPool JedisPool;
        if(Tools.notEmpty(redisPassword)){
            JedisPool = new JedisPool(jedisPoolConfig,host, port, timeout,redisPassword);
        }else{
            JedisPool = new JedisPool(jedisPoolConfig,host, port, timeout);
        }
        logger.info("JedisPool："+JedisPool);
        return JedisPool;
    }
}
