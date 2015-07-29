package com.bn.util;

import java.util.HashMap;
import java.util.Map;

import com.bn.constant.Constant;

public class VegeChildCateUtil
{
	static Map<String,String[]> vCCData=new HashMap<String,String[]>();
	public static String[] getChildCateInfo(String mainCateName) throws Exception
	{
		String [] childCateInfo;
		if(vCCData.containsKey(mainCateName))
		{//查看内存中是否存在以该主类名称为键的值
			childCateInfo=vCCData.get(mainCateName);
			return childCateInfo;
		}
		//查看SD卡
		childCateInfo=DBUtil.getChildCateByName(mainCateName);
		if(childCateInfo.length!=0)
		{//SD卡中存在
			vCCData.put(mainCateName, childCateInfo);
			return childCateInfo;
		}
		//SD卡中不存在，连接服务器获取数据
		DataGetUtilSimple.ConnectSevert(Constant.GET_CGBYMCG+mainCateName);
		String str=DataGetUtilSimple.readinfo;
		childCateInfo=TypeExchangeUtil.stringtoStringArray(str);
		//存入本地SD卡中
		DBUtil.insertChildCate(mainCateName, childCateInfo);
		//存入内存中
		vCCData.put(mainCateName, childCateInfo);
		return childCateInfo;
	}
}
