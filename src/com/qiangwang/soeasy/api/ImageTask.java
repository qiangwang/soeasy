package com.qiangwang.soeasy.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class ImageTask extends AsyncTask<Object, Void, Map<String, Object>> {

    public static final String TAG = "ImageTask";

    public static final int CONNECT_TIMEOUT = 5000;
    public static final int READ_TIMEOUT = 10000;

    @Override
    protected Map<String, Object> doInBackground(Object... params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String url = (String) params[0];
        APIListener<Bitmap, Exception> listener = (APIListener<Bitmap, Exception>) params[1];

        try {
            Log.d(TAG, url);

            URLConnection conn = new URL(url).openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) conn
                    .getContent());

            resultMap.put("result", bitmap);
        } catch (IOException e) {
            resultMap.put("result", e);
            Log.d(TAG, "doInBackground", e);
        }
        resultMap.put("listener", listener);
        return resultMap;
    }

    @Override
    protected void onPostExecute(Map<String, Object> resultMap) {
        Object result = resultMap.get("result");
        APIListener<Bitmap, Exception> listener = (APIListener<Bitmap, Exception>) resultMap
                .get("listener");
        if (result instanceof Bitmap) {
            listener.onSuccess((Bitmap) result);
        } else if (result instanceof Exception) {
            listener.onError((Exception) result);
        }
    }

}
