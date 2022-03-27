package com.kingren.blog.utils;

import org.springframework.util.DigestUtils;

public class MD5Utils {

    public static final  String salt = "LwgBbHOKWH9JZ$MR";

    public static String getMD5(String string){
        String val = string+salt;
        return DigestUtils.md5DigestAsHex(val.getBytes());
    }

    public static void main(String[] args) {
        System.out.println(getMD5("123456"));
    }
}
