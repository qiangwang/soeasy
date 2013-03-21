package com.qiangwang.soeasy.activity;

import java.util.ArrayList;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiangwang.soeasy.R;
import com.qiangwang.soeasy.Settings;
import com.qiangwang.soeasy.account.Account;
import com.qiangwang.soeasy.api.APIListener;
import com.qiangwang.soeasy.api.ViewUtils;
import com.qiangwang.soeasy.message.Attachment;
import com.qiangwang.soeasy.message.BlogAttachment;
import com.qiangwang.soeasy.message.ImageAttachment;
import com.qiangwang.soeasy.message.NewsMessage;
import com.qiangwang.soeasy.message.RetweetAttachment;

public class NewsActivity extends TabActivity {
	public static final String TAG = "NewsActivity";

	private LinearLayout newsContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		newsContent = (LinearLayout) findViewById(R.id.news_content);
	}

	public void loadLatest() {
		Map<String, Account> accounts = Settings.getAccounts();

		for (String key : accounts.keySet()) {
			Account account = accounts.get(key);

			account.getLatestNews(new APIListener<ArrayList<NewsMessage>, Exception>() {

				@Override
				public void onSuccess(ArrayList<NewsMessage> newsMessages) {
					for (NewsMessage news : newsMessages) {
						View item = getNewsItemView(news);
						newsContent.addView(item, 0);
					}
				}

				@Override
				public void onError(Exception e) {
					Log.e(TAG, "getLatestNews", e);
					Toast.makeText(NewsActivity.this, e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}

			});

		}

	}

	private View getNewsItemView(NewsMessage news) {
		LinearLayout item = (LinearLayout) LayoutInflater.from(
				NewsActivity.this).inflate(R.layout.fragment_news_item, null);

		ImageView photoView = (ImageView) item
				.findViewById(R.id.news_item_photo);
		ViewUtils.setImage(photoView, news.getAuthor().getPhotoUrl());

		TextView usernameView = (TextView) item
				.findViewById(R.id.news_item_username);
		usernameView.setText(news.getAuthor().getUsername());

		TextView contentView = (TextView) item
				.findViewById(R.id.news_item_content);
		contentView.setText(news.getContent());

		View attachmentView = getAttachmentView(news.getAttachment());
		if (attachmentView != null) {
			LinearLayout attachmentLayout = (LinearLayout) item
					.findViewById(R.id.news_item_attachment);
			attachmentLayout.addView(attachmentView);
		}

		return item;
	}

	private View getAttachmentView(Attachment attachment) {
		if (attachment instanceof ImageAttachment) {
			ImageAttachment imageAttachment = (ImageAttachment) attachment;
			ImageView imageAttachview = (ImageView) LayoutInflater.from(
					NewsActivity.this).inflate(
					R.layout.fragment_attachment_image, null);
			ViewUtils.setImage(imageAttachview,
					imageAttachment.getSmallPicUrl());
			return imageAttachview;
		} else if (attachment instanceof RetweetAttachment) {
			RetweetAttachment retweetAttachment = (RetweetAttachment) attachment;
			View retweetedView = getNewsItemView(retweetAttachment
					.getRetweeted());
			return retweetedView;
		} else if (attachment instanceof BlogAttachment) {
			BlogAttachment blogAttachment = (BlogAttachment) attachment;

			LinearLayout blogView = (LinearLayout) LayoutInflater.from(
					NewsActivity.this).inflate(
					R.layout.fragment_attachment_blog, null);

			TextView titleView = (TextView) blogView
					.findViewById(R.id.blog_title);
			titleView.setText(blogAttachment.getTitle());

			TextView summaryView = (TextView) blogView
					.findViewById(R.id.blog_summary);
			summaryView.setText(blogAttachment.getSummary());

			return blogView;
		} else {
			return null;
		}
	}

	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadLatest();
		Log.d(TAG, "onResume");
	}

	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
	}

	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
	}

	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart");
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_news;
	}
}
