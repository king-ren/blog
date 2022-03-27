package com.kingren.blog.utils;

import java.util.Random;

public class RandomUtils {
    public static String getRandomString() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < 6; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
