package com.qiangwang.soeasy.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;

public class ViewUtils {

    public static final String TAG = "ViewUtils";

    @SuppressLint("SetJavaScriptEnabled")
    public static WebView getAuthWebView(Context context) {
        WebView mWebView = new WebView(context);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        return mWebView;
    }

    public static void setImage(final ImageView view, String url) {
        new ImageTask().execute(url, new APIListener<Bitmap, Exception>() {

            @Override
            public void onSuccess(Bitmap result) {
                view.setImageBitmap(result);
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "setImage", e);
            }

        });

    }

}
