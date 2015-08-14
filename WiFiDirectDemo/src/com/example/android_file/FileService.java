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
		// TODO �Զ����ɵķ������		
		Log.d("FileService","return null;");
		return null;
	}
	
	//��������
	@Override
	public void onCreate(){
		super.onCreate();
		Log.d("FileService","File service is onCreate");
		//�½������߳�
		HandlerThread mHT=new HandlerThread("FileService",HandlerThread.NORM_PRIORITY);
		mHT.start();
		mLooper=mHT.getLooper();
		mFileHander=new FileHandler(mLooper);
	}
	//����ʼ
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
			//ָ����Χ������
			initFileArray(new File(SearchBroadCast.mServiceSearchPath));
			//�����ȡ���򲻷��͹㲥
			if(!FileMainActivity.isComeBackFromNotification==true){
				Intent intent=new Intent(FILE_SEARCH_COMPLETED);
				intent.putStringArrayListExtra("mFileNameList", mFileName);
				intent.putStringArrayListExtra("mFilePathList", mFilePaths);
				//�������Я�����ݲ����͹㲥
				sendBroadcast(intent);
			}
		}		
	}
	
	private int m=-1;
	/*�����¼��Ŀɻص�����*/
	private void initFileArray(File file){
		Log.d("FileService","CurentArray  is "+file.getPath());
		if(file.canRead()){
			File[] mFileArray=file.listFiles();
			for(File currentArray:mFileArray){
				if(currentArray.getName().indexOf(SearchBroadCast.mServiceKeyword)!=-1){
					if(m==-1){
						m++;
						//��������֮ǰ��Ŀ¼
						mFileName.add("BacktoSearchBefore");
						mFilePaths.add(FileMainActivity.mCurrentFilePath);
					}						
					mFileName.add(currentArray.getName());
					mFilePaths.add(currentArray.getPath());										
				}
				if(currentArray.exists()&&currentArray.isDirectory()){
					//����û�ȡ������Ӧ��ֹͣ����
					if(FileMainActivity.isComeBackFromNotification==true){
						return;
					}
					initFileArray(currentArray);					
				}
			}
		}		
	}
	
	NotificationManager mNF;
	
	//֪ͨ
	private void fileSearchNotification(){
		//notificationһ�����ڵ绰�����ţ��ʼ����������������ֻ���״̬���Ͼͻ����һ��Сͼ�꣬��ʾ�û��������֪ͨ
		//API16���ϵĴ�������
		
		Intent intent=new Intent(FILE_NOTIFICATION);
		//��noticeʱ����ʾ����
		intent.putExtra("notification", "��ʾ�Ƿ�ȡ������");
		PendingIntent mPI=PendingIntent.getBroadcast(this, 0, intent, 0);		
		
		 Notification noti=new Notification.Builder(this)
		 .setContentIntent(mPI)
		 .setTicker("��������")
         .setContentTitle("�� " + SearchBroadCast.mServiceSearchPath+"������")
         .setContentText("�����ؼ���Ϊ"+SearchBroadCast.mServiceKeyword+"�������ȡ��")
         .setSmallIcon(R.drawable.ic_launcher)         
         .build();
		 
		 if(mNF==null){
			 mNF=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		 }
		 mNF.notify(R.string.app_name,noti);		
	}
		
}
