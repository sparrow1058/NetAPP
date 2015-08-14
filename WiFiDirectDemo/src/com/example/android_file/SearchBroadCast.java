package com.example.android_file;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//������
public class SearchBroadCast extends BroadcastReceiver{

	//�洢�ؼ��ֺ�·��
	public static String mServiceKeyword="";
	public static String mServiceSearchPath="";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO �Զ����ɵķ������
		//ȡ��intent
		String mAction=intent.getAction();
		if(FileMainActivity.KEYGUARD_SERVICE.equals(mAction)){
			//ȡ��intent���ݹ�������Ϣ
			mServiceKeyword=intent.getStringExtra("keyword");
			mServiceSearchPath=intent.getStringExtra("searchpath");			
		}
	}
	
	
	

}
