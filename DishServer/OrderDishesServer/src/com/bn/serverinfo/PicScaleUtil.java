package com.bn.serverinfo;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;

public class PicScaleUtil {
	//缩放图片
	public static BufferedImage scaledImg(byte[]pic,int width,int height) throws IOException{	
		ByteArrayInputStream bis = new ByteArrayInputStream(pic);//byte-->图片
		BufferedImage bi = ImageIO.read(bis);//创建图片
		Image img = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);//缩放图片		
		BufferedImage bufImg=new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_RGB); //将图片信息放入图片缓冲
		bufImg.getGraphics().drawImage(img,0,0,null); //绘制图片
		bis.close();//关闭流
		return bufImg;//返回图片
	}	
	//按宽度等比例缩放图片
	public static BufferedImage scaledImgByWid(BufferedImage bi, int width) throws IOException{			
		double primaryWid = 0;
		double primaryHei = 0;
		int height = 0;    //待缩放的高度
		double proportion = 0;
		primaryWid = bi.getWidth();//得到图片宽度
		primaryHei = bi.getHeight();//得到图片高度
		proportion = width/primaryWid;
		System.out.println("proportion="+proportion);//打印单位宽度
		height = (int)(primaryHei*proportion);//定义高度		
		Image img = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);//缩放图片
		BufferedImage bufImg=new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_RGB); //将图片信息放入图片缓冲 
		bufImg.getGraphics().drawImage(img,0,0,null); //绘制图片
		return bufImg;//返回图片
	}	
	//按高度等比例缩放图片
	public static BufferedImage scaledImgByHei(BufferedImage bi, int height) throws IOException{		
		double primaryWid = 0;
		double primaryHei = 0;
		int width = 0;    //待缩放的高度
		double proportion = 0;//声明单位
		primaryWid = bi.getWidth();//得到图片宽度
		primaryHei = bi.getHeight();//得到图片高度
		proportion = height/primaryWid;
		System.out.println("proportion="+proportion);//打印单位宽度
		width = (int)(primaryHei*proportion);//定义高度		
		Image img = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);//缩放图片
		BufferedImage bufImg=new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_RGB); //将图片信息放入图片缓冲
		bufImg.getGraphics().drawImage(img,0,0,null); //绘制图片
		return bufImg;//返回图片
	}	
	    //保存图片
		public static void saveImage(BufferedImage img,String path) throws IOException{		
			File f=new File(path); //通过将给定路径path转换为抽象路径名来创建一个新 File 实例
		    FileImageOutputStream imgout=new FileImageOutputStream(f);//将File实例放入输出流
		    ImageIO.write(img,"JPEG",f); //将实例写入图片流
		    imgout.close();//关闭图片流
		}
		//从磁盘获得图片
		public static byte[]getPic(String path) {		
			byte[] pic =null;//声明图片比特数组
			try {
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));//根据路径创建输入流
				ByteArrayOutputStream out = new ByteArrayOutputStream(1024); //创建一个新的缓冲输出流，指定缓冲区大小为1024Byte
				byte[] temp = new byte[1024]; //创建大小为1024的比特数组
				int size = 0; //定义大小常量
				try {
					while ((size = in.read(temp)) != -1){  //若有内容读出					
						out.write(temp, 0, size);//写入比特数组 
					}
					in.close();//关闭流
				} catch (IOException e) {//捕获流异常
					e.printStackTrace();
				} 
				pic= out.toByteArray();//将图片信息以比特数组形式读出并赋值给图片比特数组
			} catch (Exception e1) {//捕获异常
				e1.printStackTrace();
			}
			return pic;//返回图片比特数组
		}
}