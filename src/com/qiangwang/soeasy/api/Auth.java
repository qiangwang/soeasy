package com.qiangwang.soeasy.api;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

public abstract class Auth {

    public abstract View getAuthView(Context context, APIListener<Bundle, Exception> listener);
    
}
