package com.qiangwang.soeasy.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebView;

public class ViewUtils {

    @SuppressLint("SetJavaScriptEnabled")
    public static WebView getAuthWebView(Context context){
        WebView mWebView = new WebView(context);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        return mWebView;
    }
}
