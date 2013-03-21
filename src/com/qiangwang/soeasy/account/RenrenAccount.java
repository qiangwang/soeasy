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
import com.qiangwang.soeasy.api.renren.RenrenAPI;
import com.qiangwang.soeasy.api.renren.RenrenAuth;
import com.qiangwang.soeasy.message.Attachment;
import com.qiangwang.soeasy.message.BlogAttachment;
import com.qiangwang.soeasy.message.ImageAttachment;
import com.qiangwang.soeasy.message.NewsMessage;

public class RenrenAccount extends Account {

	public static final String TAG = "RenrenAccount";

	private static final String CLIENT_ID = "228038";
	private static final String REDIRECT_URI = "http://graph.renren.com/oauth/login_success.html";

	private RenrenAuth renrenAuth;
	private RenrenAPI renrenAPI;

	private String accessToken;
	private String expiresIn;

	private long lastNewsId;

	public RenrenAccount() {
		super();
	}

	private RenrenAuth getRenrenAuth() {
		if (renrenAuth == null) {
			renrenAuth = new RenrenAuth(CLIENT_ID, REDIRECT_URI);
		}
		return renrenAuth;
	}

	private RenrenAPI getRenrenAPI() {
		if (renrenAPI == null && accessToken != null) {
			renrenAPI = new RenrenAPI(accessToken);
		}
		return renrenAPI;
	}

	@Override
	public View getAuthView(Context context, APIListener<Void, Void> listener) {
		return getRenrenAuth().getAuthView(context,
				new RenrenAuthListener(listener));
	}

	@Override
	public void getLatestNews(
			final APIListener<ArrayList<NewsMessage>, Exception> listener) {
		getRenrenAPI().getFeed(new APIListener<String, Exception>() {

			@Override
			public void onSuccess(String result) {

				long oldLastNewsId = lastNewsId;

				ArrayList<NewsMessage> newsMessages = new ArrayList<NewsMessage>();

				try {
					JSONArray jFeeds = new JSONArray(result);

					for (int i = 0; i < jFeeds.length(); i++) {
						JSONObject jFeed = jFeeds.getJSONObject(i);

						long id = jFeed.getLong("post_id");

						if (id <= oldLastNewsId)
							continue;

						if (id > lastNewsId) {
							lastNewsId = id;
						}

						Account author = new RenrenAccount();
						author.setUid(jFeed.getString("actor_id"));
						author.setUsername(jFeed.getString("name"));
						author.setPhotoUrl(jFeed.getString("headurl"));

						JSONObject jComments = jFeed.getJSONObject("comments");

						// get content according to feedType
						String content = jFeed.getString("title");
						Attachment attachment = null;

						int feedType = jFeed.getInt("feed_type");

						if (feedType >= 10 && feedType < 20) {
							content = jFeed.getString("message");
						} else if (feedType >= 20 && feedType < 30) {
							content = jFeed.getString("prefix");
							attachment = new BlogAttachment(jFeed
									.getString("title"), jFeed
									.getString("description"), jFeed
									.getString("href"));
						} else if (feedType >= 30 && feedType < 40) {
							content = jFeed.getString("title");
							JSONObject jAttachment = jFeed.getJSONArray(
									"attachment").getJSONObject(0);
							attachment = new ImageAttachment(jAttachment
									.getString("src"));
						}

						NewsMessage newsMessage = new NewsMessage(
								RenrenAccount.this, id, author, content, jFeed
										.getString("update_time"), jComments
										.getInt("count"), attachment);

						newsMessages.add(newsMessage);
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

	private class RenrenAuthListener implements APIListener<Bundle, Exception> {

		private APIListener<Void, Void> listener;

		public RenrenAuthListener(APIListener<Void, Void> listener) {
			this.listener = listener;
		}

		@Override
		public void onSuccess(final Bundle values) {
			Toast.makeText(App.getAppContext(), "认证成功", Toast.LENGTH_SHORT)
					.show();

			Log.d(TAG, values.toString());

			RenrenAccount.this.accessToken = values.getString("access_token");
			RenrenAccount.this.expiresIn = values.getString("expires_in");

			getRenrenAPI().getUsersInfo(new APIListener<String, Exception>() {

				@Override
				public void onSuccess(String result) {
					try {
						JSONArray infos = new JSONArray(result);
						JSONObject info = infos.getJSONObject(0);

						RenrenAccount.this.uid = info.getString("uid");
						RenrenAccount.this.username = info.getString("name");
						RenrenAccount.this.photoUrl = info.getString("headurl");

						Settings.saveAccount(RenrenAccount.this);

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
