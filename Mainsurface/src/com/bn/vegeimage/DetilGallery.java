package com.bn.vegeimage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class DetilGallery extends Gallery
{

	public DetilGallery(Context context) 
	{
		super(context);
	}

    public DetilGallery(Context context, AttributeSet attrs) {   
        super(context, attrs);   
    }   
 
	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) 
    {   
        return e2.getX() > e1.getX();   
    }   
    private boolean isScrollingRight(MotionEvent e1, MotionEvent e2) 
    {   
        return e2.getX() < e1.getX();   
    }  
    @Override  
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,   
            float velocityY) {   
        int keyCode=0;   
        if (isScrollingLeft(e1, e2)&&VegeImageUIActivity.indexno>0) 
        {   
        	VegeImageUIActivity.indexno=VegeImageUIActivity.indexno-1;
            keyCode = KeyEvent.KEYCODE_DPAD_LEFT;   
            onKeyDown(keyCode, null); 
        }
        else if(isScrollingRight(e1, e2)&&VegeImageUIActivity.indexno<VegeImageUIActivity.vegeimage.length-1)
        {   
        	VegeImageUIActivity.indexno=VegeImageUIActivity.indexno+1;
            keyCode = KeyEvent.KEYCODE_DPAD_RIGHT; 
            onKeyDown(keyCode, null); 
        }   
        return true;   
    }   
}  
