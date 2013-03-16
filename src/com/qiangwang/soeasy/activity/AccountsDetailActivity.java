package com.qiangwang.soeasy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.qiangwang.soeasy.R;
import com.qiangwang.soeasy.Settings;
import com.qiangwang.soeasy.account.Account;

public class AccountsDetailActivity extends Activity {

    private Account account;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_detail);
        
        account = Settings.getAccount(getIntent().getStringExtra("accountKey"));
    }
    
    public void showDelete(View view) {
        Settings.delAccount(account);
        finish();
        Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
    }

}
