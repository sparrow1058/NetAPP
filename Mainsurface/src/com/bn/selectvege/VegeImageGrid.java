package com.bn.selectvege;

import java.util.List;
import com.bn.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
/*VegeImageGrid自定义控件，每个菜品的图片和信息被作为一个单元格横向逐行显示
可以设定每行显示的单元格的最大数量,和每个单元格之间的间距
要设置的成员变量：菜品图片的Bitmap数组、菜品信息的String数组、菜品信息的文字颜色
图片的高度、宽度、文字大小、单元格间距、每行单元格的数量*/
public class VegeImageGrid extends View
{
	private Paint paint;
	//列数
    int colNum;
    //单位宽度
    float unitWidth;
    //图片高度
    float imageHeight;
    //标示的图片高度
    float picFlgHeight;
    //标示的图片宽度
    float picFlgWidth;
    //文字大小
    int fontSize;
    //字体背景颜色
    String fontColor;
    //间距
    float span;
    //图片
    Bitmap imageBM[];
    //图片信息
    List<String[]> imageDataList;
    //移动偏离
    float startY=0;
    //计算整个控件的高度
    float totalHeight;
    //当前所能显示的控件的高度
    float layoutHeight;
    VegeImageGridListener vegeImgGridListener;
    Bitmap flgBM[]=new Bitmap[2];
    //菜品已选标志位数组
    boolean []booFlg;
    
	public VegeImageGrid(Context context, AttributeSet attrs) 
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
		flgBM[1]=BitmapFactory.decodeResource(getResources(), R.drawable.checkbox_false_press);
		flgBM[0]=BitmapFactory.decodeResource(getResources(), R.drawable.checkbox_false);
	}
	public void addVegeImageGridListener(VegeImageGridListener vegeImgGridListener)
	{
		this.vegeImgGridListener=vegeImgGridListener;
	}
	//设置图片和图片信息
	public void setBMSer(Bitmap[] imageBM,boolean []booFlg)
	{
		this.imageBM=imageBM;
		this.booFlg=booFlg;
	}
	public void setTextSer(List<String[]> imageDataList,String fontColor)
	{
		this.imageDataList=imageDataList;
		this.fontColor=fontColor;
	}
	
	//设置尺寸相关参数
	public void setSizeSer(float imageHeight,float unitWidth,
							float picFlgHeight,float picFlgWidth,
							int fontSize,float span,int colNum)
	{
		this.imageHeight=imageHeight;
		this.unitWidth=unitWidth;
		this.picFlgHeight=picFlgHeight;
		this.picFlgWidth=picFlgWidth;
		this.fontSize=fontSize;
		this.span=span;
		this.colNum=colNum;
	}
	//计算控件的总高度
	public void calTotalHeight()
	{
		int linecount = (this.imageBM.length%colNum==0)?(this.imageBM.length/colNum):(this.imageBM.length/colNum+1);
		totalHeight=linecount*(imageHeight+fontSize+span);
	}
	
	//重写onDraw方法
	@Override
	public void onDraw(Canvas canvas)
	{
	 try
	 {
		if(imageBM.length==0 || imageDataList.size()==0)
		{
			return;
		}
		if(this.totalHeight<this.layoutHeight)
		{
			startY=0;
		}
		for(int i=0; i<imageBM.length; i++)
		{  //绘制图片
			if(booFlg[i])
			{
				canvas.drawBitmap
				(
					small(imageBM[i]), 
					(i%colNum)*(unitWidth+span)+(unitWidth+span)*0.05f, 
					((i/colNum)*(imageHeight+fontSize+span)+(imageHeight+fontSize+span)*0.05f)+startY, 
					paint
				);
				canvas.drawBitmap
				(
					flgBM[1], 
					(i%colNum)*(unitWidth+span), 
					(i/colNum)*(imageHeight+fontSize+span)+startY, 
					paint
					);
			}
			else
			{
				canvas.drawBitmap
				(
					imageBM[i], 
					(i%colNum)*(unitWidth+span), 
					(i/colNum)*(imageHeight+fontSize+span)+startY, 
					paint
				);
				canvas.drawBitmap
				(
						flgBM[0], 
						(i%colNum)*(unitWidth+span), 
						(i/colNum)*(imageHeight+fontSize+span)+startY, 
						paint
						);
			}
		}		
		for(int i=0; i<imageDataList.size(); i++)
		{  //绘制图片信息文字
			//设置字体大小
			paint.setTextSize(fontSize);
			//绘制文字
			canvas.drawText
			(
				imageDataList.get(i)[1]+"   ￥"+imageDataList.get(i)[2]+"/"+imageDataList.get(i)[3],
				(i%colNum)*(unitWidth+span), 
				imageHeight+fontSize+(i/colNum)*(imageHeight+fontSize+span)+startY, 
				paint
			);
		}	
		SelectVegeActivity.onchildclickflag=true;
	}
	catch(NullPointerException e)
	{
			return;
	}
	 finally{
		 SelectVegeActivity.onchildclickflag=true;
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
					this.invalidate();
				}
			return true;	
			case MotionEvent.ACTION_UP:
				//当不是移动事件时执行的操作
				if(!moveFlag)
				{//计算出所点的位置
					float unitH=this.imageHeight+this.fontSize+this.span;
					float unitW=this.unitWidth+this.span;
					int col=(int) ((e.getX())/unitW);
					int row=(int) ((e.getY()-startY)/unitH);
					double xTouch=((e.getX())%unitW);
					double yTouch=((e.getY()-startY)%unitH);
					int index=row*this.colNum+col;
					if(index>=0&&index<imageBM.length)
					{
						if(xTouch<60&&yTouch<60)
						{
							vegeImgGridListener.onItemClick(index,1);
						}
						else
						{
							vegeImgGridListener.onItemClick(index,0);
						}
					}
				}
			return true;	
		}
		return false;
	}
	 private static Bitmap small(Bitmap bitmap)
	 {
		  Matrix matrix = new Matrix(); 
		  matrix.postScale(0.9f,0.9f); //长和宽放大缩小的比例
		  Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		  return resizeBmp;
		 }
}
