package com.qiangwang.soeasy.activity;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.qiangwang.soeasy.R;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup {
    
    public static final String TAG = "MainActivity";
    
    public static Context context;
    
	private FrameLayout container; 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        context = getApplicationContext();
        
        container = (FrameLayout) findViewById(R.id.main_content);
       
        changeContent(findViewById(R.id.main_news_button));
    }
    
    public void changeContent(View view) {	
    	String id = "";
    	Class<?> c = null;
    	
    	switch(view.getId()){
    	case R.id.main_news_button:
    		id = "news";
    		c = NewsActivity.class;
    		break;
    	case R.id.main_accounts_button:
    		id = "accounts";
    		c = AccountsActivity.class;
    		break;
    	}
    	
    	if(c != null){
    		container.removeAllViews();
    		Intent intent = new Intent(this, c);
    		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
           container.addView(getLocalActivityManager().startActivity(id, intent).getDecorView());
           if(c == NewsActivity.class){
               ((NewsActivity)(getLocalActivityManager().getCurrentActivity())).loadLatest();
           }
    	}else{
    	    Toast toast = Toast.makeText(this, "missing: " + view.getId(), Toast.LENGTH_SHORT);
    	    toast.show();
    	}    	
    }
}
