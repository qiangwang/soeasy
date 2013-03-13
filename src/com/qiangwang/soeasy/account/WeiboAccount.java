package com.qiangwang.soeasy.account;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qiangwang.soeasy.Settings;
import com.qiangwang.soeasy.api.APIListener;
import com.qiangwang.soeasy.api.weibo.WeiboAPI;
import com.qiangwang.soeasy.api.weibo.WeiboAPI.FEATURE;
import com.qiangwang.soeasy.api.weibo.WeiboAuth;
import com.qiangwang.soeasy.message.NewsMessage;

public class WeiboAccount extends Account {

    public static final String TAG = "WeiboAccount";

    private static final String CLIENT_ID = "1796068784";
    private static final String REDIRECT_URI = "http://www.sina.com";

    private WeiboAuth weiboAuth;
    private WeiboAPI weiboAPI;

    private String accessToken;
    private String expiresIn;

    private long lastNewsId;

    public WeiboAccount(Context context) {
        super(context);
    }

    private WeiboAuth getWeiboAuth() {
        if (weiboAuth == null) {
            weiboAuth = new WeiboAuth(CLIENT_ID, REDIRECT_URI);
        }
        return weiboAuth;
    }

    private WeiboAPI getWeiboAPI() {
        if (weiboAPI == null && accessToken != null) {
            weiboAPI = new WeiboAPI(accessToken);
        }
        return weiboAPI;
    }

    @Override
    public View getAuthView(APIListener<Void, Void> listener) {
        return getWeiboAuth().getAuthView(context,
                new WeiboAuthListener(listener));
    }

    @Override
    public void getLatestNews(
            final APIListener<ArrayList<NewsMessage>, Exception> listener) {
        getWeiboAPI().homeTimeline(lastNewsId, 0, 10, 1, false, FEATURE.ALL,
                false, new APIListener<String, Exception>() {

                    @Override
                    public void onSuccess(String result) {

                        ArrayList<NewsMessage> newsMessages = new ArrayList<NewsMessage>();

                        try {
                            JSONObject jNews = new JSONObject(result);
                            JSONArray jStatuses = jNews
                                    .getJSONArray("statuses");

                            for (int i = 0; i < jStatuses.length(); i++) {
                                JSONObject status = jStatuses.getJSONObject(i);

                                long id = status.getLong("id");

                                JSONObject user = status.getJSONObject("user");
                                Account author = new WeiboAccount(context);
                                author.setUid(user.getString("id"));
                                author.setUsername(user
                                        .getString("screen_name"));

                                NewsMessage newsMessage = new NewsMessage(
                                        WeiboAccount.this, id, author, status
                                                .getString("text"), status
                                                .getString("created_at"),
                                        status.getInt("comments_count"));

                                newsMessages.add(newsMessage);

                                if (id > lastNewsId) {
                                    lastNewsId = id;
                                }
                            }

                            listener.onSuccess(newsMessages);
                        } catch (JSONException e) {
                            listener.onError(e);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(context, e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public String toJSON() {
        JSONObject jAccount = new JSONObject();

        try {
            jAccount.put("uid", uid);
            jAccount.put("username", username);
            jAccount.put("accessToken", accessToken);
            jAccount.put("expiresIn", expiresIn);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jAccount.toString();
    }

    @Override
    public void fromJSON(String json) {
        try {
            JSONObject jAccount = new JSONObject(json);
            uid = jAccount.optString("uid");
            username = jAccount.optString("username");
            accessToken = jAccount.optString("accessToken");
            expiresIn = jAccount.optString("expiresIn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class WeiboAuthListener implements APIListener<Bundle, Exception> {

        private APIListener<Void, Void> listener;

        public WeiboAuthListener(APIListener<Void, Void> listener) {
            this.listener = listener;
        }

        @Override
        public void onSuccess(final Bundle values) {
            Toast.makeText(context, "认证成功", Toast.LENGTH_SHORT).show();

            Log.d(TAG, values.toString());

            WeiboAccount.this.uid = values.getString("uid");
            WeiboAccount.this.accessToken = values.getString("access_token");
            WeiboAccount.this.expiresIn = values.getString("expires_in");

            getWeiboAPI().show(uid, new APIListener<String, Exception>() {

                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject info = new JSONObject(result);

                        WeiboAccount.this.username = info
                                .getString("screen_name");

                        Settings.saveAccount(context, WeiboAccount.this);

                        Toast.makeText(context, "绑定成功", Toast.LENGTH_SHORT)
                                .show();

                        listener.onSuccess(null);
                    } catch (JSONException e) {
                        Toast.makeText(context, e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.e(TAG, "exception", e);
                        listener.onError(null);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                    listener.onError(null);
                }
            });
        }

        @Override
        public void onError(Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            listener.onError(null);
        }

    }

}
