package com.qiangwang.soeasy;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.qiangwang.soeasy.account.Account;

public class Settings {

	public static final String TAG = "Settings";

	private static final String ACCOUNTS = "com.qiangwang.soeasy.accounts";

	private static Map<String, Account> accounts;

	public static String getAccountKey(Account account) {
		return account.getClass().getName() + "." + account.getUid();
	}

	private static Account prefToAccount(String pref) {
		try {
			JSONObject jAccount = new JSONObject(pref);

			String className = jAccount.optString("class");
			Class<?> c = Class.forName(className);
			Account account = (Account) c.newInstance();
			account.fromJSON(jAccount.optString("account"));
			return account;
		} catch (Exception e) {
			Log.e(TAG, "getAccounts", e);
			return null;
		}
	}

	public static Account getAccount(String key) {
		if (accounts != null)
			return accounts.get(key);

		SharedPreferences pref = App.getAppContext().getSharedPreferences(
				ACCOUNTS, Context.MODE_PRIVATE);

		return prefToAccount(pref.getString(key, ""));
	}

	public static void saveAccount(Account account) throws JSONException {
		JSONObject jAccount = new JSONObject();

		jAccount.put("class", account.getClass().getName());
		jAccount.put("account", account.toJSON());

		String key = Settings.getAccountKey(account);

		SharedPreferences pref = App.getAppContext().getSharedPreferences(
				ACCOUNTS, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(key, jAccount.toString());
		editor.commit();

		if (accounts != null) {
			accounts.put(key, account);
		}
	}

	public static void delAccount(Account account) {
		String key = Settings.getAccountKey(account);

		SharedPreferences pref = App.getAppContext().getSharedPreferences(
				ACCOUNTS, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.remove(key);
		editor.commit();

		if (accounts != null) {
			accounts.remove(key);
		}
	}

	public static void delAllAccounts() {
		SharedPreferences pref = App.getAppContext().getSharedPreferences(
				ACCOUNTS, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}

	public static Map<String, Account> getAccounts() {
		if (accounts != null)
			return accounts;

		accounts = new HashMap<String, Account>();

		SharedPreferences pref = App.getAppContext().getSharedPreferences(
				ACCOUNTS, Context.MODE_PRIVATE);
		Map<String, ?> accountsMap = pref.getAll();
		for (String key : accountsMap.keySet()) {
			Account account = prefToAccount(pref.getString(key, ""));
			if (account != null)
				accounts.put(key, account);
		}

		return accounts;
	}

}