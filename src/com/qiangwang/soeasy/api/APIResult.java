package com.qiangwang.soeasy.api;

public class APIResult {

    private String result;
    private int httpCode;
    private String url;

    public APIResult(String result, int httpCode, String url) {
        this.result = result;
        this.httpCode = httpCode;
        this.url = url;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
