package com.bn.manageorder;

import com.bn.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
public class ExceptionHandleDialog extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//Òþ²Østatus Bar
	    getWindow().getDecorView().setSystemUiVisibility(4);
		setContentView(R.layout.excephandle_om);
		this.setFinishOnTouchOutside(false);
		String excepHint=this.getIntent().getStringExtra("excepHint");
		TextView tv_hint=(TextView)findViewById(R.id.ehom_tv_hint);
		tv_hint.setText(excepHint);
		//Îª°´Å¥×¢²á¼àÌý
		Button but_submit = (Button)findViewById(R.id.ehom_but_submit);
		Button but_close = (Button)findViewById(R.id.ehom_but_close);
		but_submit.setOnClickListener( new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent=getIntent();
				intent.putExtra("excepHandleFlag", "YES");
				ExceptionHandleDialog.this.setResult(4,intent);
				ExceptionHandleDialog.this.finish();
			}			
		});
		but_close.setOnClickListener( new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent=getIntent();
				intent.putExtra("excepHandleFlag", "NO");
				ExceptionHandleDialog.this.setResult(4,intent);
				ExceptionHandleDialog.this.finish();
			}			
		});
	}
}
