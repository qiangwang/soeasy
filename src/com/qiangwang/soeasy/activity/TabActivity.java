package com.qiangwang.soeasy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.qiangwang.soeasy.R;

public abstract class TabActivity extends SlideActivity {

    public static final String TAG = "TabActivity";

    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        container = (FrameLayout) findViewById(R.id.tabs_content);

        View view = LayoutInflater.from(this).inflate(
                getContentLayoutId(), null);
        
        container.addView(view);
    }

    protected abstract int getContentLayoutId();

    public void changeContent(View view) {
        String name = (String) view.getTag();
        Class<?> c = null;

        try {
            c = Class.forName(name);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "changeContent", e);
        }

        if (c != null) {
            Intent intent = new Intent(this, c);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

    }
    
    @Override
    public void onBackPressed() {
    	moveTaskToBack(true);
    }

}
