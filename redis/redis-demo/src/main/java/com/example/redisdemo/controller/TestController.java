package com.example.redisdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.redisdemo.config.RedisComponent;
import com.example.redisdemo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qzz
 */
@RestController
@RequestMapping("/user")
public class TestController {

    /**
     * 初始3rd_session有效期 单位s  有效期为一天
     */
    private static final int expire = 60 * 60 * 24;

    /**
     * 注入 RedisUtil
     */
    @Autowired
    private RedisComponent redisComponent;

    /**
     * 获取用户策略：先从缓存中获取用户，没有则取数据表中的数据，再将数据写入缓存
     *
     * @param userId
     * @return
     */
    @GetMapping("/getData")
    public JSONObject getUserInfoById(String userId) {

        //从redis中缓存获取数据
        String userInfo = redisComponent.get("userId");
        if(Tools.isEmpty(userInfo)){
            //缓存不存在
            //模拟从数据库中获取用户信息
            Map<String, Object> data = new HashMap<>();
            data.put("userId","111");
            data.put("userName","张三");
            data.put("userPhone","15753656789");
            //存储
            redisComponent.put("userId", JSONObject.toJSONString(data));
            userInfo = JSONObject.toJSONString(data);
        }
        JSONObject obj = JSONObject.parseObject(userInfo);
        return obj;
    }
}
