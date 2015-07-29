package com.bn.main;

import com.bn.R;
import com.bn.logout.LogoutActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
/*当初始化数据或者加载出现错误后显示的Dialog形式的Activity*/
public class ExceptionDialog extends Activity 
{
	//显示错误标题
	 String errorTitle;
	 //显示错误信息
	 String errorMsg;
	 //错误标示
	 String errorFlg;
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
        setContentView(R.layout.gallryerror);
        Button bok=(Button) this.findViewById(R.id.error_submit);
        Button breset=(Button) this.findViewById(R.id.error_close);
        TextView tvMsg=(TextView) this.findViewById(R.id.error_tv);
        TextView tvTitle=(TextView)this.findViewById(R.id.textviewdialogtitle);
        //获取信息
         Intent intent=this.getIntent();
         errorTitle=intent.getStringExtra("errortitle");
         errorMsg=intent.getStringExtra("errormsg");
         errorFlg=intent.getStringExtra("error");
         tvMsg.setText(errorMsg);
         tvTitle.setText(errorTitle);
         //给 确定按钮添加监听
         bok.setOnClickListener
         (
        	new OnClickListener()
        	{
				@Override
				public void onClick(View v) 
				{
					 if(errorFlg.equals("gallery"))
					 {
						ExceptionDialog.this.finish();
					 }
					 else if(errorFlg.equals("vegegrid"))
					 {
						 Intent intent=new Intent(ExceptionDialog.this,ProgressBarActivity.class);
						 intent.putExtra("Action","INIT_VEGEIMAGEGRID");
						 startActivity(intent);
						 ExceptionDialog.this.finish();
					 }
					 else if(errorFlg.equals("uploaderror"))
					 {
						 Intent intent=new Intent(ExceptionDialog.this,ProgressBarActivity.class);
						 intent.putExtra("Action","UPLOAD_VEGEMSG");
						 startActivity(intent);
						 ExceptionDialog.this.finish();
					 }
					 else if(errorFlg.equals("uploadover"))
					 {
						 ExceptionDialog.this.finish();
					 }
					 //如果是注销用户进入LogoutActivity界面
					 else if(errorFlg.equals("logoutCurUser"))
					 {
						 Intent intent=new Intent(ExceptionDialog.this,LogoutActivity.class);
						 intent.putExtra("source","exceptionactivity");
						 startActivity(intent);
						 ExceptionDialog.this.finish();
					 }
				}
            }
        );
         breset.setOnClickListener
         (
        	new OnClickListener()
        	{
				@Override
				public void onClick(View v) 
				{
					//如果点击不取消注销直接退出
					if(errorFlg.equals("logoutCurUser"))
					{
						for(int i=0;i<MainActivity.al.size();i++)
						{
					           if(MainActivity.al.get(i)!=null)
					           {
					        	   MainActivity.al.get(i).finish();
					           }
					    }
						System.exit(0);
					}
					else
					{
						ExceptionDialog.this.finish();
					}
				}
            }
        );       
	}
}
