package com.qiangwang.soeasy.activity;

import android.app.Activity;
import android.content.Intent;

import com.qiangwang.soeasy.R;


public abstract class SlideActivity extends Activity {
    
    public void slideActivity(Intent intent){
        startActivityForResult(intent, 0);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
