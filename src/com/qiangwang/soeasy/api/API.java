package com.qiangwang.soeasy.api;

public abstract class API {

    public static final String TAG = "API";

    public void get(String url, APIParameters params,
            APIListener<String, Exception> listener) {
        request(url, params, "GET", listener);
    }

    public void post(String url, APIParameters params,
            APIListener<String, Exception> listener) {
        request(url, params, "POST", listener);
    }

    public void request(String url, APIParameters params,
            final String httpMethod, APIListener<String, Exception> listener) {
        fillParams(params);
        new APITask()
                .execute(url, params, httpMethod, getAPIListener(listener));
    }

    private APIListener<APIResult, Exception> getAPIListener(
            final APIListener<String, Exception> listener) {
        return new APIListener<APIResult, Exception>() {

            @Override
            public void onSuccess(APIResult result) {
                APIException e = checkResult(result);
                if (e == null) {
                    listener.onSuccess(result.getResult());
                } else {
                    listener.onError(e);
                }
            }

            @Override
            public void onError(Exception e) {
                listener.onError(e);
            }

        };
    }

    protected void fillParams(APIParameters params) {

    };

    protected APIException checkResult(APIResult result) {
        return null;
    }

}
