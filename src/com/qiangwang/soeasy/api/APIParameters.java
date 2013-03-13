package com.qiangwang.soeasy.api;

import java.util.HashMap;

public class APIParameters extends HashMap<String, String> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public APIParameters() {

    }

    public void put(String key, int value) {
        put(key, String.valueOf(value));
    }

    public void put(String key, long value) {
        put(key, String.valueOf(value));
    }

    public void put(String key, double value) {
        put(key, String.valueOf(value));
    }

    public void put(String key, float value) {
        put(key, String.valueOf(value));
    }

    public void put(String key, boolean value) {
        put(key, String.valueOf(value));
    }

}
