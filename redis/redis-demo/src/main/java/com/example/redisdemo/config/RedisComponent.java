package com.example.redisdemo.config;

/**
 * redis 工具类
 * @author qzz
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisComponent {
    private static final Logger logger = LoggerFactory.getLogger(RedisComponent.class);

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public String hGet(String key, String hKey) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, hKey);
    }

    public List<String> hGet(String key, Collection<String> hKeys) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        return hashOperations.multiGet(key, hKeys);
    }

    public Map<String, String> hEntries(String key) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    public Map<String, String> hashGetAll(String key) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    public Map<String, String> hGetAll(String key) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    public void hPutAll(String key, Map<String, String> map) {
        logger.debug("hPutAll key:{}, map:{}", key, map);
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(key, map);
    }

    public void hPut(String key, String hKey, String value, long timeout) {
        logger.debug("hPut key:{}, hKey:{}, value:{}", key, hKey, value);
        redisTemplate.opsForHash().put(key, hKey, value);  //存值
        redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS); // 第三个参数控制时间单位
    }

    public void hPut(String key, String hKey, String value) {
        logger.debug("hPut key:{}, hKey:{}, value:{}", key, hKey, value);
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key, hKey, value);
    }

    public void hRemove(String key, Object... hKeys) {
        logger.debug("hRemove key:{}, hKeys:{}", key, hKeys);
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(key, hKeys);
    }


    public Boolean hasKey(String key, Object hashKey) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.hasKey(key, hashKey);
    }

    public List<Object> values(String key) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.values(key);
    }



    public void delKeys(String[] keys) {
        redisTemplate.delete(Arrays.asList(keys));
    }

    public void set(String key, String value) {
        logger.debug("set key:{}, value:{}", key, value);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }


    public void set(String key, String value, Long expireTime) {
        logger.debug("set key:{}, value:{}, expireTime:{}", key, value, expireTime);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, expireTime, TimeUnit.SECONDS);// 使用秒为单位
    }

    /**
     * 存放有效期毫秒
     **/
    public void put(String key, String value, long timeout) {
        logger.debug("put key:{}, value:{}, timeout:{}", key, value, timeout);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 存放有效期毫秒
     **/
    public void put(String key, String value) {
        logger.debug("put key:{}, value:{}", key, value);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout 毫秒
     */
    public void expire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    public Long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 设置过期时间
     *
     * @param key
     */
    public void delKey(String key) {
        redisTemplate.delete(key);
    }


    public String get(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }


    public void leftPush(String key, String value) {
        logger.debug("leftPush key:{}, value:{}", key, value);
        ListOperations<String, String> listOperation = redisTemplate.opsForList();
        listOperation.leftPush(key, value);
    }

    public void rightPush(String key, String value) {
        logger.debug("rightPush key:{}, value:{}", key, value);
        ListOperations<String, String> listOperation = redisTemplate.opsForList();
        listOperation.rightPush(key, value);
    }

    public Object leftPop(String key) {
        ListOperations<String, String> listOperation = redisTemplate.opsForList();
        Object value = listOperation.leftPop(key);
        return value;
    }

    public Object rightPop(String key) {
        ListOperations<String, String> listOperation = redisTemplate.opsForList();
        Object value = listOperation.rightPop(key);
        return value;
    }

    public List<String> lGetAll(String key) {
        Long size = lSize(key);
        if (size == null || size == 0) {
            return new ArrayList<>();
        }
        return lGetRange(key, 0, size - 1);
    }

    public List<String> lGetRange(String key, long start, long end) {
        ListOperations<String, String> listOperation = redisTemplate.opsForList();
        return listOperation.range(key, start, end);
    }

    public Long lSize(String key) {
        ListOperations<String, String> listOperation = redisTemplate.opsForList();
        return listOperation.size(key);
    }

    public void sAdd(String key, String... values) {
        logger.debug("sAdd key:{}, values:{}", key, values);
        SetOperations<String, String> setOperation = redisTemplate.opsForSet();
        setOperation.add(key, values);
    }

    public void sRemove(String key, String... values) {
        logger.debug("sRemove key:{}, values:{}", key, values);
        SetOperations<String, String> setOperation = redisTemplate.opsForSet();
        if (values != null && values.length > 0) {
            Object[] objects = new Object[values.length];
            for (int i = 0; i < values.length; i++) {
                objects[i] = values[i];
            }
            setOperation.remove(key, objects);
        }
    }

    public void lRemove(String key, int count, Object value) {
        logger.debug("lRemove key:{}, count:{}, value:{}", key, count, value);
        ListOperations<String, String> listOperation = redisTemplate.opsForList();
        listOperation.remove(key, count, value);
    }

    public Object sPop(String key) {
        SetOperations<String, String> setOperation = redisTemplate.opsForSet();
        return setOperation.pop(key);
    }

    public Set<String> sGetAll(String key) {
        SetOperations<String, String> setOperation = redisTemplate.opsForSet();
        return setOperation.members(key);
    }
}

