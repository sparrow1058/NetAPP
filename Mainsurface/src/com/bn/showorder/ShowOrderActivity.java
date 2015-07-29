package com.bn.showorder;

import java.util.List;
import com.bn.R;
import com.bn.constant.Constant;
import com.bn.error.ResetErrorActivity;
import com.bn.main.MainActivity;
import com.bn.selectvege.SelectVegeActivity;
//import com.bn.yq.DCYQActivity;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShowOrderActivity extends Activity
{
	public class initListViewBar {

	}
	static int number=0;
	Dialog errordialog;
    String errorMsg;
    int excepFlg;
	public static List<String[]> orderList=null;
	//自定义控件
	ListViewBar listViewBar;
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
   				  Toast.makeText(ShowOrderActivity.this,b.getString("prompt_toast"), Toast.LENGTH_LONG).show();
   			  break;
   			  //打开错误对话框
   			  case ShowOrderConstant.OPEN_ERROR_DIALOG_MESSAGE:
   				  //获取消息中的数据
   				  b=msg.getData();
   				  //获取内容字符串
	    		 ResetErrorActivity.errorMsg=b.getString("msg");
	 			 ResetErrorActivity.errorFlg="ShowOrderActivityFlg";
	 			 Intent intent=new Intent(ShowOrderActivity.this,ResetErrorActivity.class);
	 			 startActivity(intent);
	 			 overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
   			  break;
   			  //显示toast提示信息
   			  case ShowOrderConstant.OPEN_ERROR_TOAST_MESSAGE:
   				  b = msg.getData();
   				  String hint=b.getString("msg");
   				  Toast.makeText(ShowOrderActivity.this,hint,Toast.LENGTH_LONG).show();
		      break; 
   			  case ShowOrderConstant.REMOVE_GUESTNUM_MESSAGE:
   				 b = msg.getData();
  				 String ordersuccess=b.getString("prompt_toast");
  				 Toast.makeText(ShowOrderActivity.this,ordersuccess,Toast.LENGTH_LONG).show();
  				 editor.remove(Constant.deskName);
  				 editor.commit();
  				 Constant.vegeMap.remove(Constant.deskName);
  				 Constant.defaultDeskName=Constant.deskName;
  				 Constant.dcvegemsg.clear();
				 Constant.deskId=null;
				 Constant.deskName=null;
				 Constant.guestNum=0;
				 SelectVegeActivity.vegeFlg.clear();
   			  break;
   			  }
   		}
   	};
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		MainActivity.al.add(this);
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getWindow().getDecorView().setSystemUiVisibility(4);
	    setContentView(R.layout.showorder);//已选菜品页面
	    sp=ShowOrderActivity.this.getSharedPreferences("info",Context.MODE_PRIVATE);
	    editor=sp.edit();
	    orderList=Constant.dcvegemsg;
	    //返回按钮注册监听
        Button bt_back=(Button)findViewById(R.id.showorder_back);
        bt_back.setOnClickListener
        (
        	  new OnClickListener() 
        	  {
				@Override
				public void onClick(View v) 
				{
					Intent intent =new Intent(ShowOrderActivity.this,SelectVegeActivity.class);
					startActivity(intent);
					ShowOrderActivity.this.finish();
					overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
					initListViewBar();
				}
			}
        );
        //提交订单按钮的注册监听
        Button bt_sub=(Button)findViewById(R.id.showorder_xd);
        bt_sub.setOnClickListener
        (
        		new OnClickListener() 
        		{
					@Override
					public void onClick(View v) 
					{
						if(Constant.deskName==null)
						{
							Toast.makeText(ShowOrderActivity.this,"您未开台，请先选择餐台",Toast.LENGTH_LONG).show();
						}
						else
						{
						    String userid=sp.getString("user", null);
							if(userid!=null)
							{
								Intent intent=new Intent(ShowOrderActivity.this,InPutNumber.class);
								intent.putExtra("resource","PassWord");
								intent.putExtra("pointinfo", "请输入密码:");								
								startActivity(intent);								
							}
						}
					}
        		}
        );
        listViewBar=(ListViewBar)this.findViewById(R.id.listviewbar);
        initListViewBar();
        listViewBar.addTableListener
        (
        		new ListViewBarListener() 
        		{
					@Override
					public void onItemClick(int index, int num)
					{
						number=index;
						if(num<2)
						{//2为删除指令,3为手动输入数量指令
							
							if((Double.parseDouble(orderList.get(index)[2])+num)>0)
							{
								
								orderList.get(index)[2]=Double.parseDouble(orderList.get(index)[2])+num+"";
							}
						}
						else
						{
							if(num==2)
							{
								boolean removeFlg=true;
								for(int i=0;i<orderList.size();i++)
								{
									if((i!=index)&&orderList.get(i)[4].equals(orderList.get(index)[4]))
									{//判断订单List中是否有相同菜品的不同记录；
										//若有，则SelectVegeActivity中对选中的菜品图片的标记不必更改
										//若无，则SelectVegeActivity中对选中的菜品图片的标记要更改
										removeFlg=false;//不可移除已点标记
									}
								}
								if(removeFlg)
								{
									SelectVegeActivity.vegeFlg.get(orderList.get(index)[6])[Integer.parseInt(orderList.get(index)[7])]=false;
								}
								orderList.remove(index);
							}
//							else if(num==3)
//							{
//								Intent intent=new Intent(ShowOrderActivity.this,InPutNumber.class);
//								intent.putExtra("resource","PutNum");
//								intent.putExtra("pointinfo", "请输入数量:");
//								intent.putExtra("index", index);
//								initListViewBar();
////								intent.putExtra("vegeSum",orderList.get(index)[2]);
//								startActivity(intent);
//							}							
						}
						initListViewBar();
					}
				}	
        );
        TextView tv_deskid=(TextView)findViewById(R.id.showorder_deskid);
        tv_deskid.setText(Constant.deskName);
	}
	public void initListViewBar()
	{	 
        double money=0;
        listViewBar.setTextSer(orderList, Color.BLACK);
        listViewBar.setSizeSer(ShowOrderConstant.UNIT_HEIGHT,ShowOrderConstant.FONT_SIZE,ShowOrderConstant.SPAN);
        listViewBar.calTotalHeight();
        for(int i=0;i<orderList.size();i++)
        {
        	money=Double.parseDouble(orderList.get(i)[1])*Double.parseDouble(orderList.get(i)[2])+money;
        }  
        TextView all_money=(TextView)findViewById(R.id.showorder_money);
        TextView desk_name=(TextView)findViewById(R.id.showorder_deskid);
        all_money.setText(money+"");
        desk_name.setText(Constant.deskName);
        listViewBar.invalidate();
	}
}
