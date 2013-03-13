package com.qiangwang.soeasy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qiangwang.soeasy.R;

public class AccountsAddActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_add);
    }
    
    public void addAccount(View view) {
       Intent intent = new Intent(this, AccountsAuthActivity.class);
       intent.putExtra("name", (String)view.getTag());
       startActivity(intent);
    }

}
