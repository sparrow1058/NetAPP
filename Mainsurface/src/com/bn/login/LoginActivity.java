package com.bn.login;

import static com.bn.login.LoginConstant.*;
import com.bn.R;
import com.bn.error.ResetErrorActivity;
import com.bn.main.MainActivity;
import com.bn.main.ProgressBarActivity;
import com.bn.selectvege.SelectVegeActivity;
import com.bn.util.DataUtil;
import android.app.Activity;
import android.app.Dialog;
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
/* 登陆界面，输入用户名和密码并且需要有登陆权限才可登陆成功，进行其他操作*/
public class LoginActivity extends Activity 
{
	 public  SharedPreferences sp;
	 //显示错误对话框
	 public Dialog errorDialog;
	 public String errorMsg;
	 //记录登录人的id和密码
	 String userid;
	 String userpw;
	 String did;//记录从SharedPreferences获取的记录的内容
	 Bundle b;
	 //需要使用的控件
	 ToggleButton isrem;
	 EditText loginid;
	 EditText loginpw;
	 Handler handler=new Handler()
	 {
		public void handleMessage(Message msg) 
	   	   {
			  switch(msg.what)
			  {
			    //无权登陆信息提示
			    case SHOW_AUTH_TOST_MESSAGE:
			    	  b=msg.getData();
			    	 String showmessage=b.getString("msg");
			    	 Toast.makeText(LoginActivity.this,showmessage,Toast.LENGTH_LONG).show();
			    	 loginid.setText("");
					 loginpw.setText("");
			    break;
			    //当权限出现错误时打开错误对话框
			    case SHOW_AUTH_ERROR_MESSAGE:
			    	//获取消息中的数据
					  b=msg.getData();
					  ResetErrorActivity.errorMsg=b.getString("msg");
					  ResetErrorActivity.errorFlg="LoginActivityFlg";
				      Intent intent=new Intent(LoginActivity.this,ResetErrorActivity.class);
					  startActivity(intent);
					  LoginActivity.this.finish();
					  overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
			    break;
			    case LOGIN_SUCCESS:
			    	 b=msg.getData();
			    	 String showm=b.getString("msg");
			    	//是否记住
				     if(isrem.isChecked())
				       {
					    	//如果获取当前操作员标号为空存入
					    	if(did==null)
					    	{		
					    		SharedPreferences.Editor editor=sp.edit();
								editor.putString("user",userid);
								editor.commit();
					    	}
				    	}
				     //进入初始化九宫格的进度条
					 String action=b.getString("action");
					 intent=new Intent(LoginActivity.this,ProgressBarActivity.class);
					 intent.putExtra("resource", "login");
					 intent.putExtra("Action", action);
					 startActivity(intent);
		   			 LoginActivity.this.finish();
				     Toast.makeText(LoginActivity.this,showm,Toast.LENGTH_LONG).show();
			    break;
			  }
	   		}
	     };
	     @Override
	 	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	    	{
	 		super.onActivityResult(requestCode, resultCode, data);
	 		if(requestCode==2 && resultCode==2)
	 		{
	 			 Intent intent=new Intent(LoginActivity.this,SelectVegeActivity.class);
	 			 startActivity(intent);
	 			 LoginActivity.this.finish();
	 		}
	 	 }
	   @Override
	  public void onCreate(Bundle savedInstanceState)
	  {
		   MainActivity.al.add(this);
	        super.onCreate(savedInstanceState);
	        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        this.getWindow().getDecorView().setSystemUiVisibility(4);
	        setContentView(R.layout.loginui);
	        this.setFinishOnTouchOutside(false);
	        //获取控件
	        Button bok=(Button) this.findViewById(R.id.ok);
	        Button breset=(Button)this.findViewById(R.id.reset);
	        loginid=(EditText)this.findViewById(R.id.loginid);
		    loginpw=(EditText) this.findViewById(R.id.loginpw);
    	    isrem= (ToggleButton) this.findViewById(R.id.login_imagebutton);	
			loginid.setBackgroundResource(R.drawable.editview_bk_image);
			loginpw.setBackgroundResource(R.drawable.editview_bk_image);
			//登陆按钮的监听
	        bok.setOnClickListener
	        (
	         new OnClickListener()
	           {
				 public void onClick(View v) 
				   {
					 //获取输入信息
						 userid=loginid.getText().toString();
						 userpw=loginpw.getText().toString();
						 sp=LoginActivity.this.getSharedPreferences("info",Context.MODE_PRIVATE);
					     did=sp.getString("user", null);
					    	//如果获取当前操作员标号为空存入
					     if(did!=null&&userid.equals(did))
					      {		
								Toast.makeText(LoginActivity.this, did+"为当前使用用户无需登录", Toast.LENGTH_SHORT).show(); 
					    	    LoginActivity.this.finish();
								return;
					      }
					     if(did!=null&&!(userid.equals(did)))
					     {
					    	 Toast.makeText(LoginActivity.this, "当前已经有用户，请先注销", Toast.LENGTH_SHORT).show(); 
					    	 LoginActivity.this.finish();
					    	 return;
					     }
						//判断登录密码和权限
						 if(userid.equals(""))
						  {
							 Toast.makeText(LoginActivity.this, "请输入用户编号!!!", Toast.LENGTH_SHORT).show(); 
						      return;
						  }
						   if(userpw.equals(""))
						  {
							  Toast.makeText(LoginActivity.this, "请输入密码!!!", Toast.LENGTH_SHORT).show(); 
						      return;
						  }
						 //登陆线程
						 validateThread();
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
					LoginActivity.this.finish();
				 }
	           }
	        );
	  }
	 //把需要做的内容都放入一个线程中
	 public void validateThread()
	 {
	   	  //开启线程进行判断
		  new Thread()
		  {
			  @Override
			  public void run()
			   {
				 try 
				 {
					DataUtil.loginValidate(userid, userpw,handler);
				 }
				 catch (Exception e) 
				  {//如果捕获到异常就发送handler
					 Message msg=new Message();
			    	 msg.what=LoginConstant.SHOW_AUTH_TOST_MESSAGE;
			         Bundle b=new Bundle();
			         b.putString("msg","网络未连接，请检查您的网络后重新登陆");
			         msg.setData(b);
			    	 handler.sendMessage(msg);
			    	 return;
				}
			  }
		  }.start();
	   }
}
