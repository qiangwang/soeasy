package com.qiangwang.soeasy.activity;

import android.app.Activity;
import android.os.Bundle;

import com.qiangwang.soeasy.account.Account;
import com.qiangwang.soeasy.api.APIListener;

public class AccountsAuthActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String name = getIntent().getStringExtra("name");

		try {
			Class<?> c = Class.forName(name);
			Account account = (Account) c.newInstance();

			setContentView(account.getAuthView(this,
					new APIListener<Void, Void>() {

						@Override
						public void onSuccess(Void result) {
							AccountsAuthActivity.this.finish();
						}

						@Override
						public void onError(Void e) {
							AccountsAuthActivity.this.finish();
						}

					}));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
