package com.bn.showorder;

import java.util.List;

import com.bn.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ListViewBar extends View
{
	private Paint paint;
    //单位高度
    float unitHeight;
    //文字大小
    int fontSize;
    //字体背景颜色
    int fontColor;
    //图片
    Bitmap imageBM[];
    //图片信息
    List<String[]> imageDataList;
    //计算整个控件的高度
    float totalHeight;
    //当前所能显示的控件的高度
    float layoutHeight;
    //自定义控件监听器
    ListViewBarListener tableListener;
    //下留白
    float span;
    static int index=0;
    float startY=0;
    static int temp=0;
	public ListViewBar(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		int count=attrs.getAttributeCount();
		//得到当前所显示控件的高度
		for(int i=0; i<count; i++)
		{
			if(attrs.getAttributeName(i).equals("layout_height"))
			{				
				layoutHeight=Float.parseFloat(attrs.getAttributeValue(i).replaceAll("dip|px", ""));
				break;
			}	
		}
		paint=new Paint();//创建画笔对象
		paint.setAntiAlias(true);//打开抗锯齿	
		imageBM=new Bitmap[3];
		imageBM[0]=BitmapFactory.decodeResource(getResources(), R.drawable.btn_dec1_normal);
		imageBM[1]=BitmapFactory.decodeResource(getResources(), R.drawable.btn_add1_normal);
		imageBM[2]=BitmapFactory.decodeResource(getResources(), R.drawable.btn_del_normal);
	}
	public void addTableListener(ListViewBarListener tableListener)
	{
		this.tableListener=tableListener;
	}
	public void setTextSer(List<String[]> imageDataList,int fontColor)
	{
		this.imageDataList=imageDataList;
		this.fontColor=fontColor;
	}	
	//设置尺寸相关参数
	public void setSizeSer(float unitHeight,int fontSize,float span)
	{
		this.unitHeight=unitHeight;
		this.fontSize=fontSize;
		this.span=span;
	}
	//计算控件的总高度
	public void calTotalHeight()
	{
		totalHeight = imageDataList.size()*(unitHeight+span);//+unitHeight;
	}	
	//重写onDraw方法
	@Override
	public void onDraw(Canvas canvas)
	{
	 try
	 {
		if(this.totalHeight<this.layoutHeight)
		{
			startY=0;
		}
		else if((-totalHeight-startY+layoutHeight)>41)
		{
			startY=-totalHeight+layoutHeight-41;
		}
		if(index<imageDataList.size())
		{
			temp=index;
		}
		if(temp>=imageDataList.size())
		{
			temp=0;
		}
		paint.setTextSize(fontSize);
		for(int i=0;i<imageDataList.size();i++)
		{
			 if(i==temp)
			 {
				 paint.setColor(Color.RED);
			 }
			 else
			 {
				 paint.setColor(Color.rgb(255, 255, 221));
			 }
			paint.setStyle(Style.FILL);
			canvas.drawRect
			(
				0, 
				i*(unitHeight+span)+startY,
				1024, 
				(i+1)*(unitHeight+span)+startY, 
				paint
			);
			paint.setColor(fontColor);
			canvas.drawText
			(//序号
				i+1+"",
				20,
				(i+1)*unitHeight+i*span+startY,
				paint
			);
			canvas.drawText
			(//菜品名称
				imageDataList.get(i)[0],
				110,
				(i+1)*unitHeight+i*span+startY,
				paint
			);
			canvas.drawText
			(//价格
				imageDataList.get(i)[1]+"元",
				230,
				(i+1)*unitHeight+i*span+startY,
				paint
			);
			canvas.drawBitmap
			(//减少按钮
				imageBM[0],
				300,
				span+i*(unitHeight+span)+startY, 
				paint
			);
			canvas.drawText
			(//数量
				imageDataList.get(i)[2],
				350,
				(i+1)*unitHeight+i*span+startY,
				paint
			);
			canvas.drawBitmap
			(//增加按钮
				imageBM[1],
				370,
				span+i*(unitHeight+span)+startY, 
				paint
			);
//			canvas.drawBitmap
//			(//输入数量
//				imageBM[1],
//				410,
//				span+i*(unitHeight+span)+startY, 
//				paint
//			);
			canvas.drawText
			(//小计
				Double.parseDouble(imageDataList.get(i)[1])*Double.parseDouble(imageDataList.get(i)[2])+"元",
				470,
				(i+1)*unitHeight+i*span+startY,
				paint
			);
			canvas.drawBitmap
			(//删除按钮
				imageBM[2],
				950,
				span+i*(unitHeight+span)+startY, 
				paint
			);
		}
	}
	catch(NullPointerException e)
	{
			return;
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
				startYPre=startY;
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
					if(startY<this.layoutHeight-this.totalHeight-unitHeight)
					{
						startY=this.layoutHeight-this.totalHeight-unitHeight;
					}
					this.invalidate();
				}
			return true;	
			case MotionEvent.ACTION_UP:
				//当不是移动事件时执行的操作
				if(!moveFlag)
				{//计算出所点的位置
					float unitH=this.unitHeight+this.span;
					int row=(int) ((e.getY()-startY)/unitH);
					index=row;
					if(index<imageDataList.size())
					{
						if(e.getX()>300&&e.getX()<335)
						{//减少
							if(!imageDataList.get(index)[2].equals("0"))
							{
								tableListener.onItemClick(index,-1);
							}
						}
						else
							if(e.getX()>370&&e.getX()<405)
							{//增加
								tableListener.onItemClick(index,1);
							}
							else
								if(e.getX()>950&&e.getX()<990)
								{//删除
									tableListener.onItemClick(index,2);
								}
//								else if(e.getX()>410&&e.getX()<445)
//								{//手动输入数量
//									tableListener.onItemClick(index,3);
//								}
								else
								{
									tableListener.onItemClick(index,0);
								}
						this.invalidate();
					}
				}
			return true;	
		}
		return false;
	}
}
