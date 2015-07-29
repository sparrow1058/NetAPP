package com.bn.resetpw;
import com.bn.R;
import com.bn.util.DataUtil;

import android.app.Activity;
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

public class ResetPassWord extends Activity
{
	EditText userid;
	EditText oldpw;
	EditText newpw;
	EditText truepw;
	Bundle b;
	Handler handler=new Handler()
	 {
		public void handleMessage(Message msg) 
	   	   {
			  switch(msg.what)
			  {
			    //无权登陆信息提示
			    case ResetPassWordConstant.RESET_PASSWORD_MESSAGE:
			    	 b=msg.getData();
			    	 String showmessage=b.getString("msg");
			    	 Toast.makeText(ResetPassWord.this,showmessage,Toast.LENGTH_LONG).show();
			    break;
			    case ResetPassWordConstant.RESET_PASSWORD_SUCCESS:
			    	 b=msg.getData();
			    	 String message=b.getString("msg");
			    	 Toast.makeText(ResetPassWord.this,message,Toast.LENGTH_LONG).show();
			    	 ResetPassWord.this.finish();
			    break;
			  }
	   		}
	     };
	 public void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        getWindow().getDecorView().setSystemUiVisibility(4);
	        setContentView(R.layout.resetpw);
	        userid=(EditText) this.findViewById(R.id.reset_userid);
	        oldpw=(EditText)this.findViewById(R.id.pre_password);
	        newpw=(EditText)this.findViewById(R.id.newpassword);
	        truepw=(EditText) this.findViewById(R.id.truepassword);
	        userid.setBackgroundResource(R.drawable.editview_bk_image);
	        oldpw.setBackgroundResource(R.drawable.editview_bk_image);
	        newpw.setBackgroundResource(R.drawable.editview_bk_image);
	        truepw.setBackgroundResource(R.drawable.editview_bk_image);
	        Button bok=(Button)this.findViewById(R.id.resetpw_ok);
	        Button breset=(Button)this.findViewById(R.id.resetpw_reset);
	        //确定按钮
	        bok.setOnClickListener
	        (
	        	new OnClickListener()
	        	{
					@Override
					public void onClick(View v) 
					{
						String oldpassword=oldpw.getText().toString();
						String newpassword=newpw.getText().toString();
						String truepassword=truepw.getText().toString();
						String wid=userid.getText().toString();
						if(!truepassword.equals(newpassword))
						{
							 Toast.makeText(ResetPassWord.this,"两次密码输入不同请重新输入",Toast.LENGTH_LONG).show();
							 truepw.setText("");
							 newpw.setText("");
							 return;
						}
						DataUtil.resetPassWord(wid,oldpassword,newpassword,handler);
					}
	            }
	        );
	        //取消按钮
	        breset.setOnClickListener
	        (
		        	new OnClickListener()
		        	{
						@Override
						public void onClick(View v) 
						{
							 ResetPassWord.this.finish();
						}
		            }
		        );
	    }
}
