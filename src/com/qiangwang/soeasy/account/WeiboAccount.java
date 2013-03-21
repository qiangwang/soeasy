package com.qiangwang.soeasy.account;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qiangwang.soeasy.App;
import com.qiangwang.soeasy.Settings;
import com.qiangwang.soeasy.api.APIListener;
import com.qiangwang.soeasy.api.weibo.WeiboAPI;
import com.qiangwang.soeasy.api.weibo.WeiboAPI.FEATURE;
import com.qiangwang.soeasy.api.weibo.WeiboAuth;
import com.qiangwang.soeasy.message.NewsMessage;

public class WeiboAccount extends Account {

	public static final String TAG = "WeiboAccount";

	private static final String CLIENT_ID = "1796068784";
	private static final String REDIRECT_URI = "http://www.sina.com";

	private WeiboAuth weiboAuth;
	private WeiboAPI weiboAPI;

	private String accessToken;
	private String expiresIn;

	private long lastNewsId;

	public WeiboAccount() {
		super();
	}

	private WeiboAuth getWeiboAuth() {
		if (weiboAuth == null) {
			weiboAuth = new WeiboAuth(CLIENT_ID, REDIRECT_URI);
		}
		return weiboAuth;
	}

	private WeiboAPI getWeiboAPI() {
		if (weiboAPI == null && accessToken != null) {
			weiboAPI = new WeiboAPI(accessToken);
		}
		return weiboAPI;
	}

	@Override
	public View getAuthView(Context context, APIListener<Void, Void> listener) {
		return getWeiboAuth().getAuthView(context,
				new WeiboAuthListener(listener));
	}

	@Override
	public void getLatestNews(
			final APIListener<ArrayList<NewsMessage>, Exception> listener) {
		getWeiboAPI().homeTimeline(lastNewsId, 0, 10, 1, false, FEATURE.ALL,
				false, new APIListener<String, Exception>() {

					@Override
					public void onSuccess(String result) {

						ArrayList<NewsMessage> newsMessages = new ArrayList<NewsMessage>();

						try {
							JSONObject jNews = new JSONObject(result);
							JSONArray jStatuses = jNews
									.getJSONArray("statuses");

							for (int i = 0; i < jStatuses.length(); i++) {
								JSONObject jStatus = jStatuses.getJSONObject(i);

								NewsMessage newsMessage = jsonToNews(jStatus);

								newsMessages.add(newsMessage);

								if (newsMessage.getId() > lastNewsId) {
									lastNewsId = newsMessage.getId();
								}
							}

							listener.onSuccess(newsMessages);
						} catch (JSONException e) {
							listener.onError(e);
						}
					}

					@Override
					public void onError(Exception e) {
						Toast.makeText(App.getAppContext(), e.getMessage(),
								Toast.LENGTH_LONG).show();
					}

					private NewsMessage jsonToNews(JSONObject jStatus)
							throws JSONException {
						if (jStatus == null)
							return null;

						long id = jStatus.getLong("id");

						JSONObject jUser = jStatus.getJSONObject("user");
						Account author = new WeiboAccount();
						author.setUid(jUser.getString("id"));
						author.setUsername(jUser.getString("screen_name"));
						author.setPhotoUrl(jUser.getString("profile_image_url"));

						String smallPicUrl = jStatus.optString("thumbnail_pic");

						NewsMessage retweeted = jsonToNews(jStatus
								.optJSONObject("retweeted_status"));

						return new NewsMessage(WeiboAccount.this, id, author,
								jStatus.getString("text"), jStatus
										.getString("created_at"), jStatus
										.getInt("comments_count"), smallPicUrl,
								retweeted);
					}
				});
	}

	@Override
	public String toJSON() {
		String json = "";

		try {
			JSONObject jAccount = new JSONObject(super.toJSON());

			jAccount.put("accessToken", accessToken);
			jAccount.put("expiresIn", expiresIn);

			json = jAccount.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

	@Override
	public void fromJSON(String json) {
		super.fromJSON(json);
		try {
			JSONObject jAccount = new JSONObject(json);
			accessToken = jAccount.optString("accessToken");
			expiresIn = jAccount.optString("expiresIn");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private class WeiboAuthListener implements APIListener<Bundle, Exception> {

		private APIListener<Void, Void> listener;

		public WeiboAuthListener(APIListener<Void, Void> listener) {
			this.listener = listener;
		}

		@Override
		public void onSuccess(final Bundle values) {
			Toast.makeText(App.getAppContext(), "认证成功", Toast.LENGTH_SHORT)
					.show();

			Log.d(TAG, values.toString());

			WeiboAccount.this.uid = values.getString("uid");
			WeiboAccount.this.accessToken = values.getString("access_token");
			WeiboAccount.this.expiresIn = values.getString("expires_in");

			getWeiboAPI().show(uid, new APIListener<String, Exception>() {

				@Override
				public void onSuccess(String result) {
					try {
						JSONObject info = new JSONObject(result);

						WeiboAccount.this.username = info
								.getString("screen_name");
						WeiboAccount.this.photoUrl = info
								.getString("profile_image_url");

						Settings.saveAccount(WeiboAccount.this);

						Toast.makeText(App.getAppContext(), "绑定成功",
								Toast.LENGTH_SHORT).show();

						listener.onSuccess(null);
					} catch (JSONException e) {
						Toast.makeText(App.getAppContext(), e.getMessage(),
								Toast.LENGTH_LONG).show();
						Log.e(TAG, "exception", e);
						listener.onError(null);
					}
				}

				@Override
				public void onError(Exception e) {
					Toast.makeText(App.getAppContext(), e.getMessage(),
							Toast.LENGTH_LONG).show();
					listener.onError(null);
				}
			});
		}

		@Override
		public void onError(Exception e) {
			Toast.makeText(App.getAppContext(), e.getMessage(),
					Toast.LENGTH_LONG).show();
			listener.onError(null);
		}

	}

}
