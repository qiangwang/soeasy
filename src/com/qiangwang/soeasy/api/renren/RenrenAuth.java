package com.qiangwang.soeasy.api.renren;

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

public class RenrenAuth extends Auth {
    public static final String TAG = "RenrenAuth";

    public static final String AUTHORIZE_URL = "https://graph.renren.com/oauth/authorize";

    public String clientId;
    public String redirectURI;

    public RenrenAuth(String clientId, String redirectURI){
        this.clientId = clientId;
        this.redirectURI = redirectURI;
    }
    
    @Override
    public View getAuthView(Context context, APIListener<Bundle, Exception> listener) {
        APIParameters params = new APIParameters();

        params.put("client_id", clientId);
        params.put("redirect_uri", redirectURI);
        params.put("response_type", "token");
        params.put("display", "touch");
        params.put("x_renew", "true");

        String url = AUTHORIZE_URL + "?" + Utility.encodeUrl(params);

        WebView webView = ViewUtils.getAuthWebView(context);
        webView.setWebViewClient(new RenrenWebViewClient(listener));
        webView.loadUrl(url);
        return webView;
    }

    private class RenrenWebViewClient extends WebViewClient {

        private APIListener<Bundle, Exception> listener;

        public RenrenWebViewClient(APIListener<Bundle, Exception> listener) {
            this.listener = listener;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "Redirect URL: " + url);
            
            if (url.startsWith(RenrenAuth.this.redirectURI)) {
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
            String errorURI = values.getString("error_uri"); 

            if (!TextUtils.isEmpty(errorMsg) || !TextUtils.isEmpty(errorCode)) {
                listener.onError(new APIException(errorMsg, errorCode, 200, errorURI));            
            } else {
                listener.onSuccess(values);
            }
        }

    }
}
