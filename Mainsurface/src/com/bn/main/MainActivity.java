package com.bn.main;

import java.util.ArrayList;
import java.util.List;
//import java.util.Map;
//import java.util.Map;
import com.bn.R;
//import com.bn.util.*;
//import com.bn.util.DataUtil;
import com.bn.vegeinfo.VegeActivity;
import com.bn.constant.Constant;
import com.bn.login.LoginActivity;
import com.bn.logout.LogoutActivity;
import com.bn.resetlogin.ResetLoginActivity;
import com.bn.selectvege.SelectVegeActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import static com.bn.constant.Constant.IP;
import static com.bn.constant.Constant.POINT;
import static com.bn.main.MainConstant.*;

/*MainActivity是主界面，通过自定义的画廊控件VegeGalleryList显示“今日推荐”的菜品图片和数据
 在自定义按钮组ButtonListBar中显示了服务员手持端的基本功能和帮助信息的按钮在MainActivity中
 当点击点菜按钮时，为SelectVegeActivity要显示的初始界面初始化数据，在跳转之前显示进度对话框*/
public class MainActivity extends Activity 
{
	public static List<Activity> al=new ArrayList<Activity>();
	//暂存对话框中要显示的信息
    public static String waitMsg;
    public static String errorMsg;
    public static int excepFlg;
    public static boolean isHasSD=true;
	ProgressDialog waitDialog;
	Dialog errorDialog;
	Bundle b;
	SharedPreferences sp;
	Handler handler=new Handler()
   	{
//		@SuppressWarnings("unchecked")
		@Override
   		public void handleMessage(Message msg) 
   		{
   			super.handleMessage(msg);
   			Bundle bundle=null;
   			String action=null;
   			Intent intent=null;
   			switch(msg.what)
   			{
			     //打开菜品九宫格对话框
				 case OPEN_WAIT_DIALOG_FOR_GRID:
					 bundle=msg.getData();
					 action=bundle.getString("action");
					 intent=new Intent(MainActivity.this,ProgressBarActivity.class);
					 intent.putExtra("Action", action);
					 intent.putExtra("resource", "main");
					 startActivityForResult(intent,2);
			     break;

				 case GOTO_VEGE_INTRO_MESSAGE:
					 intent=new Intent(MainActivity.this,VegeActivity.class);
					 intent.putExtra("source", "MainActivity");
	   				 startActivity(intent);
	   				 overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
			     break;
				 //直接显示Toast提示错误
				 case OPEN_ERROR_TOAST_MESSAGE:
					 b = msg.getData();
					 String hint=b.getString("msg");
					 Toast.makeText(MainActivity.this,hint,Toast.LENGTH_LONG).show();
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
			 Intent intent=new Intent(MainActivity.this,SelectVegeActivity.class);
			 startActivity(intent);
			 MainActivity.this.finish();
		}
	 }
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);  
        al.add(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏status Bar
	    getWindow().getDecorView().setSystemUiVisibility(4);
        setContentView(R.layout.main);     
        //从preferences中获得上次得到存入的ip和point
  		SharedPreferences spipandpoint=MainActivity.this.getSharedPreferences("ipandpoint", Context.MODE_PRIVATE);
  		IP=spipandpoint.getString("ip", Constant.ip);
  		POINT=spipandpoint.getInt("point", Constant.point);
        
        //按钮抬起的图片
        Bitmap buttonBMUp[]=new Bitmap[6];
        //按钮按下的图片
        Bitmap buttonBMDown[]=new Bitmap[6];  
        //按钮组的背景图片
        Bitmap bmbg=BitmapFactory.decodeResource(this.getResources(), R.drawable.mbuttonback);
        //加载按钮图片
        buttonBMUp[0]=BitmapFactory.decodeResource(this.getResources(), R.drawable.setting_normal); //设置
        buttonBMUp[1]=BitmapFactory.decodeResource(this.getResources(), R.drawable.order_normal);   //点菜
        buttonBMUp[2]=BitmapFactory.decodeResource(this.getResources(), R.drawable.logout_normal);  //注销
        buttonBMUp[3]=BitmapFactory.decodeResource(this.getResources(), R.drawable.exit_normal);   //退出
        buttonBMUp[4]=BitmapFactory.decodeResource(this.getResources(), R.drawable.help_normal);    //帮助
        buttonBMUp[5]=BitmapFactory.decodeResource(this.getResources(), R.drawable.about_normal);   //关于
        buttonBMDown[0]=BitmapFactory.decodeResource(this.getResources(), R.drawable.setting_pressed); //设置
        buttonBMDown[1]=BitmapFactory.decodeResource(this.getResources(), R.drawable.order_pressed);   //点菜
        buttonBMDown[2]=BitmapFactory.decodeResource(this.getResources(), R.drawable.logout_pressed);  //注销
        buttonBMDown[3]=BitmapFactory.decodeResource(this.getResources(), R.drawable.exit_pressed);   //退出
        buttonBMDown[4]=BitmapFactory.decodeResource(this.getResources(), R.drawable.help_pressed);    //帮助
        buttonBMDown[5]=BitmapFactory.decodeResource(this.getResources(), R.drawable.about_pressed); //关于

        ButtonListBar butListBar=(ButtonListBar)this.findViewById(R.id.bbar);
        //设置按钮的总背景、抬起、按下图片
        butListBar.setBMSer(bmbg, buttonBMUp, buttonBMDown);
        //设置按钮的高、宽、左侧留白、按钮间距
        butListBar.setSizeSer
        (
        	MainConstant.BUTTON_HEIGHT, 
        	MainConstant.BUTTON_WIDTH, 
        	MainConstant.BUTTONBAR_LEFTMARGIN, 
        	MainConstant.BUTTON_SPAN
        );
        //对控件添加监听
        butListBar.addButtonListBarListener
        (
        	new ButtonListBarListener()
        	{
			
				public void onButtonClick(int index) 
				{
					Intent intent;
					switch(index)
					{
					//设置按钮
					  case 0:
						  intent=new Intent(MainActivity.this,ResetLoginActivity.class);
						  startActivity(intent);
						  overridePendingTransition(R.anim.out_to_left,R.anim.in_from_right); 
					  break;
					  //点菜按钮
					  case 1:
						  sp=MainActivity.this.getSharedPreferences("info",Context.MODE_PRIVATE);
					       String did=sp.getString("user", null);
				    	   if(did!=null)
				    	   {
				    		   //通过餐厅ID初始化类别及主类别信息
								  Message msg=new Message();
								  Bundle b=new Bundle();
								  msg.what=MainConstant.OPEN_WAIT_DIALOG_FOR_GRID;
								  b.putString("action","INIT_VEGEIMAGEGRID");
								  msg.setData(b);
								  handler.sendMessage(msg);
				    	   }
				    	  else
						  {
							  //打开登陆界面
							  intent = new Intent(MainActivity.this,LoginActivity.class);
							  startActivity(intent);
							  overridePendingTransition(R.anim.out_to_left,R.anim.in_from_right);
						  }
					  break;
					  case 2:
						  //打开注销界面
						  sp=MainActivity.this.getSharedPreferences("info",Context.MODE_PRIVATE);
						  String isu=sp.getString("user", null);
						  if(isu!=null)
						  {
						  intent = new Intent(MainActivity.this,LogoutActivity.class);
						  intent.putExtra("source","mainactivity");
						  startActivity(intent);
						  overridePendingTransition(R.anim.out_to_left,R.anim.in_from_right);
						  }
						  else
						  {
							  Toast.makeText(MainActivity.this, "当前无用户登录，无需注销！", Toast.LENGTH_SHORT).show();
						  }
					  break;
					  case 3:
						  //如果有用户提示是否注销进入提示对话框
						  sp=MainActivity.this.getSharedPreferences("info",Context.MODE_PRIVATE);
					       String userid=sp.getString("user", null);
				    	   if(userid!=null)
				    	   {
				    		     intent=new Intent(MainActivity.this,ExceptionDialog.class);
			                     intent.putExtra("errormsg","是否注销当前用户");
			                     intent.putExtra("error","logoutCurUser");
			                     intent.putExtra("errortitle", "信息提示：");
			                     startActivity(intent);
				    	   }
				    	   else
				    	   {
				    		   //退出系统
				    		   for(int i=0;i<MainActivity.al.size();i++)
								{
							           if(MainActivity.al.get(i)!=null)
							           {
							        	   MainActivity.al.get(i).finish();
							           }
							    }
								System.exit(0); 
				    	   }
					  break;
					}
				}
        	}
        );
	}
}