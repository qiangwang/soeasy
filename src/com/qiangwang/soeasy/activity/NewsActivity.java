package com.qiangwang.soeasy.activity;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiangwang.soeasy.R;
import com.qiangwang.soeasy.Settings;
import com.qiangwang.soeasy.account.Account;
import com.qiangwang.soeasy.api.APIListener;
import com.qiangwang.soeasy.message.NewsMessage;

public class NewsActivity extends Activity {
    public static final String TAG = "NewsActivity";

    private LinearLayout newsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_news);

        newsContent = (LinearLayout) findViewById(R.id.news_content);
    }

    public void loadLatest() {
        Map<String, Account> accounts = null;

        try {
            accounts = Settings.getAccounts(this);
            Log.d(TAG, accounts.toString());
        } catch (Exception e) {
            Log.e(TAG, "exception", e);
            return;
        }

        for (String key : accounts.keySet()) {
            Account account = accounts.get(key);

            account.getLatestNews(new APIListener<ArrayList<NewsMessage>, Exception>() {

                @Override
                public void onSuccess(ArrayList<NewsMessage> newsMessages) {
                    for (NewsMessage news : newsMessages) {
                        LinearLayout item = (LinearLayout) LayoutInflater.from(
                                NewsActivity.this).inflate(
                                R.layout.fragment_news_item, null);

                        TextView usernameView = (TextView) item
                                .findViewById(R.id.news_item_username);
                        usernameView.setText(news.getAuthor().getUsername());

                        TextView contentView = (TextView) item
                                .findViewById(R.id.news_item_content);
                        contentView.setText(news.getContent());

                        newsContent.addView(item, 0);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, "getLatestNews", e);
                    Toast.makeText(NewsActivity.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

            });

        }

    }

    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }
}
