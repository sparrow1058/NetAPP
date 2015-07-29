package com.bn.resetmain;
import com.bn.R;
import com.bn.main.MainActivity;
import com.bn.main.ProgressBarActivity;
import com.bn.reset.ResetUIActivity;
import com.bn.resetpw.ResetPassWord;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import static com.bn.resetmain.ResetDialogConstant.*;

/*单击设置按钮后显示的dialog，在dialog中可以选择设置的项目， 并跳转到相应的界面进行相应的操作*/
public class ResetDialogActivity extends Activity
{	
	Handler handler=new Handler()
   	{
		@Override
   		public void handleMessage(Message msg) 
   		{
   			super.handleMessage(msg);
   			Bundle bundle=null;
   			String action=null;
   			Intent intent=null;
   			switch(msg.what)
   			{
   			  //打开加载等待对话框
				 case OPEN_DOWNLOAD_DIALOG_MESSAGE:
					 bundle=msg.getData();
					 action=bundle.getString("action");
					 intent=new Intent(ResetDialogActivity.this,ProgressBarActivity.class);
					 intent.putExtra("Action", action);
		   			 startActivity(intent);
		   			 ResetDialogActivity.this.finish();
			     break;
   			}
   		}
   	};
	 public void onCreate(Bundle savedInstanceState)
	    {
		 MainActivity.al.add(this);
	        super.onCreate(savedInstanceState);
	        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        getWindow().getDecorView().setSystemUiVisibility(4);//隐藏状态栏
	        setContentView(R.layout.resetmain);
	        initDialog();
	    }
	private void initDialog()
    {
		final Bitmap[] resetimage=new Bitmap[2];
		resetimage[0]=BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_resetip);
		//resetimage[1]=BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_resetdown);
		resetimage[1]=BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_resetip);
		ListView resetmainlv=(ListView)this.findViewById(R.id.resetmainlv);
		BaseAdapter adapter=new BaseAdapter()
		{
			@Override
			public int getCount()
			{
				return ResetDialogConstant.RESETMSG.length;
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
				LinearLayout llayout=new LinearLayout(ResetDialogActivity.this);
				llayout.setOrientation(LinearLayout.HORIZONTAL);
				llayout.setPadding(10,2,0,0);//设置四周留白
				llayout.setMinimumHeight(ResetDialogConstant.LAYOUTHEIGHT);
				
				ImageView ivreset=new ImageView(ResetDialogActivity.this);
				ivreset.setImageBitmap(resetimage[position]);
				
				TextView tvresetname=new TextView(ResetDialogActivity.this);
				tvresetname.setText(ResetDialogConstant.RESETMSG[position]);
				tvresetname.setTextColor(R.color.black);
				tvresetname.setPadding(10, 5, 5, 5);
				tvresetname.setTextSize(ResetDialogConstant.FONT_SIZE);
				llayout.addView(ivreset);
				llayout.addView(tvresetname);
				return llayout;
			}
		};
		resetmainlv.setAdapter(adapter);
		
		resetmainlv.setOnItemClickListener(
				new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) 
					{
						switch(arg2)
						{
						  case 0:
							     Intent intent=new Intent(ResetDialogActivity.this,ResetUIActivity.class);
							     startActivity(intent);
							     ResetDialogActivity.this.finish();
							     overridePendingTransition(R.anim.out_to_left,R.anim.in_from_right); 
						  break;
						  case 1:
							  Intent i=new Intent(ResetDialogActivity.this,ResetPassWord.class);
							  startActivity(i);
							  ResetDialogActivity.this.finish();
							  overridePendingTransition(R.anim.out_to_left,R.anim.in_from_right); 
						  break;
						}
					}
				}
			);
	}
}
