package com.bn.vegeinfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VegeImageList  extends View{

    //初始化画笔
	private Paint paint;		
	//图片的宽和高
    float vegeimageWidth;
	float vegeimageHeight;		
	//图片的间距
	float span;	
	//菜品图片信息
	Bitmap vegeImage[];		
    //移动偏离
    float startY=0;
    //计算整个控件的高度
    float totalHeight;
    //当前所能显示的控件的宽度
    float layoutHeight; 
	    
	public VegeImageList (Context context,AttributeSet attrs)
	{
		super(context, attrs);
		int count=attrs.getAttributeCount();
		//得到当前所显示控件的高度
		for(int i=0; i<count; i++){
			if(attrs.getAttributeName(i).equals("layout_height"))
			{				
				layoutHeight=Float.parseFloat(attrs.getAttributeValue(i).replaceAll("dip|px", ""));
				break;
			}	
		}
		paint=new Paint();//创建画笔对象
		paint.setAntiAlias(true);//打开抗锯齿		
	}	
	public void setBMser(Bitmap vegeImage[])
	{
		this.vegeImage=vegeImage;
	}	
	public void setSizeser(float vegeimageWidth,float vegeimageHeight,float span)
	{
		this.vegeimageWidth=vegeimageWidth;
		this.vegeimageHeight=vegeimageHeight;
		this.span=span;
	}	
	public void onDraw(Canvas canvas)//重写onDraw方法
	{
		for(int i=0;i<vegeImage.length;i++)
		{
			canvas.drawBitmap
			(
				vegeImage[i],
				0,
				i*(vegeimageHeight+span)+startY,
				paint
			);
		}
	}	
	float startTouchX;//记录点下X的位置
	float startTouchY;//记录点下的Y位置
	boolean moveFlag;//标示是否移动
	float startYPre;
	static final float thold=20;//移动阈值
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{	
		switch(e.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				startTouchX=e.getX();//记录当前点的位置
				startTouchY=e.getY();
				startYPre=startY;//记录之前的偏移值
				moveFlag=false;
			return true;
			case MotionEvent.ACTION_MOVE: 
				float x=e.getX();
				float y=e.getY();
				float xs=Math.abs(x-startTouchX);
				float ys=Math.abs(y-startTouchY);
				if(xs>thold||ys>thold)  //判断坐标移动的距离是否超过了阈值
				{
					moveFlag=true;
				}
				//如果移动
				if(moveFlag)
				{
					//移动的偏移量		
					startY=startYPre+(y-startTouchY);
					//规定上界限
					if(startY>0)
					{
						startY=0;
					}
					//规定下界限
					if(startY<this.layoutHeight-this.totalHeight)
					{
						startY=this.layoutHeight-this.totalHeight;
					}
					if(this.totalHeight<this.layoutHeight)
					{
						startY=0;
					}
					this.invalidate();
				}
			return true;	
			case MotionEvent.ACTION_UP:
				//当不是移动事件时执行的操作
				if(!moveFlag)
				{//计算出所点的位置
					
				}
			return true;	
		}
		return false;
	}
	public void calTotalHeight() 
	{
		totalHeight=(vegeimageHeight+span)*vegeImage.length;
		System.out.println("totalheight===="+totalHeight);
	}
}
