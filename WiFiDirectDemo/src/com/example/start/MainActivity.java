package com.example.start;

import com.example.android.wifidirect.R;
import com.example.android.wifidirect.WiFiDirectActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MainActivity extends Activity {
	private final int SPLASH_DISPLAY_LENGHT = 3000; // 延迟3秒  
	 private SharedPreferences preferences;  
	  private Editor editor;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_activity_main);
		 preferences = getSharedPreferences("phone", Context.MODE_PRIVATE); 
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if (preferences.getBoolean("firststart", true)) {
					editor = preferences.edit();  
				    //将登录标志位设置为false，下次登录时不在显示首次登录界面  
				    editor.putBoolean("firststart", false);  
				    editor.commit(); 
				    Intent intent=new Intent();
					intent.setClass(MainActivity.this,AndyViewPagerActivity.class);
					MainActivity.this.startActivity(intent);
					MainActivity.this.finish();
				}else{
					 Intent intent=new Intent();
						intent.setClass(MainActivity.this,WiFiDirectActivity.class);
						MainActivity.this.startActivity(intent);
						MainActivity.this.finish();
					
				}
				
			}
		},SPLASH_DISPLAY_LENGHT);
	}


}
