package com.bn.error;

import com.bn.R;
import com.bn.login.LoginActivity;
import com.bn.logout.LogoutActivity;
import com.bn.main.MainActivity;
import com.bn.resetlogin.ResetLoginActivity;
//import com.bn.selectvege.MoreFunctionActivity;
import com.bn.selectvege.SelectVegeActivity;
import com.bn.util.DataUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ResetErrorActivity extends Activity 
{
	public static String errorMsg;
	public static String errorFlg;
	   @Override
	  public void onCreate(Bundle savedInstanceState)
	  {
		    MainActivity.al.add(this);
	        super.onCreate(savedInstanceState);
	        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        this.getWindow().getDecorView().setSystemUiVisibility(4);
	        setContentView(R.layout.errordialog);
	        this.setFinishOnTouchOutside(false);
	        //获取控件
	        Button bok=(Button) this.findViewById(R.id.ok);
	        Button breset=(Button)this.findViewById(R.id.reset);
	        TextView tverror=(TextView)this.findViewById(R.id.textviewerror);
	        tverror.setText(errorMsg);
			//按钮的监听
	        bok.setOnClickListener
	        (
	         new OnClickListener()
	           {
				public void onClick(View v) 
				   {
					 if(errorFlg.equals("ResetUIActivityFlg"))
					 {
						 ResetErrorActivity.this.finish(); 
					 }
					 else if(errorFlg.equals("ResetLoginActivityFlg"))
					 {
						 Intent intent=new Intent(ResetErrorActivity.this,ResetLoginActivity.class);

						 startActivity(intent);
						 ResetErrorActivity.this.finish(); 
						 overridePendingTransition(R.anim.out_to_left,R.anim.in_from_right);
					 }
					 else if(errorFlg.equals("CancelLogoutActivityFlg"))
					 {
						SharedPreferences sp=getSharedPreferences("info",Context.MODE_PRIVATE);
						final String userid=sp.getString("user", null);
						SharedPreferences.Editor editor=sp.edit();
			    		editor.remove("user");
						editor.commit();
						new Thread()
						{
							public void run()
							{
								DataUtil.updateLoginFlg(userid);
							}
						}.start();
						if(LogoutActivity.sourceFlg.equals("mainactivity"))
						{
							ResetErrorActivity.this.finish(); 
						}
						else if(LogoutActivity.sourceFlg.equals("exceptionactivity"))
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
						
					 }
					 else if(errorFlg.equals("LogoutActivityFlg"))
					 {
						 ResetErrorActivity.this.finish();
					 }
					 else if(errorFlg.equals("CancleTableActivityFlg"))
					 {
						 ResetErrorActivity.this.finish();
					 }
					 else if(errorFlg.equals("LoginActivityFlg"))
					 {
						 Intent intent=new Intent(ResetErrorActivity.this,LoginActivity.class);
						 startActivity(intent);
						 ResetErrorActivity.this.finish(); 
						 overridePendingTransition(R.anim.out_to_left,R.anim.in_from_right);
					 }
					 else if(errorFlg.equals("SelectVegeActivityFlg"))
					 {
						 Intent intent=new Intent(ResetErrorActivity.this,SelectVegeActivity.class);
						 startActivity(intent);
						 ResetErrorActivity.this.finish(); 
						 overridePendingTransition(R.anim.out_to_left,R.anim.in_from_right);
					 }
					 else if(errorFlg.equals("ShowOrderActivityFlg"))
					 {
						 ResetErrorActivity.this.finish();
					 }
					 else if(errorFlg.equals("VegeImageActivityFlg"))
					 {
						 ResetErrorActivity.this.finish();
					 }
				   }	
		       }        	
	         );
	        //取消按钮监听
	        breset.setOnClickListener
	        (
	          new OnClickListener()
	           {
				 public void onClick(View v)
				 {
					ResetErrorActivity.this.finish();
				 }
	           }
	        );
	  }
}