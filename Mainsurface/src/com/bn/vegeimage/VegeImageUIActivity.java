package com.bn.vegeimage;

import static com.bn.vegeimage.VegeImageConstant.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bn.R;

import com.bn.error.ResetErrorActivity;
import com.bn.main.MainActivity;
import com.bn.selectvege.SelectVegeActivity;
import com.bn.util.DataUtil;
import com.bn.vegeinfo.VegeActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
public class VegeImageUIActivity extends Activity 
{
	//存放菜品类别信息的Map
	public static Map<String,Object> childVegeInfo;
	//菜品图片
	static Bitmap[] vegeimage;
	//菜品信息
	List<String[]> vegeinfo=new ArrayList<String[]>();
	float startTouchX;//记录点下X的位置
    boolean moveFlag;//标示是否移动
    float thold=20;//移动阈值
	public static int indexno;
	public static boolean onclickflag=true;
	Bundle b;
	public static int i;
	String selectedVegeName;	
	Handler handler=new Handler()
   	{
		@Override
   		public void handleMessage(Message msg) 
   		{
   			super.handleMessage(msg);
   			switch(msg.what)
   			{
   		    //跳转到菜品介绍
		     case GOTO_VEGE_INTRO_MESSAGE:
		    	 Intent intent=new Intent(VegeImageUIActivity.this,VegeActivity.class);
                 startActivity(intent);
                 overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
			 break;
		     case OPEN_ERROR_DIALOG_MESSAGE:
		    	//获取消息中的数据
				  b=msg.getData();
				  ResetErrorActivity.errorMsg=b.getString("msg");
				  ResetErrorActivity.errorFlg="VegeImageActivityFlg";
			      intent=new Intent(VegeImageUIActivity.this,ResetErrorActivity.class);
				  startActivity(intent);
				  VegeImageUIActivity.this.finish();
				  overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
		     break;
		     case OPEN_ERROR_TOAST_MESSAGE:
		    	  b=msg.getData();
				  String strmsg=b.getString("msg");
				  Toast.makeText(VegeImageUIActivity.this, strmsg, Toast.LENGTH_SHORT).show();
		     break;
   			}
   		}
   	};
	@SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		i++;
		MainActivity.al.add(this);
		super.onCreate(savedInstanceState);
		//强制横屏，无标题，全屏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getWindow().getDecorView().setSystemUiVisibility(4);
        setContentView(R.layout.vegebigimage);
        selectedVegeName=this.getIntent().getStringExtra("selectedVegeName");
        //获取信息
    	if(childVegeInfo!=null)
		{
			//获取菜品图片
    		vegeinfo=((List<String[]>)childVegeInfo.get("vegeInfolist"));
			//获取菜品数据
    		vegeimage=(Bitmap[])childVegeInfo.get("bmArr");
    		if(selectedVegeName!=null)
    		{
    			for(int i=0; i<vegeinfo.size(); i++)
    			{
    				if(selectedVegeName.equals(vegeinfo.get(i)[1]))
    				{
    					indexno=i;
    					break;
    				}
    			}
    		}
		}
		final Gallery gallery=(Gallery)this.findViewById(R.id.Gallery01);
		
    	final BaseAdapter ba=new BaseAdapter()
    	{
    		LayoutInflater inflater=LayoutInflater.from(VegeImageUIActivity.this);
			@Override
			public int getCount()
			{
				return vegeinfo.size();
			}			
			@Override
			public Object getItem(int position) 
			{
				return null;
			}			
			@Override
			public long getItemId(int position) 
			{
				return 0;
			}
			@Override
			public View getView(int position, View convertView, ViewGroup parent) 
			{
				View view;
				 if (convertView != null)
				 {     
				        view = convertView;          
				 }
				 else 
				 {      
					 view =(View)inflater.inflate(R.layout.vegeline, null);        
				 }     
				LinearLayout ll=(LinearLayout)view.findViewById(R.id.vegeline);
                ImageView iv=(ImageView)ll.findViewById(R.id.viiv);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setLayoutParams(new LayoutParams(1024,700));
				iv.setImageBitmap(vegeimage[position]);
                
                TextView tv=(TextView)view.findViewById(R.id.vivegename);
                tv.setText(vegeinfo.get(position)[1]+"     "+"￥"+vegeinfo.get(position)[2]+"/"+vegeinfo.get(position)[3]);
         	    Button bintro=(Button)view.findViewById(R.id.vibintro);
         		bintro.setBackgroundDrawable(VegeImageUIActivity.this.getResources().getDrawable(R.drawable.intro));
        		//设置介绍按钮
        		bintro.setOnClickListener(
        		  new OnClickListener()
        		  {
        			@Override
        			public void onClick(View arg0)
        			{
        				if(onclickflag)
        				{
        				  onclickflag=false;
        			      DataUtil.getVegeIntro(vegeinfo.get(indexno)[0], handler);
        				}
                      }		
        		  }
        		);
        		Button bexit=(Button)view.findViewById(R.id.viexit);
        		bexit.setBackgroundDrawable(VegeImageUIActivity.this.getResources().getDrawable(R.drawable.exit));
        		//设置返回按钮
        		bexit.setOnClickListener(
        		  new OnClickListener()
        		  {
        			@Override
        			public void onClick(View arg0) 
        			{
        				
        				SelectVegeActivity.onclickflag=true;
        				Intent intent=new Intent(VegeImageUIActivity.this,SelectVegeActivity.class);
        				startActivity(intent);
        				VegeImageUIActivity.this.finish();
        			}		
        		  }
        		);	
        		return view;
			}
    	};
        gallery.setAdapter(ba);
        //跳到某一种图片
        gallery.setSelection(indexno);
	}
}
