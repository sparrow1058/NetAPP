package com.bn.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bn.R;
import com.bn.constant.Constant;
import com.bn.error.ResetErrorActivity;
import com.bn.main.MainActivity;
import com.bn.selectvege.SelectVegeActivity;
import com.bn.util.DataUtil;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import static com.bn.table.TableInfoConstant.*;
//开台
public class OpenTableActivity extends Activity 
{
	  EditText tidET;
	  EditText guestNumET;
	  //存放所选择的餐台信息
	  String tablestate;//餐台状态
	  String tableid;//餐台id
	  String tablename;//餐台名称
	  String roomid;//餐厅id
	  int guenum=0;//最大容纳人数
	  int curguestNum;//真实输入人数
	  int ordergnum=0;//从网络中获取人数
	  //对话框
	  Dialog dialog;
	  ProgressDialog waitDialog;
	  String errorMsg;
	  String waitMsg;
	  Bundle b;
	  String resource=null;//用来表示来自哪个Activity
	  SharedPreferences sp=null;
	  Editor editor=null;
	  Handler handler=new Handler()
	    {
			@Override
	    	public void handleMessage(Message msg) 
	    	{
	    		switch(msg.what)
	    		{
	    		//更新餐台状态时候异常处理dialog
	    		case SHOW_UPDATETABLEERROR_DIALOG:
	    			 b=msg.getData();
	    			 ResetErrorActivity.errorMsg=b.getString("msg");
	 				 ResetErrorActivity.errorFlg="CancleTableActivityFlg";
	 				 Intent intent=new Intent(OpenTableActivity.this,ResetErrorActivity.class);
	 				 startActivity(intent);
	 				 OpenTableActivity.this.finish();
	 				 overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
	    		break;
	    		//获取餐台信息错误信息Dialog
	    		case SHOW_TABLEERROE_DIALOG:
	    			b=msg.getData();
	    			ResetErrorActivity.errorMsg=b.getString("msg");
					ResetErrorActivity.errorFlg="CancleTableActivityFlg";
					intent=new Intent(OpenTableActivity.this,ResetErrorActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
	    		break;
	    		//显示Toast信息
	    		case SHOW_TOAST_MESSAGE:
	    			b=msg.getData();
	    			String toastmsg=b.getString("msg");
	    			Toast.makeText(OpenTableActivity.this,toastmsg, Toast.LENGTH_SHORT).show();
	    		break;
	    		//关闭当前Activity
	    		case TableInfoConstant.CLOSE_CUR_ACTIVITY:
					  Constant.guestNum=curguestNum;
					  Constant.deskId=tableid;
					  Constant.deskName=tablename;
					  Constant.roomId="001";
					  if(!sp.contains(tablename))
					  {
						  editor.putInt(tablename, curguestNum);
						  editor.commit();
					  }
	    			
	    			if(SelectTableActivity.recordindex!=-1&&SelectTableActivity.recordindex<SelectTableActivity.initTableInfo.size())
	    			{
	    			   SelectTableActivity.initTableInfo.remove(SelectTableActivity.recordindex);
	    			   SelectTableActivity.recordindex=-1;
	    			}
	    			if(resource.equals("selectVege"))
	    			{
	    				 intent=new Intent(OpenTableActivity.this,SelectVegeActivity.class);
	    				 startActivity(intent);
	    			}
	    			OpenTableActivity.this.finish();
	    			overridePendingTransition(R.anim.out_to_right,R.anim.in_from_left);
	    		break;
	    		case GUEST_NUM_MESSAGE:
	    			 guestNumET.setText(ordergnum+"");
	    		break;
//	    		 //跳转到选择餐台界面
			    case GO_SELECT_TABLE:
	   				 Intent i=new Intent(OpenTableActivity.this,SelectTableActivity.class);
	   				 startActivity(i);
	   				 overridePendingTransition(R.anim.out_to_left,R.anim.in_from_right);
	   		     break;
	    		}
	        }
	    };
	  
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	MainActivity.al.add(this);
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	this.getWindow().getDecorView().setSystemUiVisibility(4);
    	setContentView(R.layout.opentable);
        this.setFinishOnTouchOutside(false);
        
        sp=OpenTableActivity.this.getSharedPreferences("info",Context.MODE_PRIVATE);
        editor=sp.edit();
        
        Button but_submit = (Button)findViewById(R.id.ot_submit);
        Button but_close = (Button)findViewById(R.id.ot_close);
        Button but_select=(Button) this.findViewById(R.id.kt_select);
        guestNumET=(EditText) this.findViewById(R.id.ot_et_peoplenum);//人数
        tidET=(EditText) this.findViewById(R.id.ot_et_tableid);//餐台
   
        guestNumET.setBackgroundResource(R.drawable.editview_bk_image);
    	tidET.setBackgroundResource(R.drawable.editview_bk_image);
    	
        Intent intent=this.getIntent();
        resource=intent.getStringExtra("resource");
        if(resource.equals("selectTable"))
        {	
          getResult(intent);
          tidET.setText(tablename); 
        }
        //选择餐台
        but_select.setOnClickListener
        (
           new OnClickListener()
           {
			@Override
			public void onClick(View v) 
			{
					new Thread()
					{
						public void run()
						{
				            SelectTableActivity.operState=0;
			             	SelectTableActivity.ishasPerson=Constant.NOPERSON;
				            DataUtil.openTableInfo(handler);
						}
					}.start();
			  }
           }
        ); 
        //开台
        but_submit.setOnClickListener
        (
          new OnClickListener()
           {
			   @Override
				public void onClick(View v) 
				{
				   tablename=tidET.getText().toString();
				   curguestNum=(guestNumET.getText().toString()==null)?0:Integer.parseInt(guestNumET.getText().toString());
					//判断登录密码和权限
						if(curguestNum==0||tablename.equals(""))
					     {
					       Toast.makeText(OpenTableActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show(); 
					       return;
					     }
				        if(guenum!=0)
				        {
						    if(curguestNum>guenum)
						    {
						       guestNumET.setText("");
							   Toast.makeText(OpenTableActivity.this,"输入人数过多，请选择其他餐台或者重新输入",Toast.LENGTH_SHORT).show();
						       return;
						    } 
				        }
				        //已经开台并且又开台
					   if(Constant.dcvegemsg.size()!=0&&Constant.dcvegemsg!=null&&Constant.deskName!=null)
					    {
						   if(Constant.allpointvegeinfo.containsKey(Constant.deskName))
						   {//记录当前菜品
							   Constant.allpointvegeinfo.remove(Constant.deskName);
						   }
						   List<String[]> list=new ArrayList<String[]>();
						   Map<String,boolean[]> curVegeFlgMap=new HashMap<String,boolean[]>();
							for(int i=0;i<Constant.dcvegemsg.size();i++)
							{
								list.add(Constant.dcvegemsg.get(i));
							}
							for(int i=0;i<SelectVegeActivity.vegeFlg.size();i++)
							{
								curVegeFlgMap.putAll(SelectVegeActivity.vegeFlg);
							}
						   Constant.allpointvegeinfo.put(Constant.deskName,list); 
						   Constant.vegeMap.put(Constant.deskName, curVegeFlgMap);
						   Constant.dcvegemsg.clear();
						   SelectVegeActivity.vegeFlg.clear();////////////////////////////////////////////////////
					    }
					 //开台	    
					   new Thread()
					   {
						   public void run()
						   {
							   DataUtil.openTableUpdate(tablename,curguestNum+"",handler);
						   }
					   }.start();
				}
           }
        );
//关闭按钮监听
        but_close.setOnClickListener
        (
         new OnClickListener()
          {
			@Override
			public void onClick(View v)
			{	
				OpenTableActivity.this.finish();
			}
          }
       );
    }
    //根据得到的值
    public void getResult(Intent intent)
    {
    	String tinfo[]=intent.getStringArrayExtra("tableinfo");
		if(tinfo!=null)
		{
			tablestate=tinfo[3];
	    	tableid=tinfo[0];
	        tablename=tinfo[2];
	        roomid=tinfo[5];
	        guenum=Integer.parseInt(tinfo[4]);//最大容纳人数
	    	tidET.setText(tablename); 
	    	if(tablestate.equals("1"))
	    	{
	    		if(Constant.deskId!=null&&Constant.deskId.equals(tinfo[0]))//当前餐台
	    		{
	    			guestNumET.setText(Constant.guestNum+"");
	    		}
	    		else if(sp.contains(tablename))//开台但是没有下订单
	    		{
	    			guestNumET.setText(sp.getInt(tablename, 0));
	    		}
	    		else//已经有人，并且下单,从库中获取人数
	    		{
	    		  new Thread()
	    		  {
	    			  public void run()
	    			  {
	    				  ordergnum=DataUtil.guestNumByDeskId(tablename, handler);
	    			  }
	    		  }.start();
	    		}
	    	}
    		else if(tablestate.equals("0"))//无人
    		{
    			 guestNumET.setText("1");
    			 tidET.setText(""); 
    		}  		
    	}
		else
		{	
			guestNumET.setText("");
		}
    }  
}
