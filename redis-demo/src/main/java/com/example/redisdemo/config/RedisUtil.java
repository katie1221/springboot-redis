package com.example.redisdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * redis工具类
 * @author Administrator
 *
 */
@Component
public class RedisUtil {

    @Autowired
    private JedisPool jedisPool;

    //设置为0就是永不过期  单位秒
    private int expire = 60;
    /**
     * 获取Jedis实例
     *
     * @return
     */
    public synchronized Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                //设置为第二个库（db1） spring.redis.database
                resource.select(0);
                return resource;
            } else {
                System.out.println("获取的Redis链接为null");
                return null;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("获取Redis链接出错：" + e.getMessage());
            return null;
        }
    }

    /**
     * get value from redis
     *
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        Jedis jedis = getJedis();
        byte[] value = null;
        try {
            value = jedis.get(key);
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * get value from redis
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = getJedis();
        String value = null;
        try {
            value = jedis.get(key);
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @return
     */
    public void set(byte[] key, byte[] value) {
        Jedis jedis = getJedis();
        try {
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } finally {
            jedis.close();
        }
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @return
     */
    public void set(String key, String value) {
        Jedis jedis = getJedis();
        try {
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } finally {
            jedis.close();
        }
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public void set(byte[] key, byte[] value, int expire) {
        Jedis jedis = getJedis();
        try {
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } finally {
            jedis.close();
        }
    }

    /**
     * 添加key=String,value=HashMap<String,String>类型的数据 hmset
     *
     * @param key
     * @param inMap
     * @param expire
     *            生命周期 单位秒
     */
    public void hmset(String key, Map<String, String> inMap, int expire) {
        Jedis jedis = getJedis();
        try {
            jedis.hmset(key, inMap);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } finally {
            jedis.close();
        }
    }

    /**
     * 获取value类型为HashMap<String,String>类型的数据
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(String key) {
        Jedis jedis = getJedis();
        Map<String, String> value = null;
        try {
            value = jedis.hgetAll(key);
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * del
     *
     * @param key
     */
    public void del(byte[] key) {
        Jedis jedis = getJedis();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    /**
     * del
     *
     * @param key
     */
    public void del(String key) {
        Jedis jedis = getJedis();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    /**
     * 判断指定键是否存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        Jedis jedis = getJedis();
        boolean flag = jedis.exists(key);
        jedis.close();
        return flag;
    }

    /**
     * 获取key对应的值剩余存活时间
     *
     * @param key
     * @return 正数：剩余的时间(秒) 负数：已过期
     */
    public Long ttlKey(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.ttl(key);
        } catch (Exception e) {
            System.out.println(" -- Redis 获取key对应的值剩余存活时间出错，出错原因：" + e);
            //e.printStackTrace();
            return 0L;
        } finally {
            jedis.close();
        }
    }

    /**
     * 获取key对应的值剩余存活时间
     *
     * @param key
     * @return 正数：剩余的时间(秒) 负数：已过期
     */
    public Long ttlKey(byte[] key) {
        Jedis jedis = getJedis();
        try {
            return jedis.ttl(key);
        } catch (Exception e) {
            System.out.println(" -- Redis 获取key对应的值剩余存活时间出错，出错原因：" + e);
            //e.printStackTrace();
            return 0L;
        } finally {
            jedis.close();
        }
    }

    /**
     * 存储对象
     *
     * @param key
     * @param obj
     * @param expire
     */
    public void setObject(String key, Object obj, int expire) {
        Jedis jedis = getJedis();
        byte[] data = ObjTOSerialize(obj);
        jedis.set(key.getBytes(), data);
        if (expire != 0) {
            jedis.expire(key, expire);
        }
        jedis.close();
    }

    /**
     * 获取对象
     *
     * @param key
     * @return
     */
    public Object getObject(String key) {
        Jedis jedis = getJedis();
        byte[] data = jedis.get(key.getBytes());
        Object obj = null;
        if (data != null) {
            obj = unSerialize(data);
        }
        jedis.close();
        return obj;
    }

    /**
     *
     * 序列化一个对象
     *
     * @param obj
     * @return
     */
    public byte[] ObjTOSerialize(Object obj) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream byteOut = null;
        try {
            byteOut = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(byteOut);
            oos.writeObject(obj);
            byte[] bytes = byteOut.toByteArray();
            return bytes;
        } catch (Exception e) {
            System.out.println("-- Redis序列化对象出错：" + e);
            //e.printStackTrace();
        }
        return null;
    }

    /**
     * 反序列化一个对象
     *
     * @param bytes
     * @return
     */
    public Object unSerialize(byte[] bytes) {
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(bytes);
            ObjectInputStream objIn = new ObjectInputStream(in);
            return objIn.readObject();
        } catch (Exception e) {
            System.out.println("-- Redis反序列化对象出错：" + e);
            //e.printStackTrace();
        }
        return null;
    }

}


