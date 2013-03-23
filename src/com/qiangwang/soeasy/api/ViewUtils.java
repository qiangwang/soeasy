package com.qiangwang.soeasy.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;

public class ViewUtils {

	public static final String TAG = "ViewUtils";

	private static LruCache<String, Bitmap> bitmapCache;

	static {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

	    // Use 1/8th of the available memory for this memory cache.
	    final int cacheSize = maxMemory / 8;
		
		bitmapCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in kilobytes rather than
				// number of items.
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}
		};
	}

	@SuppressLint("SetJavaScriptEnabled")
	public static WebView getAuthWebView(Context context) {
		WebView mWebView = new WebView(context);
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.getSettings().setJavaScriptEnabled(true);
		return mWebView;
	}

	public static void setImage(final ImageView view, final String url) {
		if (TextUtils.isEmpty(url))
			return;

		Bitmap bitmap = bitmapCache.get(url);
		if(bitmap != null){
			view.setImageBitmap(bitmap);
			return;
		}
		
		new ImageTask().execute(url, new APIListener<Bitmap, Exception>() {

			@Override
			public void onSuccess(Bitmap result) {
				view.setImageBitmap(result);
				bitmapCache.put(url, result);
			}

			@Override
			public void onError(Exception e) {
				Log.e(TAG, "setImage:" + url, e);
			}

		});

	}

}
