package com.bn.resetlogin;

import com.bn.R;
import com.bn.constant.Constant;
import com.bn.error.ResetErrorActivity;
import com.bn.main.MainActivity;
import com.bn.resetmain.ResetDialogActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetLoginActivity extends Activity 
{
	 //记录登录人的id和密码
	 String userpw;
	 //需要使用的控件
	 EditText loginpw;
	   @Override
	  public void onCreate(Bundle savedInstanceState)
	  {
		   MainActivity.al.add(this);
	        super.onCreate(savedInstanceState);
	        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        this.getWindow().getDecorView().setSystemUiVisibility(4);
	        setContentView(R.layout.administratordialog);
	        this.setFinishOnTouchOutside(false);
	        //获取控件
	        Button bok=(Button) this.findViewById(R.id.ok);
	        Button breset=(Button)this.findViewById(R.id.reset);
		    loginpw=(EditText) this.findViewById(R.id.loginpw);
			loginpw.setBackgroundResource(R.drawable.editview_bk_image);
			//登陆按钮的监听
	        bok.setOnClickListener
	        (
	         new OnClickListener()
	           {
				 public void onClick(View v) 
				   {
					 //获取输入信息
						 userpw=loginpw.getText().toString();
						//判断登录密码和权限
						  if(userpw.equals(""))
						  {
							  Toast.makeText(ResetLoginActivity.this, "请输入密码!!!", Toast.LENGTH_SHORT).show(); 
						  }
						  else
						  {
							  if(userpw.equals(Constant.RESETPASSWORD))
							  {
								  Intent intent=new Intent(ResetLoginActivity.this,ResetDialogActivity.class);
								  startActivity(intent);
								  ResetLoginActivity.this.finish();
								  overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
							  }
							  else
							  {
								  ResetErrorActivity.errorMsg="密码输入错误，请您重新输入！！！";
								  ResetErrorActivity.errorFlg="ResetLoginActivityFlg";
								  Intent intent=new Intent(ResetLoginActivity.this,ResetErrorActivity.class);
								  startActivity(intent);
								  ResetLoginActivity.this.finish();
								  overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
							  }
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
					ResetLoginActivity.this.finish();
				 }
	           }
	        );
	  }
}
