package com.qiangwang.soeasy;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.qiangwang.soeasy.account.Account;

public class Settings {

    private static final String ACCOUNTS = "com.qiangwang.soeasy.accounts";

    private static Map<String, Account> accounts;

    private static String getAccountKey(Account account) {
        return account.getClass().getName() + "." + account.getUid();
    }

    public static void saveAccount(Context context, Account account)
            throws JSONException {
        JSONObject jAccount = new JSONObject();

        jAccount.put("class", account.getClass().getName());
        jAccount.put("account", account.toJSON());

        String key = Settings.getAccountKey(account);

        SharedPreferences pref = context.getSharedPreferences(ACCOUNTS,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(key, jAccount.toString());
        editor.commit();

        if (accounts != null) {
            accounts.put(key, account);
        }
    }

    public static void delAccount(Context context, Account account) {
        String key = Settings.getAccountKey(account);

        SharedPreferences pref = context.getSharedPreferences(ACCOUNTS,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();

        if (accounts != null) {
            accounts.remove(key);
        }
    }

    public static void delAllAccounts(Context context) {
        SharedPreferences pref = context.getSharedPreferences(ACCOUNTS,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public static Map<String, Account> getAccounts(Context context) {
        if (accounts != null)
            return accounts;

        accounts = new HashMap<String, Account>();

        SharedPreferences pref = context.getSharedPreferences(ACCOUNTS,
                Context.MODE_PRIVATE);
        Map<String, ?> accountsMap = pref.getAll();
        for (String key : accountsMap.keySet()) {
            JSONObject jAccount;
            try {
                jAccount = new JSONObject(pref.getString(key, ""));

                String className = jAccount.optString("class");
                Class<?> c = Class.forName(className);
                Account account = (Account) c.getDeclaredConstructor(
                        Context.class).newInstance(context.getApplicationContext());
                account.fromJSON(jAccount.optString("account"));
                accounts.put(key, account);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return accounts;
    }

}