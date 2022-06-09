package com.example.redisdemo.config;

import com.example.redisdemo.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 使用jedis整合redis
 * @author qzz
 */
@Configuration
public class RedisConfig {
    private static Logger logger = LoggerFactory.getLogger(RedisConfig.class);


    /**
     * redis
     */
    @Value("${spring.redis.host}")
    private String host="127.0.0.1";

    /**
     * redis端口号
     */
    @Value("${spring.redis.port}")
    private int port=6379;

    /**
     * redis密码
     */
    @Value("${spring.redis.password}")
    private String redisPassword="";

    /**
     * redis连接超时时间（毫秒）
     */
    @Value("${spring.redis.timeout}")
    private int timeout=6000;

    /**
     * Redis默认情况下有16个分片，这里配置具体使用的分片，默认时0
     */
    @Value("${spring.redis.database}")
    private String database="0";

    /**
     * 连接池最大连接数（使用负值表示没有限制）默认0
     */
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive=20;

    /**
     * 连接池最大阻塞等待时间 （使用负值表示没有限制） 默认-1
     */
//	@Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWait=5000;

    /**
     * 连接池中的最大空闲连接 默认8
     */
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle=200;

    /**
     * 连接池中的最小空闲连接 默认0
     */
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle=2;

    /**
     * 连接耗时时是否阻塞
     */
    //@Value("${spring.redis.block-when-exhausted}")
    private boolean blockWhenExhausted=true;

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

    /**
     * redis键值出现 \xac\xed\x00\x05t\x00&的解决方法:
     *   出现该问题的原因是， redis template向redis存放使用java对象序列化的值，序列化方式和string的一般方式不同
     *   解决方法：添加 redis序列化
     */
    /**
     * 如果key和value都使用的StringRedisSerializer序列化器，则推荐使用StringRedisTemplate
     * 配置Redis的Key和Value的序列化器
     * @param redisTemplate 从容器中获取RedisTemplate
     * @return 修改后的RedisTemple
     */
    @Bean
    public RedisTemplate<Object, Object> redisStringTemplate(RedisTemplate<Object, Object> redisTemplate) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // 如果手动将Value转换成了JSON，就不要再用JSON序列化器了。
        // redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        redisTemplate.setValueSerializer(stringRedisSerializer);
        return redisTemplate;
    }

}

