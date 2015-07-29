package com.bn.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bn.constant.Constant;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class PicUtil 
{
	//保存图片
		public static void saveImage(byte[] img,String imagename) throws Exception
		{
			if(android.os.Environment.getExternalStorageState().equals( 
					android.os.Environment.MEDIA_MOUNTED))
			{
				  String paths = Environment.getExternalStorageDirectory().toString()+"/OrderDish/pic";
				  File destDir = new File(paths);
				  if (!destDir.exists()) 
				  {
				      destDir.mkdirs();
				  }
				  //至此应该成功创建文件夹
                  File file=new File(destDir,"/"+imagename);
                  if(!file.exists())
                  {
          		    //将byte【】数组存入sd卡中
          		    	 FileOutputStream fous=new FileOutputStream(file);
          		    	 BufferedOutputStream bos=new BufferedOutputStream(fous);
          		    	 Bitmap bm= BitmapFactory.decodeByteArray(img, 0, img.length);// bitmap          
          		         bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);    
          		         bos.flush();    
          		         bos.close();  
                }
			}
			else
			{
				throw new NoSdCardException("未检测到SD卡,请插入！！！");
			}
		}
		//从磁盘获得图片
		public static byte[] getPic(String path) throws Exception 
		{
			byte[] pic =null;
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024); 
			byte[] temp = new byte[1024]; 
			int size = 0; 
			while ((size = in.read(temp)) != -1) 
			{ 
				out.write(temp, 0, size); 
			}
			in.close();
			pic= out.toByteArray();
			return pic;
		}
		//得到菜品路径id和路径
		public static Map<String,String> getSdidpath() throws Exception 
		{
			Map<String,String> imageidpath=new HashMap<String,String>();
			List<String> imagepath=DBUtil.getAllvegepath();
			for(int i=0;i<imagepath.size();i++)
			{
				String[] curpath=imagepath.get(i).split("/");
				String pathid=curpath[curpath.length-1].replace(".jpg", "");
				imageidpath.put(pathid, imagepath.get(i));
			}
			return imageidpath;
		}
		public static String[] saveImagemsg(String[] vegemsg) throws Exception
		{
			 //得到菜品的主图片
			  DataGetUtilSimple.ConnectSevert(Constant.GET_IMAGE+vegemsg[22]);
			  byte[] main_image= DataGetUtilSimple.data;
			  //生成新的图片路径
			  String mainpath=vegemsg[22];
			  String[] imagepath=mainpath.split("/");
			  String imagename=imagepath[imagepath.length-1];
			  //存入sdcard中
			  PicUtil.saveImage(main_image, imagename);
			  //将路径进行修改
			  vegemsg[22]=Environment.getExternalStorageDirectory().toString()+"/OrderDish/pic/"+imagename;
			  
			  //得到菜品的子图片
				  String[] path=vegemsg[23].split(",");
				  StringBuilder sbmsgpath=new StringBuilder();
				  for(int j=0;j<path.length;j++)
				  {
					  DataGetUtilSimple.ConnectSevert(Constant.GET_IMAGE+path[j]);
				      byte[] imagemsg= DataGetUtilSimple.data;
					  //生成新的图片路径
					  imagepath=path[j].split("/");
					  imagename=imagepath[imagepath.length-1];
					  //存入sdcard中
					  PicUtil.saveImage(imagemsg, imagename);
					  sbmsgpath.append(Environment.getExternalStorageDirectory().toString()+"/OrderDish/pic/"+imagename+",");
				   }
				  //将路径进行修改
			  vegemsg[23]=sbmsgpath.toString();
			  return vegemsg;
		}
}
