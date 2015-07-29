package com.bn.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bn.constant.Constant;

public class VegeMainCateUtil 
{
	static Map<String,List<String[]>> vMCData=new HashMap<String,List<String[]>>();
	public static List<String[]> getMainCateInfo(String roomId) throws Exception
	{
		List<String[]> mainCateInfo=new ArrayList<String[]>();
		if(vMCData.containsKey(roomId))
		{//查看内存中是否存在以该房间id为键的值
			mainCateInfo=vMCData.get(roomId);
			return mainCateInfo;
		}
		//查看SD卡
		mainCateInfo=DBUtil.getVegeMainCateByRoomId(roomId);
		if(mainCateInfo.size()!=0)
		{//SD卡中存在
			vMCData.put(roomId, mainCateInfo);
			return mainCateInfo;
		}
		//SD卡中不存在，连接服务器获取数据
		DataGetUtilSimple.ConnectSevert(Constant.GET_W_MCGINFO+Constant.roomId);
		mainCateInfo=TypeExchangeUtil.strToList(DataGetUtilSimple.readinfo);
		//存入本地SD卡中
		DBUtil.insertMainCate(roomId, mainCateInfo);
		//存入内存
		vMCData.put(roomId, mainCateInfo);
		return mainCateInfo;
	}
}
