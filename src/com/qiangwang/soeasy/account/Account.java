package com.qiangwang.soeasy.account;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;

import com.qiangwang.soeasy.JSONable;
import com.qiangwang.soeasy.api.APIListener;
import com.qiangwang.soeasy.message.NewsMessage;

public abstract class Account implements JSONable {

    protected Context context;
    protected String uid;
    protected String username;
    protected String photo;

    public Account(Context context) {
        this.context = context;
    }

    public abstract View getAuthView(APIListener<Void, Void> listener);

    public abstract void getLatestNews(
            APIListener<ArrayList<NewsMessage>, Exception> handler);

    @Override
    public String toJSON() {
        JSONObject jAccount = new JSONObject();

        try {
            jAccount.put("uid", uid);
            jAccount.put("username", username);
            jAccount.put("photo", photo);
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
            photo = jAccount.optString("photo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Context getContext() {
        return context;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
