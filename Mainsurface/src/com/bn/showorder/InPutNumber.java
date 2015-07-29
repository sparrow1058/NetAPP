package com.bn.showorder;

import java.util.regex.Pattern;
import com.bn.R;
import com.bn.constant.Constant;
import com.bn.error.ResetErrorActivity;
import com.bn.selectvege.SelectVegeActivity;
import com.bn.util.DataUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.TextView;
import android.widget.Toast;

public class InPutNumber extends Activity
{
	int index=0;
	String vegeSum="";
	TextView tvNum;
	EditText dtSum;
	Button btOK;
	Button btReset;
	SharedPreferences sp=null;
	Editor editor=null;
	Handler handler=new Handler()
   	{
		@Override
   		public void handleMessage(Message msg) 
   		{
   			super.handleMessage(msg);
   			  switch(msg.what)
   			  {
   			  case ShowOrderConstant.OPEN_PROMPT_TOAST_MESSAGE:
   				  Bundle b=new Bundle();
   				  b=msg.getData();
   				  Toast.makeText(InPutNumber.this,b.getString("prompt_toast"), Toast.LENGTH_LONG).show();
   			  break;
   			  //打开错误对话框
   			  case ShowOrderConstant.OPEN_ERROR_DIALOG_MESSAGE:
   				  //获取消息中的数据
   				  b=msg.getData();
   				  //获取内容字符串
	    		 ResetErrorActivity.errorMsg=b.getString("msg");
	 			 ResetErrorActivity.errorFlg="ShowOrderActivityFlg";
	 			 Intent intent=new Intent(InPutNumber.this,ResetErrorActivity.class);
	 			 startActivity(intent);
	 			 overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
   			  break;
   			  //显示toast提示信息
   			  case ShowOrderConstant.OPEN_ERROR_TOAST_MESSAGE:
   				  b = msg.getData();
   				  String hint=b.getString("msg");
   				  Toast.makeText(InPutNumber.this,hint,Toast.LENGTH_LONG).show();
		      break; 
   			  case ShowOrderConstant.REMOVE_GUESTNUM_MESSAGE:
   				 b = msg.getData();
  				 String ordersuccess=b.getString("prompt_toast");
  				 Toast.makeText(InPutNumber.this,ordersuccess,Toast.LENGTH_LONG).show();
  				 editor.remove(Constant.deskName);
  				 editor.commit();
  				 Constant.defaultDeskName=Constant.deskName;
  				 Constant.dcvegemsg.clear();//清除列表中的所有内容
				 Constant.deskId=null;
				 Constant.deskName=null;
				 Constant.guestNum=0;
				 SelectVegeActivity.vegeFlg.clear();
				 InPutNumber.this.finish();
   			  break;
   			  }
   		}
   	};
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().getDecorView().setSystemUiVisibility(4);
        setContentView(R.layout.inputnumber);
        sp=InPutNumber.this.getSharedPreferences("info",Context.MODE_PRIVATE);
	    editor=sp.edit();
        tvNum=(TextView)this.findViewById(R.id.input_tv);
        dtSum=(EditText)findViewById(R.id.input_et01);
        btOK=(Button)findViewById(R.id.input_ok);
        btReset=(Button)findViewById(R.id.input_reset);
        dtSum.setBackgroundResource(R.drawable.editview_bk_image);
        Intent intent=this.getIntent();
        String resource=intent.getStringExtra("resource");
        String inputInfo=intent.getStringExtra("pointinfo");
        tvNum.setText(inputInfo);
//        if(resource.equals("PutNum"))
//        {
//        	index=intent.getIntExtra("index", 0);
//		    btOK.setOnClickListener
//		    (
//		    	new OnClickListener() 
//		    	{
//					@Override
//					public void onClick(View v) 
//					{
//						 Pattern pattern = Pattern.compile("[0-9].?[0-9]*"); 
//					     vegeSum=dtSum.getText().toString();
//					     if(pattern.matcher(vegeSum).matches()&&Double.parseDouble(vegeSum)>0)
//					     {
//					    	 Constant.dcvegemsg.get(index)[2]=vegeSum;
//					    	 finish();					    	 
//					     }
//					     else
//					     {
//					         Toast.makeText(InPutNumber.this, "格式输入错误！！！", Toast.LENGTH_SHORT).show();					
//					     }	
//					}
//				} 
//		    );
//        }else
	    if(resource.equals("PassWord"))
        {
        	btOK.setOnClickListener
		    (
		    	new OnClickListener() 
		    	{
					@Override
					public void onClick(View v) 
					{
					     vegeSum=dtSum.getText().toString();					     
					     if( vegeSum.equals(""))
					     {
					    	  Toast.makeText(InPutNumber.this, "请输入密码", Toast.LENGTH_SHORT).show();
					     }else
					     {
							 String userid=sp.getString("user", null);
							 if(userid!=null)
							 {
					    	   DataUtil.setNewOrder(userid,vegeSum,handler);
							 }
					     }
					}
				}
		    );
        }
	        btReset.setOnClickListener
	        (
	        	new OnClickListener()
	        	{
					@Override
					public void onClick(View v) 
					{
						finish();
					}
				}
	        );     
	}
}
