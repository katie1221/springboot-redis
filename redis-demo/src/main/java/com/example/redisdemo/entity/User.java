package com.example.redisdemo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户表
 * @author qzz
 */
@Data
public class User implements Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = -4938532764925446470L;

    private Integer user_id;

    private String openid;

    private String user_name;

    private String user_phone;
}
