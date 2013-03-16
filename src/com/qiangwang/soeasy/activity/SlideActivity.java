package com.qiangwang.soeasy.activity;

import android.app.Activity;
import android.content.Intent;

import com.qiangwang.soeasy.R;


public abstract class SlideActivity extends Activity {
    
    @Override
    public void startActivityForResult(Intent intent, int requestCode){
        //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        super.startActivityForResult(intent, 500);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
