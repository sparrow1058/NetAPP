package com.example.start;

import java.util.ArrayList;  
import java.util.List;  
  



import com.example.android.wifidirect.R;
import com.example.android.wifidirect.WiFiDirectActivity;

import android.app.Activity;  
import android.content.Intent;
import android.os.Bundle;  
import android.support.v4.view.ViewPager;  
import android.support.v4.view.ViewPager.OnPageChangeListener;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.Button;
import android.widget.ImageView;  
import android.widget.LinearLayout;  
  
public class AndyViewPagerActivity extends Activity implements OnClickListener, OnPageChangeListener{  
      
    private ViewPager vp;  
    private ViewPagerAdapter vpAdapter;  
    private List<View> views;  
    private Button button;
      
    //寮曞鍥剧墖璧勬簮  
    private static final int[] pics = { R.drawable.guide11,  
            R.drawable.guide12, R.drawable.guide14,  
            R.drawable.guide15};  
      
    //搴曢儴灏忓簵鍥剧墖  
    private ImageView[] dots ;  
      
    //璁板綍褰撳墠閫変腑浣嶇疆  
    private int currentIndex;  
      
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.start_main);  
          button=(Button)findViewById(R.id.button);
        views = new ArrayList<View>();  
         
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,  
                LinearLayout.LayoutParams.WRAP_CONTENT);  
          
        //鍒濆鍖栧紩瀵煎浘鐗囧垪琛? 
        for(int i=0; i<pics.length; i++) {  
            ImageView iv = new ImageView(this);  
            iv.setLayoutParams(mParams);  
            iv.setImageResource(pics[i]);  
            views.add(iv);  
        }  
        vp = (ViewPager) findViewById(R.id.viewpager);  
        //鍒濆鍖朅dapter  
        vpAdapter = new ViewPagerAdapter(views);  
        vp.setAdapter(vpAdapter);  
        //缁戝畾鍥炶皟  
        vp.setOnPageChangeListener(this);  
//        button = (Button) findViewById(R.id.button);
        //鍒濆鍖栧簳閮ㄥ皬鐐? 
        initDots();  
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent();
				intent.setClass(AndyViewPagerActivity.this, WiFiDirectActivity.class);
				AndyViewPagerActivity.this.startActivity(intent);
				finish();
			}
		});
          
    }  
      
    private void initDots() {  
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);  
  
        dots = new ImageView[pics.length];  
  
        //寰幆鍙栧緱灏忕偣鍥剧墖  
        for (int i = 0; i < pics.length; i++) {  
            dots[i] = (ImageView) ll.getChildAt(i);  
            dots[i].setEnabled(true);//閮借涓虹伆鑹? 
            dots[i].setOnClickListener(this);  
            dots[i].setTag(i);//璁剧疆浣嶇疆tag锛屾柟渚垮彇鍑轰笌褰撳墠浣嶇疆瀵瑰簲  
        }  
  
        currentIndex = 0;  
        dots[currentIndex].setEnabled(false);//璁剧疆涓虹櫧鑹诧紝鍗抽?涓姸鎬? 
    }  
      
    /** 
     *璁剧疆褰撳墠鐨勫紩瀵奸〉  
     */  
    private void setCurView(int position)  
    {  
        if (position < 0 || position >= pics.length) {  
            return;  
        }  
  
        vp.setCurrentItem(position);  
    }  
  
    /** 
     *杩欏彧褰撳墠寮曞灏忕偣鐨勯?涓? 
     */  
    private void setCurDot(int positon)  
    {  
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {  
            return;  
        }  
  
        dots[positon].setEnabled(false);  
        dots[currentIndex].setEnabled(true);  
  
        currentIndex = positon;  
    }  
  
    //褰撴粦鍔ㄧ姸鎬佹敼鍙樻椂璋冪敤  
    @Override  
    public void onPageScrollStateChanged(int arg0) {  
        // TODO Auto-generated method stub  
          
    }  
  
    //褰撳綋鍓嶉〉闈㈣婊戝姩鏃惰皟鐢? 
    @Override  
    public void onPageScrolled(int arg0, float arg1, int arg2) {  
        // TODO Auto-generated method stub  
          
    }  
  
    //褰撴柊鐨勯〉闈㈣閫変腑鏃惰皟鐢? 
    @Override  
    public void onPageSelected(int arg0) {  
        //璁剧疆搴曢儴灏忕偣閫変腑鐘舵?  
        setCurDot(arg0);  
        if(arg0 == 3){
        	button.setVisibility(View.VISIBLE);
        	
        }else{
        	button.setVisibility(View.GONE);
        }
    }  
  
    @Override  
    public void onClick(View v) {  
        int position = (Integer)v.getTag();  
        setCurView(position);  
        setCurDot(position);  
    } 
    
}