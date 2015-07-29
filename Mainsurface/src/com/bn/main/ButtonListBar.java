package com.bn.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
/*用来设置一组按钮的控件类，按钮组横向显示需要设置的参数有按钮组背景图片、
 * 按钮组中每个按钮的图片按钮组中按钮的高度、宽度、左侧留白、按钮间距*/
public class ButtonListBar extends View
{
	//画笔
	private Paint paint;
	//背景的Bitmap
	Bitmap bg;
	//按钮图组
	Bitmap[] buttonBMUp;
	Bitmap[] buttonBMDown;
	//高度
	float height;
	//按钮宽度
	float width;
	//左侧留白
	float leftMargin;
	//按钮之间留白
	float span;
	ButtonListBarListener butListBarListener;	
	public void addButtonListBarListener(ButtonListBarListener butListBarListener)
	{
		this.butListBarListener=butListBarListener;
	}	
	//设置绘制所需图片
	public void setBMSer(Bitmap bg,Bitmap[] buttonBMUp,Bitmap[] buttonBMDown)
	{
		this.bg=bg;
		this.buttonBMUp=buttonBMUp;
		this.buttonBMDown=buttonBMDown;
	}	
	//设置尺寸相关参数
	public void setSizeSer(float height,float width,float leftMargin,float span)
	{
		this.height=height;
		this.width=width;
		this.leftMargin=leftMargin;
		this.span=span;
	}	
	public ButtonListBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		paint=new Paint();//创建画笔对象
		paint.setAntiAlias(true);//打开抗锯齿		
	}	
	public void onDraw(Canvas canvas)//重写onDraw方法
	{
		canvas.drawBitmap(bg, 0, 0, paint); //绘制按钮总背景
		for(int i=0; i<buttonBMUp.length; i++)
		{  //绘制按钮
			canvas.drawBitmap
			(
				buttonBMUp[i], 
				this.leftMargin+i*(this.width+this.span), 
				0, 
				paint
			);
		}		
	}	
	float startX;//记录点下X的位置
	float startY;//记录点下的Y位置
	boolean moveFlag;//标示是否移动
	static final float thold=20;//移动阈值
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		switch(e.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				startX=e.getX();
				startY=e.getY();
				moveFlag=false;
			return true;
			case MotionEvent.ACTION_MOVE:
				float x=e.getX();
				float y=e.getY();
				float xs=Math.abs(x-startX);
				float ys=Math.abs(y-startY);
				if(xs>thold||ys>thold)  //判断坐标移动的距离是否超过了阈值
				{
					moveFlag=true;
				}
			return true;	
			case MotionEvent.ACTION_UP:
				//当不是移动事件时执行的操作
				if(!moveFlag)
				{//计算出所点的位置
					int index=(int) ((e.getX()-this.leftMargin)/(this.width+this.span));
					this.butListBarListener.onButtonClick(index);
				}
			return true;	
		}
		return false;
	}
}
