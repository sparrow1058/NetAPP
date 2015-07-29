package com.bn.util;

import java.util.ArrayList;
import java.util.List;

/*
 * TypeExchangeUtil是类型转换工具类，完成对引用类型的不同形式的转换，
 * 方便程序的编写
 */
public class TypeExchangeUtil 
{
	public static String[][] getString(String msg)
	{
		String row[]=msg.split("#");
		String col[]=row[0].split("η");
		String info[][]=new String[row.length][col.length];
		for(int i=0;i<row.length;i++)
		{
			col=row[i].split("η");
			for(int j=0;j<col.length;j++)
			{
				info[i][j]=col[j];
			}
		}
		return info;
	}	
	public static List<String[]> strToList(String msg)
	{
		List<String[]> list =new ArrayList<String[]>();
		String []str=msg.split("#");
		for(int i=0;i<str.length;i++)
		{
			if(str[i].length()>0){
				list.add(str[i].split("η"));
			}
		}
		return list;
	}	
	public static String[] strToString(String getinfo) 
	{
		String row[]=getinfo.split("#");
		String col[]=row[0].split("η");
		String info[]=new String[col.length];
		for(int i=0;i<col.length;i++)
		{
			info[i]=col[i];
		}
		return info;
	}
	public static String listToString(List<String[]> list)
	{
		StringBuffer sb=new StringBuffer();
		if(list!=null)
		{
			for(int i=0;i<list.size();i++)
			{
				if(list.get(i)!=null)
				{
					String str[]=list.get(i);
					for(int j=0;j<str.length;j++)
					{
						sb.append(str[j]+"η");
					}
					sb.substring(0,sb.length()-1);
					sb.append("#");
				}
				else
					break;
			}
		}
		return sb.toString();
	}
	public static String[] stringtoStringArray(String getinfo)
	{
		String[] str=getinfo.split("#");
		return str;
	}
	public static List<String[]> stringArrayToList(String[] str)
	{
		List<String[]> list=new ArrayList<String[]>();
		list.add(str);
		return list;
	}
	public static String[] listToStr(List<String> list)
	{
		String []str=new String[list.size()];
		for(int i=0;i<list.size();i++)
		{
			str[i]=list.get(i);
		}
		return str;
	}
}
