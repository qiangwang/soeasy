package com.qiangwang.soeasy.activity;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiangwang.soeasy.R;
import com.qiangwang.soeasy.Settings;
import com.qiangwang.soeasy.account.Account;
import com.qiangwang.soeasy.api.ViewUtils;

public class AccountsActivity extends TabActivity {

    public static final String TAG = "AccountsActivity";

    private LinearLayout accountsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountsContent = (LinearLayout) findViewById(R.id.accounts_content);
    }

    @Override
    protected void onResume() {
        super.onResume();

        accountsContent.removeAllViews();

        Map<String, Account> accounts = Settings.getAccounts();

        for (String key : accounts.keySet()) {
            Account account = accounts.get(key);

            LinearLayout item = (LinearLayout) LayoutInflater.from(this)
                    .inflate(R.layout.fragment_account_item, null);

            ImageView photoView = (ImageView) item
                    .findViewById(R.id.account_item_photo);
            ViewUtils.setImage(photoView, account.getPhotoUrl());

            TextView usernameView = (TextView) item
                    .findViewById(R.id.account_item_username);
            usernameView.setText(account.getUsername());

            item.setTag(account);

            accountsContent.addView(item);
        }

    }

    public void showAdd(View view) {
        Intent intent = new Intent(this, AccountsAddActivity.class);
        slideActivity(intent);
    }

    public void showDetail(View view) {
        Account account = (Account) view.getTag();

        Intent intent = new Intent(this, AccountsDetailActivity.class);
        intent.putExtra("accountKey", Settings.getAccountKey(account));
        
        slideActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_accounts;
    }
}
