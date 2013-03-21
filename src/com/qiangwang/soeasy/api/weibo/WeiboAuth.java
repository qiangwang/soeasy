package com.qiangwang.soeasy.api.weibo;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.qiangwang.soeasy.App;
import com.qiangwang.soeasy.api.APIException;
import com.qiangwang.soeasy.api.APIListener;
import com.qiangwang.soeasy.api.APIParameters;
import com.qiangwang.soeasy.api.Auth;
import com.qiangwang.soeasy.api.Utility;
import com.qiangwang.soeasy.api.ViewUtils;

public class WeiboAuth extends Auth {
    public static final String TAG = "WeiboAuth";

    public static final String AUTHORIZE_URL = "https://open.weibo.cn/oauth2/authorize";

    public String clientId;
    public String redirectURI;

    public WeiboAuth(String clientId, String redirectURI) {
        this.clientId = clientId;
        this.redirectURI = redirectURI;
    }

    @Override
    public View getAuthView(Context context,
            APIListener<Bundle, Exception> listener) {
        APIParameters params = new APIParameters();

        params.put("client_id", clientId);
        params.put("redirect_uri", redirectURI);
        params.put("response_type", "token");
        params.put("display", "mobile");
        params.put("forcelogin", "true");

        String url = AUTHORIZE_URL + "?" + Utility.encodeUrl(params);

        WebView webView = ViewUtils.getAuthWebView(context);
        webView.setWebViewClient(new WeiboWebViewClient(listener));
        webView.loadUrl(url);
        return webView;
    }

    private class WeiboWebViewClient extends WebViewClient {

        private APIListener<Bundle, Exception> listener;

        public WeiboWebViewClient(APIListener<Bundle, Exception> listener) {
            this.listener = listener;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "Redirect URL: " + url);

            if (url.startsWith(WeiboAuth.this.redirectURI)) {
                handleRedirectUrl(view, url);
                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        	Toast.makeText(App.getAppContext(), "正在加载", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onPageStarted URL: " + url);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "onPageFinished URL: " + url);
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            listener.onError(new Exception(description));
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                SslError error) {
            handler.proceed();
        }

        private void handleRedirectUrl(WebView view, String url) {
            Bundle values = Utility.parseUrl(url);

            String errorMsg = values.getString("error_description");
            String errorCode = values.getString("error");
            String errorURI = values.getString("error_url");

            if (!TextUtils.isEmpty(errorMsg)|| !TextUtils.isEmpty(errorCode)) {
                listener.onError(new APIException(errorMsg, errorCode, 200,
                        errorURI));
            } else {
                listener.onSuccess(values);
            }
        }

    }
}
