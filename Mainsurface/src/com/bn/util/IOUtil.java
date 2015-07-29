package com.bn.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
/*
 *IOUtil工具类提供了对DataInputStream进行读取的方法
 */
public class IOUtil 
{
	//读取字符串数据
	public static String readstr(DataInputStream din) throws Exception
	{		
		String str=null;
		byte[]  data=null;
		ByteArrayOutputStream out=null;
	    //循环接受数据
		try
		{
			out=new ByteArrayOutputStream(1024);
			int len=0,temRev=0,size;
			len=din.readInt();
			byte[] buf=new byte[len-temRev];
			while ((size = din.read(buf)) != -1) //当产生EOFException将在此处终止执行
			{ 	
				temRev+=size;
				out.write(buf, 0, size);
				if(temRev>=len)
				{
					break;
				}
				buf=new byte[len-temRev];
			}
			//当获取到数据的长度和数据的真实长度不等时，说明数据没有读全，应该重新获取数据
			data=out.toByteArray();
			//len可能比读到的数据的真实大小要大，如果用len,则可能会造成StringIndexOutOfBoundsException
			str=new String(data,0,temRev,"utf-8");
			str=MyConverter.unescape(str);
		}
		finally
		{
			try{if(out!=null){out.close();} }catch(Exception e){e.printStackTrace();}
		}
		return str;
	}
	//读取图片数据
	public static byte[] readBytes(DataInputStream din) throws Exception
	{
		byte[]  data=null;
		ByteArrayOutputStream out=null;
		try
		{
			out= new ByteArrayOutputStream(1024); 
			int len =0,temRev =0,size;
			len =din.readInt();
			byte[] buf=new byte[len-temRev];
			while ((size = din.read(buf)) != -1)
			{ 	
				temRev+=size;
				out.write(buf, 0, size);
				if(temRev>=len)
				{
					break;
				}
				buf = new byte[len-temRev];
			}
			data = out.toByteArray();
		}
		finally
		{
			try{if(out!=null){out.close();} }catch(Exception e){e.printStackTrace();}
		}
		return data;
	}
}
