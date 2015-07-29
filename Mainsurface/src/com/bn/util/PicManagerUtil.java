package com.bn.util;

import java.util.HashMap;
import java.util.Map;
import static com.bn.constant.Constant.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/*
 * PicManagerUtil用来实现菜品图片的二级缓存，
 * 当需要显示菜品图片时首先在Bitmap内存中查找，如果没有则在内存中查找，
 * 内存中没有才会到服务端下载
 */
public class PicManagerUtil 
{
	 //共存30副图
	 static final int bmCount=30;
	 //在缓存中存图片的信息
     static Bitmap[] bitmapBuffer=new Bitmap[bmCount];
     //为true则当前位置有图
     static boolean[] isFull=new boolean[bmCount];
     //每一副图的最后使用的时间
     static long[] timeStamp=new long[bmCount];
     //图片数据库中图片的编号与数组下标对应map
     static Map<String,Integer> bmMap=new HashMap<String,Integer>();
     //图片数组下标对应map与数据库中图片的编号
     static Map<Integer,String> bmMapInverse=new HashMap<Integer,String>();    
     //存图的id和图的数据
     static Map<String,byte[]> bmData=new HashMap<String,byte[]>();
     //获得图的方法
     public static Bitmap getBM(String bmId) throws Exception
     {    	 
    	 //第一步，先查bitmap有无指定编号的图片，有则返回
    	 if(bmMap.containsKey(bmId))
    	 {
    		 int index=bmMap.get(bmId);
    		 timeStamp[index]=System.nanoTime();
    		 return bitmapBuffer[index];
    	 }  
    	//第二步，查数据缓冲中有无指定编号的图片，有则返回
    	 if(bmData.containsKey(bmId))
    	 {
    		 byte[] picData=bmData.get(bmId);
    		 return fromMem(bmId,picData);
    	 }
    	 ////存sdcard中菜品的编号和路径
         Map<String,String> bmMapId=PicUtil.getSdidpath();
    	 if(bmMapId.containsKey(bmId))
    	 {
    		 String path=bmMapId.get(bmId);
    		 byte[] picData=PicUtil.getPic(path);
    		 bmData.put(bmId, picData);
        	 return fromMem(bmId,picData);
    	 } 
    	 //第三步，如果缓存中和内存中都没有指定编号的图片则从网上下载
    	 //数据缓冲中没有
    	 DataGetUtilSimple.ConnectSevert(GET_W_PICTUREINFO+bmId);
    	 //异常处理
    	 byte[] picData=DataGetUtilSimple.data;
    	 bmData.put(bmId, picData);
    	 return fromMem(bmId,picData);
     }     
     private static Bitmap fromMem(String bmId,byte[] picData)
     {
		 //查找图片数组有无空位
		 int kw=-1;
		 for(int i=0;i<bmCount;i++)
		 {
			 if(isFull[i]==false)
			 {
				 kw=i;
				 break;
			 }
		 }
		 //有空位
		 if(kw!=-1)
		 {
			 //将数据解码为图片
			 bitmapBuffer[kw]=BitmapFactory.decodeByteArray(picData, 0, picData.length);
			 //将该位置置为有图片状态
			 isFull[kw]=true;
			 //更新当前的使用时间
			 timeStamp[kw]=System.nanoTime();
			 //插入缓冲数组中
			 bmMap.put(bmId, kw);
			 bmMapInverse.put(kw, bmId);
			 return bitmapBuffer[kw];
		 }
		 //没有空位
		 else
		 {
			 //找到当然最小的时间
			 long tempT=Long.MAX_VALUE;
			 kw=-1;
			 //得到当前最小的并将其给空位
			 for(int i=0;i<bmCount;i++)
			 {
				 if(timeStamp[i]<tempT)
				 {
					 tempT=timeStamp[i];
					 kw=i;
				 }
			 }
			 //释放原有图片内存
			 bitmapBuffer[kw].recycle();
			//将数据解码为图片
			 bitmapBuffer[kw]=BitmapFactory.decodeByteArray(picData, 0, picData.length);
			 isFull[kw]=true;
			 //更新当前的使用时间
			 timeStamp[kw]=System.nanoTime();
			 //删除缓冲数组中原有的图片信息
			 bmMap.remove(bmMapInverse.get(kw));
			 bmMapInverse.remove(kw);
			 //插入缓冲数组中
			 bmMap.put(bmId, kw);
			 bmMapInverse.put(kw,bmId);
			 return bitmapBuffer[kw];
		 }
     }  
}
