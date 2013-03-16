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
import com.qiangwang.soeasy.api.renren.RenrenAPI;
import com.qiangwang.soeasy.api.renren.RenrenAuth;
import com.qiangwang.soeasy.message.NewsMessage;

public class RenrenAccount extends Account {

    public static final String TAG = "RenrenAccount";

    private static final String CLIENT_ID = "228038";
    private static final String REDIRECT_URI = "http://graph.renren.com/oauth/login_success.html";

    private RenrenAuth renrenAuth;
    private RenrenAPI renrenAPI;

    private String accessToken;
    private String expiresIn;
    private String refreshToken;

    private long lastNewsId;

    public RenrenAccount(Context context) {
        super(context);
    }

    private RenrenAuth getRenrenAuth() {
        if (renrenAuth == null) {
            renrenAuth = new RenrenAuth(CLIENT_ID, REDIRECT_URI);
        }
        return renrenAuth;
    }

    private RenrenAPI getRenrenAPI() {
        if (renrenAPI == null && accessToken != null) {
            renrenAPI = new RenrenAPI(accessToken);
        }
        return renrenAPI;
    }

    @Override
    public View getAuthView(APIListener<Void, Void> listener) {
        return getRenrenAuth().getAuthView(context,
                new RenrenAuthListener(listener));
    }

    @Override
    public void getLatestNews(
            final APIListener<ArrayList<NewsMessage>, Exception> listener) {
        getRenrenAPI().getFeed(new APIListener<String, Exception>() {

            @Override
            public void onSuccess(String result) {

                ArrayList<NewsMessage> newsMessages = new ArrayList<NewsMessage>();

                try {
                    JSONArray jFeeds = new JSONArray(result);

                    for (int i = 0; i < jFeeds.length(); i++) {
                        JSONObject jFeed = jFeeds.getJSONObject(i);

                        long id = jFeed.getLong("post_id");

                        Account author = new RenrenAccount(context);
                        author.setUid(jFeed.getString("actor_id"));
                        author.setUsername(jFeed.getString("name"));
                        author.setPhoto(jFeed.getString("headurl"));

                        JSONObject jComments = jFeed.getJSONObject("comments");

                        NewsMessage newsMessage = new NewsMessage(
                                RenrenAccount.this, id, author, jFeed
                                        .getString("title"), jFeed
                                        .getString("update_time"), jComments
                                        .getInt("count"));

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
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public String toJSON() {
        String json = "";

        try {
            JSONObject jAccount = new JSONObject(super.toJSON());

            jAccount.put("accessToken", accessToken);
            jAccount.put("expiresIn", expiresIn);

            json = jAccount.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    public void fromJSON(String json) {
        super.fromJSON(json);
        try {
            JSONObject jAccount = new JSONObject(json);
            accessToken = jAccount.optString("accessToken");
            expiresIn = jAccount.optString("expiresIn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class RenrenAuthListener implements APIListener<Bundle, Exception> {

        private APIListener<Void, Void> listener;

        public RenrenAuthListener(APIListener<Void, Void> listener) {
            this.listener = listener;
        }

        @Override
        public void onSuccess(final Bundle values) {
            Toast.makeText(context, "认证成功", Toast.LENGTH_SHORT).show();

            Log.d(TAG, values.toString());

            RenrenAccount.this.accessToken = values.getString("access_token");
            RenrenAccount.this.expiresIn = values.getString("expires_in");
            RenrenAccount.this.refreshToken = values.getString("refresh_token");

            getRenrenAPI().getUsersInfo(new APIListener<String, Exception>() {

                @Override
                public void onSuccess(String result) {
                    try {
                        JSONArray infos = new JSONArray(result);
                        JSONObject info = infos.getJSONObject(0);

                        RenrenAccount.this.uid = info.getString("uid");
                        RenrenAccount.this.username = info.getString("name");
                        RenrenAccount.this.photo = info.getString("headurl");

                        Settings.saveAccount(RenrenAccount.this);

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
