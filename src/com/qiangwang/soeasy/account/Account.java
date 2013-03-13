package com.qiangwang.soeasy.account;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;

import com.qiangwang.soeasy.JSONable;
import com.qiangwang.soeasy.api.APIListener;
import com.qiangwang.soeasy.message.NewsMessage;

public abstract class Account implements JSONable {

    protected Context context;
    protected String uid;
    protected String username;

    public Account(Context context) {
        this.context = context;
    }
    
    public abstract View getAuthView(APIListener<Void, Void> listener);

    public abstract void getLatestNews(
            APIListener<ArrayList<NewsMessage>, Exception> handler);

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
}
