package com.example.android_file;

import java.io.File;
import java.util.ArrayList;
import java.util.Currency;
import java.util.logging.FileHandler;

import com.example.android_file.*;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.example.android.wifidirect.R;
public class FileService extends Service{
	
	private Looper mLooper;
	private FileHandler mFileHander;
	private ArrayList<String> mFileName=null;
	private ArrayList<String> mFilePaths=null;
	public static final String FILE_SEARCH_COMPLETED="com.supermario.file.FILE_SEARCH_COMPLETED";
	public static final String FILE_NOTIFICATION="com.supermario.file.FILE_NOTIFICATION";
	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根		
		Log.d("FileService","return null;");
		return null;
	}
	
	//创建服务
	@Override
	public void onCreate(){
		super.onCreate();
		Log.d("FileService","File service is onCreate");
		//新建处理线程
		HandlerThread mHT=new HandlerThread("FileService",HandlerThread.NORM_PRIORITY);
		mHT.start();
		mLooper=mHT.getLooper();
		mFileHander=new FileHandler(mLooper);
	}
	//服务开始
	@Override
	 public int onStartCommand(Intent intent, int flags, int startId) {                
        Log.d("FileService","File service is onStart");
        mFileName=new ArrayList<String>();
        mFilePaths=new ArrayList<String>();
        mFileHander.sendEmptyMessage(0);
        fileSearchNotification();
		return startId;
    }   
	
	public void onDestroy(){
		super.onDestroy();
		mNF.cancel(R.string.app_name);
	}
	
	class FileHandler extends Handler{
		public FileHandler(Looper looper){
			super(looper);
		}
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			Log.d("FileService","File service is handleMessage");
			//指定范围内搜索
			initFileArray(new File(SearchBroadCast.mServiceSearchPath));
			//当点击取消则不发送广播
			if(!FileMainActivity.isComeBackFromNotification==true){
				Intent intent=new Intent(FILE_SEARCH_COMPLETED);
				intent.putStringArrayListExtra("mFileNameList", mFileName);
				intent.putStringArrayListExtra("mFilePathList", mFilePaths);
				//搜索完后，携带数据并发送广播
				sendBroadcast(intent);
			}
		}		
	}
	
	private int m=-1;
	/*搜索事件的可回调函数*/
	private void initFileArray(File file){
		Log.d("FileService","CurentArray  is "+file.getPath());
		if(file.canRead()){
			File[] mFileArray=file.listFiles();
			for(File currentArray:mFileArray){
				if(currentArray.getName().indexOf(SearchBroadCast.mServiceKeyword)!=-1){
					if(m==-1){
						m++;
						//返回搜索之前的目录
						mFileName.add("BacktoSearchBefore");
						mFilePaths.add(FileMainActivity.mCurrentFilePath);
					}						
					mFileName.add(currentArray.getName());
					mFilePaths.add(currentArray.getPath());										
				}
				if(currentArray.exists()&&currentArray.isDirectory()){
					//如果用户取消搜索应该停止搜索
					if(FileMainActivity.isComeBackFromNotification==true){
						return;
					}
					initFileArray(currentArray);					
				}
			}
		}		
	}
	
	NotificationManager mNF;
	
	//通知
	private void fileSearchNotification(){
		//notification一般用在电话，短信，邮件，闹钟铃声，在手机的状态栏上就会出现一个小图标，提示用户处理这个通知
		//API16以上的创建方法
		
		Intent intent=new Intent(FILE_NOTIFICATION);
		//打开notice时的提示内容
		intent.putExtra("notification", "提示是否取消搜索");
		PendingIntent mPI=PendingIntent.getBroadcast(this, 0, intent, 0);		
		
		 Notification noti=new Notification.Builder(this)
		 .setContentIntent(mPI)
		 .setTicker("正在搜索")
         .setContentTitle("在 " + SearchBroadCast.mServiceSearchPath+"下搜索")
         .setContentText("搜索关键字为"+SearchBroadCast.mServiceKeyword+"，点击可取消")
         .setSmallIcon(R.drawable.ic_launcher)         
         .build();
		 
		 if(mNF==null){
			 mNF=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		 }
		 mNF.notify(R.string.app_name,noti);		
	}
		
}
