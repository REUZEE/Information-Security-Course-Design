package com.pan1.utils;

public class GetFileSize {

    public static double cal(long size) {
        double MB = 1024 * 1024;
        //DecimalFormat decimalFormat = new DecimalFormat("0.0000");
        return ((double) size / MB);
    }
}
