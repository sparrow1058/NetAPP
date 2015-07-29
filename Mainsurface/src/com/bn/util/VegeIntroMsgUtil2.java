package com.bn.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bn.constant.Constant;
/*
 * VegeIntroMsgUtil对于菜品id，菜品名称，价格，介绍，单位，营养，材料，子路径信息的缓存，
 * 当需要某个菜品信息时，首先在内存中查找，如果有直接使用，没有则从服务端下载
 */
public class VegeIntroMsgUtil2
{
	//存菜品的id和菜品的信息
    static Map<String,String[]> vgData=new HashMap<String,String[]>();
    public static List<String[]> getVege(String vegeid) throws Exception
    {
    	System.out.println("vegeid="+vegeid);
    	String[] vegeinfo=new String[8]; 
    	List<String[]> list = new ArrayList<String[]>();
    	Object vegeIdSet[] = vgData.keySet().toArray();
    	for(Object obj : vegeIdSet)
    	{
    		System.out.println("objjj="+obj.toString());
    	}
    	//判断传过来的id是否为null,防止出现空指针异常
    	if(vegeid==null)
    	{
    		return null;
    	}
    	for(int i=0; i<vegeIdSet.length; i++)
    	{
			if(vegeIdSet[i].toString().startsWith(vegeid))
			{
				list.add(vgData.get(vegeIdSet[i]));
			}
    	}
    	if(list!=null && list.size()!=0)
    	{
    		return list;
    	}
    	//第二步，sqlite中查找
    	list=DBUtil.getVegeintroByid2(vegeid);
    	if(list!=null && list.size()!=0)
    	{
   		 for(String[] arr : list)
   		 {
   			 for( String str : arr)
   			 {
   				 System.out.print(str+" ");
   			 }
   			 System.out.println("\n VegeIntroMsgUtil2---\n");
   		 }
   		 for(String[] vegeInfo : list)
   		 {
     		vgData.put(vegeInfo[0],vegeInfo);   
   		 }
    		return list;
    	}
    	System.out.println("Server-------------------------");
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
		if(list!=null)
		{
			list.clear();
		}
		list.add(vegeinfo);
		vgData.put(vegeid,vegeinfo);  	
		return list;
    }
}
