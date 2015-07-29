package com.bn.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bn.constant.Constant;
/*
 * VegeIntroMsgUtil对于菜品id，菜品名称，价格，介绍，单位，营养，材料，子路径信息的缓存，
 * 当需要某个菜品信息时，首先在内存中查找，如果有直接使用，没有则从服务端下载
 */
public class VegeIntroMsgUtil
{
	//存菜品的id和菜品的信息
    static Map<String,String[]> vgData=new HashMap<String,String[]>();
    public static String[] getVege(String vegeid) throws Exception
    {
    	String[] vegeinfo=new String[8]; 
    	if(vgData.containsKey(vegeid))
    	{
    		vegeinfo=vgData.get(vegeid);
    		return vegeinfo;
    	}   	
    	//第二步，sqlite中查找
    	vegeinfo=DBUtil.getVegeintroByid(vegeid);
    	if(vegeinfo[0]!=null)
    	{
			vgData.put(vegeid,vegeinfo); 
    		return vegeinfo;
    	}
		DataGetUtilSimple.ConnectSevert(Constant.SEARCH_W_VGBYID+vegeid);
		String backinfo=DataGetUtilSimple.readinfo;
		String[] vegeintromsg=TypeExchangeUtil.stringtoStringArray(backinfo);
		vegeintromsg=PicUtil.saveImagemsg(vegeintromsg);
		//存入库
	    List<String[]> vegemsg=TypeExchangeUtil.stringArrayToList(vegeintromsg);
		DBUtil.insertAll("vege", vegemsg);
		//存入sqlite
		vegeinfo[0]=vegeintromsg[0];
		vegeinfo[1]=vegeintromsg[1];
		vegeinfo[2]=vegeintromsg[2];
		vegeinfo[3]=vegeintromsg[8];
		vegeinfo[4]=vegeintromsg[3];
		vegeinfo[7]=vegeintromsg[23];
		vgData.put(vegeid,vegeinfo);  	
		return vegeinfo;
    }
}
