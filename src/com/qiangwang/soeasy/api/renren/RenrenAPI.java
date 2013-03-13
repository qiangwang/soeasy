package com.qiangwang.soeasy.api.renren;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.qiangwang.soeasy.api.API;
import com.qiangwang.soeasy.api.APIException;
import com.qiangwang.soeasy.api.APIListener;
import com.qiangwang.soeasy.api.APIParameters;
import com.qiangwang.soeasy.api.APIResult;

public class RenrenAPI extends API {

    private static final String API_URL = "https://api.renren.com/restserver.do";

    private static final String FEED_TYPE_ALL = "10,11,20,21,22,23,30,31,32,33,34,35,36,,40,41,,50,51,52,53,54,55";

    private String accessToken;

    public RenrenAPI(String accessToken) {
        this.accessToken = accessToken;
    }

    public void getUsersInfo(APIListener<String, Exception> listener) {
        APIParameters params = new APIParameters();
        params.put("method", "users.getInfo");
        post(API_URL, params, listener);
    }

    public void getFeed(APIListener<String, Exception> listener) {
        APIParameters params = new APIParameters();
        params.put("method", "feed.get");
        params.put("type", FEED_TYPE_ALL);
        params.put("page", 1);
        params.put("count", 10);

        post(API_URL, params, listener);
    }

    @Override
    protected void fillParams(APIParameters params) {
        params.put("v", "1.0");
        params.put("access_token", accessToken);
        params.put("format", "JSON");
    }

    protected APIException checkResult(APIResult apiResult) {
        String errorMsg = null;
        String errorCode = null;
        int httpCode = apiResult.getHttpCode();
        String errorURI = apiResult.getUrl();

        JSONObject jResult = null;
        try {
            jResult = new JSONObject(apiResult.getResult());
        } catch (JSONException e) {

        }

        if (jResult != null) {
            errorMsg = jResult.optString("error_msg");
            errorCode = jResult.optString("error_code");
        }

        if (!TextUtils.isEmpty(errorMsg) || !TextUtils.isEmpty(errorCode) || httpCode != 200) {
            return new APIException(errorMsg, errorCode, httpCode, errorURI);
        }

        return null;
    }

}
