package com.qiangwang.soeasy.activity;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiangwang.soeasy.R;
import com.qiangwang.soeasy.Settings;
import com.qiangwang.soeasy.account.Account;

public class AccountsActivity extends Activity {

    private LinearLayout accountsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        accountsContent = (LinearLayout) findViewById(R.id.accounts_content);
    }

    @Override
    protected void onResume() {
        super.onResume();

        accountsContent.removeAllViews();
        
        Map<String, Account> accounts = null;

        accounts = Settings.getAccounts(getApplicationContext());

        for (String key : accounts.keySet()) {
            Account account = accounts.get(key);

            LinearLayout item = (LinearLayout) LayoutInflater.from(this)
                    .inflate(R.layout.fragment_account_item, null);

            TextView usernameView = (TextView) item
                    .findViewById(R.id.account_item_username);
            usernameView.setText(account.getUsername());

            Button delButton = (Button) item
                    .findViewById(R.id.account_item_del);
            delButton.setTag(account);

            accountsContent.addView(item);
        }

    }

    public void showAdd(View view) {
        Intent intent = new Intent(this, AccountsAddActivity.class);
        startActivity(intent);
    }

    public void showDelete(View view) {
        Account account = (Account) ((Button) view).getTag();
        Settings.delAccount(this, account);
        accountsContent.removeView((View) view.getParent());
        Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
    }

    public void showDeleteAll(View view) {
        Settings.delAllAccounts(this);
        accountsContent.removeAllViews();
        Toast.makeText(this, "已全部删除", Toast.LENGTH_SHORT).show();
    }
}
