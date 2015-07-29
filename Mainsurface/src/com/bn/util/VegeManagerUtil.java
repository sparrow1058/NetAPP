package com.bn.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bn.constant.Constant;

/*
 * VegeManagerUtil对于菜品数据的缓存，当需要某个菜品信息时，首先
 * 在内存中查找，如果有直接使用，没有则从服务端下载
 */
public class VegeManagerUtil 
{
    //存菜品的id和图的数据
    static Map<String,List<String[]>> vgData=new HashMap<String,List<String[]>>();	    
    public static List<String[]> getVege(String vegecate) throws Exception
    {
    	List<String[]> vegeinfo=new ArrayList<String[]>(); 
    	//内存中没有
    	if(vgData.containsKey(vegecate))
    	{
    		vegeinfo=vgData.get(vegecate);
    		return vegeinfo;
    	}
    	//查看sd卡
    	vegeinfo=DBUtil.getVegeintroByCate(vegecate);
    	
    	if(vegeinfo.size()!=0)
    	{
    		vgData.put(vegecate, vegeinfo);
    		return vegeinfo;
    	}
    	//否则上网查
		DataGetUtilSimple.ConnectSevert(Constant.GET_W_VEGEINFO+vegecate);
		String backinfo=DataGetUtilSimple.readinfo;
		vegeinfo=TypeExchangeUtil.strToList(backinfo);
		List<String[]> vegelist=new ArrayList<String[]>();
		//把图片放到pic中
		for(int i=0;i<vegeinfo.size();i++)
		{
			String[] vegemsg=PicUtil.saveImagemsg(vegeinfo.get(i));
			vegelist.add(vegemsg);
		}
		//把信息放入库中
		DBUtil.insertAll("vege",vegelist);
		//把需要的信息放入内存
		List<String[]> catevege=new ArrayList<String[]>();
		catevege=DBUtil.getVegeintroByCate(vegecate);
		vgData.put(vegecate,catevege);  	   
		return catevege;
    }
}
