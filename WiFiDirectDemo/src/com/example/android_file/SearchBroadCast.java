package com.example.android_file;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//接收器
public class SearchBroadCast extends BroadcastReceiver{

	//存储关键字和路径
	public static String mServiceKeyword="";
	public static String mServiceSearchPath="";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自动生成的方法存根
		//取得intent
		String mAction=intent.getAction();
		if(FileMainActivity.KEYGUARD_SERVICE.equals(mAction)){
			//取得intent传递过来的信息
			mServiceKeyword=intent.getStringExtra("keyword");
			mServiceSearchPath=intent.getStringExtra("searchpath");			
		}
	}
	
	
	

}
