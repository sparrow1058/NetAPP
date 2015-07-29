package com.bn.logout;

import com.bn.R;
import com.bn.error.ResetErrorActivity;
import com.bn.main.MainActivity;
import com.bn.util.DataUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import static com.bn.logout.LogoutConstant.*;
/*用户撤销*/
public class LogoutActivity extends Activity 
{
	public SharedPreferences sp=null;
	 //显示错误对话框
	 public String errorMsg;
	 public String pointMsg;
	 //记录登录人的id和密码
	 String userid;
	 String userpw;
	 Bundle b;
	 //需要使用的控件
	 ToggleButton isrem;
	 EditText logoutid;
	 EditText logoutpw;
	 Intent intent;
	 public static String sourceFlg=null;
	 Handler handler=new Handler()
	 {
		public void handleMessage(Message msg) 
		{
		  switch(msg.what)
		  {
		    //无权登陆信息提示
		    case SHOW_AUTH_TOST_MESSAGE:
		    	 Bundle b=msg.getData();
		    	 String showmessage=b.getString("msg");
		    	 Toast.makeText(LogoutActivity.this,showmessage,Toast.LENGTH_LONG).show();
		    	 LogoutActivity.this.finish();
		    break;
		    //打开注销对话框
		    case CANCEL_LOGIN_MESSAGE:
		    	  b=msg.getData();
				  ResetErrorActivity.errorMsg=b.getString("msg");
				  ResetErrorActivity.errorFlg="CancelLogoutActivityFlg";
				  intent=new Intent(LogoutActivity.this,ResetErrorActivity.class);
				  startActivity(intent);
				  LogoutActivity.this.finish();
				  overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
		    break;
		    case SHOW_AUTH_ERROR_MESSAGE:
		    	//获取消息中的数据
		    	  b=msg.getData();
				  ResetErrorActivity.errorMsg=b.getString("msg");
				  ResetErrorActivity.errorFlg="LogoutActivityFlg";
			      Intent intent=new Intent(LogoutActivity.this,ResetErrorActivity.class);
				  startActivity(intent);
				  LogoutActivity.this.finish();
				  overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
		    break;
		  }
		}
	 };	 	
	   @Override
	  public void onCreate(Bundle savedInstanceState) 
	  {
		   MainActivity.al.add(this);
	        super.onCreate(savedInstanceState);
	        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        this.getWindow().getDecorView().setSystemUiVisibility(4);
	        setContentView(R.layout.logout);
	        this.setFinishOnTouchOutside(false);
	        //获取控件
	        Button blogoutok=(Button) this.findViewById(R.id.logoutok);
	        Button blogoutreset=(Button)this.findViewById(R.id.logoutreset);
	        logoutid=(EditText)this.findViewById(R.id.logoutid);
	   	    logoutpw=(EditText) this.findViewById(R.id.logoutpw);
	   	    logoutid.setBackgroundResource(R.drawable.editview_bk_image);
	   	    logoutpw.setBackgroundResource(R.drawable.editview_bk_image);
	   	    //区分从mainActivity 传来还是ExceptionDialog传来
	   	    Intent intent=this.getIntent();
	   	    sourceFlg=intent.getStringExtra("source");
			//注销按钮的监听
	    	 blogoutok.setOnClickListener
	         (
	           new OnClickListener()
	            {
				  public void onClick(View v) 
				   {
					 //获取输入信息
					    userid=logoutid.getText().toString();
						userpw=logoutpw.getText().toString();
						//判断登录密码和权限
						 if(userid.equals(""))
						  {
							 Toast.makeText(LogoutActivity.this, "请输入用户编号!!!", Toast.LENGTH_SHORT).show(); 
						  }
						  else if(userpw.equals(""))
						  {
							  Toast.makeText(LogoutActivity.this, "请输入密码!!!", Toast.LENGTH_SHORT).show(); 
						  }
						  else
						  {
							  validateThread();
						  }
				    }	
		       }        	
	         );
	        //取消按钮监听
	     blogoutreset.setOnClickListener
	        (
	          new OnClickListener()
	           {
				 public void onClick(View v)
				 {
					LogoutActivity.this.finish();
				 }
	           }
	        );
	  	}
	   public void validateThread()
	   {
		   //开启线程进行判断
			  new Thread()
			  {
				  public void run()
				  {
					 this.setName("validateThread---LogoutActivity");
				     boolean isHasLoginAuth;
					try {
						 isHasLoginAuth = DataUtil.logoutValidate(userid, userpw,handler);
					     if(isHasLoginAuth==false)
					     {//如果没有登陆权限发送handler
					    	 Message msg=new Message();
					    	 msg.what=LogoutConstant.SHOW_AUTH_TOST_MESSAGE;
					         Bundle b=new Bundle();
					         b.putString("msg","  对不起您的密码错误  ");
					         msg.setData(b);
					    	 handler.sendMessage(msg);
					     }
					     else
					     {//如果正确发送注销确认对话框
					    	 sp=getSharedPreferences("info",Context.MODE_PRIVATE);
					    	 if(sp.getString("user",null).equals(userid))
					    	 {
					    		 Message msg=new Message();
						    	 msg.what=LogoutConstant.CANCEL_LOGIN_MESSAGE;
						         Bundle b=new Bundle();
						         b.putString("msg","     您确定要注销!!!   ");
						         msg.setData(b);
						    	 handler.sendMessage(msg);
					    	 }
					    	 else
					    	 {
					    		 Message msg=new Message();
						    	 msg.what=LogoutConstant.SHOW_AUTH_TOST_MESSAGE;
						         Bundle b=new Bundle();
						         b.putString("msg","您未登录无需注销！！！");
						         msg.setData(b);
						    	 handler.sendMessage(msg);
					    	 }
					     }
					} 
					catch (Exception e)
					{
						e.printStackTrace();
						 Message msg=new Message();
				    	 msg.what=LogoutConstant.SHOW_AUTH_TOST_MESSAGE;
				         Bundle b=new Bundle();
				         b.putString("msg","网络出问题，请检查是否连接后重试");
				         msg.setData(b);
				    	 handler.sendMessage(msg);
					}
				  }
			  }.start();
	      }
  }

