package com.bn.table;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TableInfoGrid extends View {
	//列数
     public int colNum;
     //桌子图片的宽度
     public float uintWidth;
     //桌子图片的高度
     public float timageHeight;
     //桌子内容
     public String tableText;
     //图片间距
     public float span;
     //字体大小
     public float textSize;
     //字体高度
     public float textHeight;
     //字体颜色
     public String textColor;
     //大厅图片
     public Bitmap dtBM[];
     //包厢图片
     public Bitmap bxBM[];
     //控件的高度
     float layoutHeight;
     //当前所有的餐台的高度
     static float totalHeight;
     //移动偏移量
     float startY=0;
     //左留白
     float leftMargin;
     float topMargin;
     //画笔
     public Paint paint;
     String pointstate="无人";
     //餐台信息
     List<String[]> tableInfo;
     TableInfoGridListener tableinfogridlistener;
	public TableInfoGrid(Context context, AttributeSet attrs) 
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
	//初始化所需要的图片
	public void setTableImageSer(Bitmap dtBM[], Bitmap bxBM[])
	{
		this.dtBM=dtBM;
		this.bxBM=bxBM;
	}
	//布局属性设置
	public void setImageSizeSer(int colNum,float uintWidth,float timageHeight)
	{
		this.colNum=colNum;
		this.timageHeight=timageHeight;
		this.uintWidth=uintWidth;
	}
	public void setLayoutSer(float span,float leftMargin,float topMargin)
	{
		this.span=span;
		this.leftMargin=leftMargin;
		this.topMargin=topMargin;
	}
	public void setTextSer(float textSize,String textColor,float textHeight,List<String[]> tableInfo)
	{
	    this.textSize=textSize;
	    this.textColor=textColor;
	    this.textHeight=textHeight;
	    this.tableInfo=tableInfo;
	}
	//计算所有高端
	public void calTotalHeight()
	{
		int rowNum=((tableInfo.size()%colNum)==0)?(tableInfo.size()/colNum):(tableInfo.size()/colNum+1);
		totalHeight=topMargin+(timageHeight+4*textSize+span)*rowNum;
	}
	//添加监听
	public void addTableInfoGridListener(TableInfoGridListener tableinfogridlistener)
	{
		this.tableinfogridlistener=tableinfogridlistener;
	}
	@Override
	public void onDraw(Canvas canvas)
	{
		if(totalHeight<this.layoutHeight)
		{
			startY=0;
		}
		for(int i=0;i<tableInfo.size();i++)
		{
			if(tableInfo.get(i)[6].equals("大厅"))
			{
				drawInfo(dtBM,canvas,i);
			}
			else
			{
				drawInfo(bxBM,canvas,i);
			}
		  paint.setTextSize(textSize);
		  
		  canvas.drawText
		  (
			 "所在区域:"+tableInfo.get(i)[5],
			 (i%colNum)*(uintWidth+span)+leftMargin,
			 timageHeight+textSize+span+(i/colNum)*(timageHeight+4*textSize+span)+startY,
			 paint
		  );
		  canvas.drawText
		  (
			 "餐台名称:"+tableInfo.get(i)[2],
			 (i%colNum)*(uintWidth+span)+leftMargin,
			 timageHeight+2*textSize+span+(i/colNum)*(timageHeight+4*textSize+span)+startY,
			 paint
		  );
		  canvas.drawText
		  (
			 "当前状态:"+pointstate,
			 (i%colNum)*(uintWidth+span)+leftMargin,
			 timageHeight+3*textSize+span+(i/colNum)*(timageHeight+4*textSize+span)+startY,
			 paint
		  );
		  canvas.drawText
		  (
			 "最大容纳人数:"+tableInfo.get(i)[4],
			 (i%colNum)*(uintWidth+span)+leftMargin,
			 timageHeight+4*textSize+span+(i/colNum)*(timageHeight+4*textSize+span)+startY,
			 paint
		  );
		}
	}
	public void drawInfo(Bitmap bm[],Canvas canvas,int i)
	{
		 if(tableInfo.get(i)[3].equals("0"))
		  {
			  pointstate="无人";
			   canvas.drawBitmap
			   (
				bm[0], 
				(i%colNum)*(uintWidth+span)+leftMargin, 
				(i/colNum)*(timageHeight+4*textSize+span)+startY+topMargin, 
				paint
				);
		  }
		  else if(tableInfo.get(i)[3].equals("1"))
		  {
			  pointstate="有人";
			  canvas.drawBitmap
			   (
				bm[1], 
				(i%colNum)*(uintWidth+span)+leftMargin, 
				(i/colNum)*(timageHeight+4*textSize+span)+startY+topMargin, 
				paint
				);
		  }
		  else if(tableInfo.get(i)[3].equals("2"))
		  {
			  pointstate="已定";
			  canvas.drawBitmap
			   (
				bm[2], 
				(i%colNum)*(uintWidth+span)+leftMargin, 
				(i/colNum)*(timageHeight+4*textSize+span)+startY+topMargin,  
				paint
			   );
		  }
		  else if(tableInfo.get(i)[3].equals("3"))
		  {
			  pointstate="选中";
			  canvas.drawBitmap
			   (
				bm[3], 
				(i%colNum)*(uintWidth+span)+leftMargin, 
				(i/colNum)*(timageHeight+4*textSize+span)+startY+topMargin,  
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
					if(startY<this.layoutHeight-totalHeight)
					{
						startY=this.layoutHeight-totalHeight;
					}
					this.invalidate();
				}
			return true;	
			case MotionEvent.ACTION_UP:
				//当不是移动事件时执行的操作
				if(!moveFlag)
				{//计算出所点的位置
					float unitH=this.timageHeight+4*this.textSize+this.span;
					float unitW=this.uintWidth+this.span;
					int col=(int) ((e.getX())/unitW);
					int row=(int) ((e.getY()-startY)/unitH);
					int index=row*this.colNum+col;
					if(index<tableInfo.size())
					{
						tableinfogridlistener.onItemClick(index);
					}
				}
			return true;	
		}
		return false;
	}
}
