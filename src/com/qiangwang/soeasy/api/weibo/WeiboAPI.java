package com.qiangwang.soeasy.api.weibo;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.qiangwang.soeasy.api.API;
import com.qiangwang.soeasy.api.APIException;
import com.qiangwang.soeasy.api.APIListener;
import com.qiangwang.soeasy.api.APIParameters;
import com.qiangwang.soeasy.api.APIResult;

public class WeiboAPI extends API {

    public static final String TAG = "WeiboAPI";
    /**
     * 访问微博服务接口的地址
     */
    public static final String API_URL = "https://api.weibo.com/2";

    public static final String USER_API_URL = API_URL + "/users";

    public static final String STATUS_API_URL = API_URL + "/statuses";

    private String accessToken;

    public WeiboAPI(String accessToken) {
        this.accessToken = accessToken;
    }

    public enum FEATURE {
        ALL, ORIGINAL, PICTURE, VIDEO, MUSICE
    }

    public enum SRC_FILTER {
        ALL, WEIBO, WEIQUN
    }

    public enum TYPE_FILTER {
        ALL, ORIGAL
    }

    public enum AUTHOR_FILTER {
        ALL, ATTENTIONS, STRANGER
    }

    public enum TYPE {
        STATUSES, COMMENTS, MESSAGE
    }

    public enum EMOTION_TYPE {
        FACE, ANI, CARTOON
    }

    public enum LANGUAGE {
        cnname, twname
    }

    public enum SCHOOL_TYPE {
        COLLEGE, SENIOR, TECHNICAL, JUNIOR, PRIMARY
    }

    public enum CAPITAL {
        A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z
    }

    public enum FRIEND_TYPE {
        ATTENTIONS, FELLOWS
    }

    public enum RANGE {
        ATTENTIONS, ATTENTION_TAGS, ALL
    }

    public enum USER_CATEGORY {
        DEFAULT, ent, hk_famous, model, cooking, sports, finance, tech, singer, writer, moderator, medium, stockplayer
    }

    public enum STATUSES_TYPE {
        ENTERTAINMENT, FUNNY, BEAUTY, VIDEO, CONSTELLATION, LOVELY, FASHION, CARS, CATE, MUSIC
    }

    public enum COUNT_TYPE {
        /**
         * 新微博数
         */
        STATUS,
        /**
         * 新粉丝数
         */
        FOLLOWER,
        /**
         * 新评论数
         */
        CMT,
        /**
         * 新私信数
         */
        DM,
        /**
         * 新提及我的微博数
         */
        MENTION_STATUS,
        /**
         * 新提及我的评论数
         */
        MENTION_CMT
    }

    /**
     * 分类
     * 
     * @author xiaowei6@staff.sina.com.cn
     * 
     */
    public enum SORT {
        Oauth2AccessToken, SORT_AROUND
    }

    public enum SORT2 {
        SORT_BY_TIME, SORT_BY_HOT
    }

    public enum SORT3 {
        SORT_BY_TIME, SORT_BY_DISTENCE
    }

    public enum COMMENTS_TYPE {
        NONE, CUR_STATUSES, ORIGAL_STATUSES, BOTH
    }

    public void show(String uid, APIListener<String, Exception> listener) {
        APIParameters params = new APIParameters();
        params.put("uid", uid);
        get(USER_API_URL + "/show.json", params, listener);
    }

    /**
     * 获取当前登录用户及其所关注用户的最新微博
     * 
     * @param since_id
     *            若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
     * @param max_id
     *            若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
     * @param count
     *            单页返回的记录条数，默认为50。
     * @param page
     *            返回结果的页码，默认为1。
     * @param base_app
     *            是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
     * @param feature
     *            过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
     * @param trim_user
     *            返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false。
     * @param listener
     */
    public void homeTimeline(long since_id, long max_id, int count, int page,
            boolean base_app, FEATURE feature, boolean trim_user,
            APIListener<String, Exception> listener) {
        APIParameters params = new APIParameters();
        params.put("since_id", since_id);
        params.put("max_id", max_id);
        params.put("count", count);
        params.put("page", page);
        if (base_app) {
            params.put("base_app", 1);
        } else {
            params.put("base_app", 0);
        }
        params.put("feature", feature.ordinal());
        if (trim_user) {
            params.put("trim_user", 1);
        } else {
            params.put("trim_user", 0);
        }
        get(STATUS_API_URL + "/home_timeline.json", params, listener);
    }

    @Override
    protected void fillParams(APIParameters params) {
        params.put("access_token", accessToken);
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
            errorMsg = jResult.optString("error");
            errorCode = jResult.optString("error_code");
            errorURI = jResult.optString("request");
        }

        if (!TextUtils.isEmpty(errorMsg) || !TextUtils.isEmpty(errorCode) || httpCode != 200) {
            return new APIException(errorMsg, errorCode, httpCode, errorURI);
        }

        return null;
    }

}
