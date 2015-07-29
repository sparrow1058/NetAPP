package com.bn.vegeinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.bn.R;
import com.bn.main.MainActivity;
import com.bn.vegeimage.VegeImageUIActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class VegeActivity extends Activity 
{
	//当前订单所点的菜品的信息
	public static List<String[]> dcvegemsg=new ArrayList<String[]>();
	//存放菜品图片和信息map
	public static Map<String , Object> vegeintromap;
	VegeImageList vil;
	//菜品图片
	Bitmap[] vegeimage;
	//菜品信息
	String[] vegeinfo;
	String source;
	protected void onCreate(Bundle savedInstanceState)
	{
		MainActivity.al.add(this);
		super.onCreate(savedInstanceState);
		//强制横屏，无标题，全屏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getWindow().getDecorView().setSystemUiVisibility(4);
		setContentView(R.layout.vegeimage);
		//获取信息
	    vegeimage=(Bitmap[]) vegeintromap.get("vegebminfo");
	    vegeinfo=(String[]) vegeintromap.get("vegeinfo");
	    //如果是因为点击今日推荐进入的，则可以获取到source.equals("MainActivity")
	    source=getIntent().getStringExtra("source");
	    
	    vil=(VegeImageList) this.findViewById(R.id.viil);
	    //初始化菜品控件
	    initvegeimage();
	    
	    //得到textview的实例
	    TextView tvcl=(TextView)this.findViewById(R.id.ivcl);
		
	    tvcl.setText("介绍："+vegeinfo[3]);
	    tvcl.setTextSize(VegeConstant.FONT_SIZE);
		
		TextView tv=(TextView)this.findViewById(R.id.vivegename);
		//Button bdc=(Button)this.findViewById(R.id.vibdc);
		Button bexit=(Button)this.findViewById(R.id.viexit);
		
		//设置菜品名称和价格
		tv.setText(vegeinfo[1]+"     "+"￥"+vegeinfo[2]+"/"+vegeinfo[4]);
		//tv.setTextSize(25);
		
		//设置返回按钮
		bexit.setOnClickListener(
		  new OnClickListener()
		  {
			public void onClick(View arg0) 
			{
				Intent intent=new Intent();
				if(source!=null)
				{//如果返回主界面（主界面不再出现进度条）
					intent.setClass(VegeActivity.this, MainActivity.class);
					intent.putExtra("selectvege", "selectvege");
				}
				else
				{
					intent.setClass(VegeActivity.this, VegeImageUIActivity.class);
				}
				 VegeImageUIActivity.onclickflag=true;
				startActivity(intent);
				VegeActivity.this.finish();
				overridePendingTransition(R.anim.out_to_left,R.anim.in_from_right);
			}		
		  }
		);
	}
	//初始化菜品图片控件的信息
	void initvegeimage()
	{
        vil.startY=0;
		vil.setBMser(vegeimage);
		vil.setSizeser(VegeConstant.VEGEIMAGE_WIDTH, VegeConstant.VEGEIAMGE_HEIGHT, VegeConstant.VEGE_PIC_SPAN);
		vil.calTotalHeight();
	}
}