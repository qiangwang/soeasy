package com.qiangwang.soeasy.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class APITask extends AsyncTask<Object, Void, Map<String, Object>> {

    public static final String TAG = "APITask";

    @Override
    protected Map<String, Object> doInBackground(Object... params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String url = (String) params[0];
        APIParameters apiParams = (APIParameters) params[1];
        String httpMethod = (String) params[2];
        APIListener<APIResult, Exception> listener = (APIListener<APIResult, Exception>) params[3];

        try {
            Log.d(TAG, url);
            Log.d(TAG, apiParams.toString());

            APIResult result = HttpManager.openUrl(url, httpMethod, apiParams,
                    apiParams.get("pic"));

            resultMap.put("result", result);

            Log.d(TAG, String.valueOf(result.getHttpCode()));
            
            String json = "";
			try {
				json = new JSONObject(result.getResult()).toString(2);
			} catch (JSONException e) {
				try {
					json = new JSONArray(result.getResult()).toString(2);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
            Log.d(TAG, json);
        } catch (IOException e) {
            resultMap.put("result", e);
            Log.d(TAG, "api exception", e);
        }
        resultMap.put("listener", listener);
        return resultMap;
    }

    @Override
    protected void onPostExecute(Map<String, Object> resultMap) {
        Object result = resultMap.get("result");
        APIListener<APIResult, Exception> listener = (APIListener<APIResult, Exception>) resultMap
                .get("listener");
        if (result instanceof APIResult) {
            listener.onSuccess((APIResult) result);
        } else if (result instanceof Exception) {
            listener.onError((Exception) result);
        }
    }
}
