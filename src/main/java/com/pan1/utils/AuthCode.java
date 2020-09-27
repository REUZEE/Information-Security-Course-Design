package com.pan1.utils;

import java.util.Random;

public class AuthCode {

    public static String generate() {

        Random random = new Random();
        String res = "";
        for (int i = 0; i < 6; i++)
            res += random.nextInt(10) + "";
        return res;
    }

}
