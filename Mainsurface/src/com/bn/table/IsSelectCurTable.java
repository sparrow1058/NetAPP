package com.bn.table;

import com.bn.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class IsSelectCurTable extends Activity
{
	public static String selectTableInfo[];
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//强制横屏，无标题，全屏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getWindow().getDecorView().setSystemUiVisibility(4);
		setContentView(R.layout.isselect);
		this.setFinishOnTouchOutside(false);
		TextView etroom=(TextView) this.findViewById(R.id.selectroomid);
		TextView ettname=(TextView) this.findViewById(R.id.selectpointname);
		TextView etstatement=(TextView) this.findViewById(R.id.tablestatement);
		TextView maxnum=(TextView) this.findViewById(R.id.maxnum);
		Button bok=(Button)this.findViewById(R.id.isselect);
		Button breset=(Button)this.findViewById(R.id.notselect);
		if(selectTableInfo!=null)
		{
			etroom.append(selectTableInfo[5]+"/"+selectTableInfo[6]);
			ettname.append(selectTableInfo[2]+"餐台");
			etstatement.append(selectTableInfo[3].equals("0")?"无人":"有人");
			maxnum.append(selectTableInfo[4]+"位");
		}
		bok.setOnClickListener
		(
			new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					//如果是开台
					if(SelectTableActivity.operState==0)
					{
						Intent in=new Intent(IsSelectCurTable.this,OpenTableActivity.class);
						in.putExtra("tableinfo",selectTableInfo);
						startActivity(in);
						IsSelectCurTable.this.finish();
					}
				}
	        }
		);
		breset.setOnClickListener
		(
			new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					IsSelectCurTable.this.finish();
				}	
	        }
		);
	}
}
