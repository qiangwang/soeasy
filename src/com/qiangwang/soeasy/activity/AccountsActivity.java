package com.qiangwang.soeasy.activity;

import java.util.Map;

import android.app.Activity;
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

public class AccountsActivity extends Activity {

    public static final String TAG = "AccountsActivity";

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

        accounts = Settings.getAccounts();

        for (String key : accounts.keySet()) {
            Account account = accounts.get(key);

            LinearLayout item = (LinearLayout) LayoutInflater.from(this)
                    .inflate(R.layout.fragment_account_item, null);

            ImageView photoView = (ImageView) item
                    .findViewById(R.id.account_item_photo);
            ViewUtils.setImage(photoView, account.getPhoto());

            TextView usernameView = (TextView) item
                    .findViewById(R.id.account_item_username);
            usernameView.setText(account.getUsername());

            item.setTag(account);

            accountsContent.addView(item);
        }

    }

    public void showAdd(View view) {
        Intent intent = new Intent(this, AccountsAddActivity.class);
        startActivityForResult(intent, 500);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void showDetail(View view) {
        Intent intent = new Intent(this, AccountsDetailActivity.class);
        Account account = (Account) view.getTag();
        intent.putExtra("accountKey", Settings.getAccountKey(account));
        startActivityForResult(intent, 600);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
  
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
