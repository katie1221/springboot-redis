package com.example.redisdemo.controller;

import com.example.redisdemo.config.RedisUtil;
import com.example.redisdemo.entity.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author qzz
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 初始3rd_session有效期 单位s  有效期为一天
     */
    private static final int  expire = 60*60*24;

    /**
     * 注入 RedisUtil
     */
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取用户策略：先从缓存中获取用户，没有则取数据表中的数据，再将数据写入缓存
     * @param userId
     * @return
     */
    @GetMapping("/getData")
    public Map<String, String> getUserInfoById(String userId){

        String userKey = "user_"+userId;

        //从redis存储中取所需数据
        Map<String, String> redisDataMap = redisUtil.hgetAll(userKey);
        if(redisDataMap==null || redisDataMap.size()==0){
            System.out.println("缓存不存在");
            //缓存不存在

            //模拟从数据库中获取用户信息
            User userInfo = new User();
            userInfo.setUser_id(1);
            userInfo.setUser_name("张三");
            userInfo.setUser_phone("15720821396");


            //把用户信息存入redis中
            redisDataMap.put("user_id",String.valueOf(userInfo.getUser_id()));
            redisDataMap.put("user_name",String.valueOf(userInfo.getUser_name()));
            redisDataMap.put("user_phone",String.valueOf(userInfo.getUser_phone()));
            //插入缓存
            redisUtil.hmset(userKey, redisDataMap,expire);
        }else{
            System.out.println("缓存存在,用户信息："+redisDataMap.toString());
        }
        return redisDataMap;
    }
}
