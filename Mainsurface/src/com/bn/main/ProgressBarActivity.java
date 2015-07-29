package com.bn.main;

import static com.bn.main.MainConstant.*;
import com.bn.R;
import com.bn.constant.Constant;
import com.bn.selectvege.SelectVegeActivity;
import com.bn.util.DataUtil;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
/* 加载数据时显示的进度条*/
public class ProgressBarActivity extends Activity 
{
    public static String waitMsg;
    public static String errorMsg;
    public static int excepFlg;  
	ProgressDialog waitDialog;
	Dialog errorDialog;
    String resource;//记录Intent的来源
    String action;//记录是显示画廊的进度条还是初始九宫格的进度条
	Handler handler=new Handler()
   	{
		Bundle b;
		@Override
   		public void handleMessage(Message msg) 
   		{
   			super.handleMessage(msg);
   			Intent intent=null;
   			switch(msg.what)
   			{
			     //取消画廊等待对话框
//				 case CANCEL_WAIT_DIALOG_MESSAGE:
//					 intent=getIntent();
//					 setResult(1,intent);
//					 ProgressBarActivity.this.finish();
//				 break;
				 case OPEN_GRIDERROR_DIALOG_MESSAGE:
				    //获取消息中的数据
					  b=msg.getData();
					 //获取内容字符串
					 errorMsg=b.getString("msg");
					 ProgressBarActivity.this.finish();
                     intent=new Intent(ProgressBarActivity.this,ExceptionDialog.class);
                     intent.putExtra("errormsg",errorMsg);
                     intent.putExtra("error","vegegrid");
                     intent.putExtra("errortitle", "错误信息提示：");
                     startActivity(intent); 
				 break;
			     //取消九宫格等待对话框
				 case CANCEL_WAIT_DIALOG_FOR_GRID:
					 if(resource.equals("main"))
					 {
						 intent=getIntent();
						 setResult(2,intent);
						 ProgressBarActivity.this.finish();
					 }
					 else if(resource.equals("login"))
					 {
						Intent i=new Intent(ProgressBarActivity.this,SelectVegeActivity.class);
						startActivity(i);
						ProgressBarActivity.this.finish();
					 }
				 break;
				 //直接显示Toast提示错误
				 case OPEN_ERROR_TOAST_MESSAGE:
					 b = msg.getData();
					 String hint=b.getString("msg");
					 Toast.makeText(ProgressBarActivity.this,hint,Toast.LENGTH_LONG).show();
					 ProgressBarActivity.this.finish();
			     break;
			   //打开画廊错误对话框
//				 case OPEN_GALLERYERROR_DIALOG_MESSAGE:
//					 //获取消息中的数据
//					  b=msg.getData();
//					 //获取内容字符串
//					 errorMsg=b.getString("msg");
//					 //标识是什么错误
//					 excepFlg=b.getInt("excepFlg");
//					 ProgressBarActivity.this.finish();
//                     intent=new Intent(ProgressBarActivity.this,ExceptionDialog.class);
//                     intent.putExtra("errormsg",errorMsg);
//                     intent.putExtra("error","gallery");
//                     intent.putExtra("errortitle", "错误信息提示：");
//                     startActivity(intent);
//				 break;
				 case OPEN_UPLOADERROR_DISLOG_MESSAGE:
					 //获取消息中的数据
					  b=msg.getData();
					 //获取内容字符串
					 errorMsg=b.getString("msg");
					ProgressBarActivity.this.finish();
                    intent=new Intent(ProgressBarActivity.this,ExceptionDialog.class);
                    intent.putExtra("errormsg",errorMsg);
                    intent.putExtra("error","uploaderror");
                    intent.putExtra("errortitle", "错误信息提示：");
                    startActivity(intent);
				 break;
				 case OPEN_UPLOAD_DIALOG_MESSGE:
					errorMsg="加载已完成！！！";
					ProgressBarActivity.this.finish();
                    intent=new Intent(ProgressBarActivity.this,ExceptionDialog.class);
                    intent.putExtra("errormsg",errorMsg);
                    intent.putExtra("error","uploadover");
                    intent.putExtra("errortitle", "提示：");
                    startActivity(intent);
				 break;
   			}
   		}
   	};  
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
		MainActivity.al.add(this);
        super.onCreate(savedInstanceState);  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏status Bar
	    getWindow().getDecorView().setSystemUiVisibility(4);
	    ProgressBarActivity.this.setFinishOnTouchOutside(false);
        setContentView(R.layout.progressbar);
        action=this.getIntent().getStringExtra("Action");
        resource=this.getIntent().getStringExtra("resource");
        new Thread()
        {
        	@Override
        	public void run()
        	{
        		this.setName("ProgressBar Thread");
//        		if(action.equals("INIT_GALLERY"))
//        		{  
//        			DataUtil.initGalleryInfo("今日推荐", handler);
//        		}
//        		else 
        			if(action.equals("INIT_VEGEIMAGEGRID"))
        		{
        			DataUtil.initCateInfoByRoomId(Constant.roomId, handler);
        		}
        		else if(action.equals("UPLOAD_VEGEMSG"))
        		{
        			DataUtil.uploadvege(handler);
        		}
        	}
        }.start();        
	}
}
