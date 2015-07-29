package com.bn.worker;

import static com.bn.constant.Constant.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.bn.util.SocketClient;
import com.bn.util.TypeExchangeUtil;
public class WorkerInfoTransform 
{
	static String roleArr[][];   //存放角色表中的所有记录
	static String departmentArr[][];  //存放department表中的所有记录
	static String groupcorpArr[][];  //存放groupcorp表中的所有记录
	static List<String[]> list;
	public static Vector<Vector<String>> Transform(String workerInfo)
	{
		
		list = TypeExchangeUtil.strToList(workerInfo);
		for(String[] infoArr : list)
		{
			for(int j=0; j<infoArr.length; j++)
			{
				if(infoArr[j].trim().equals("null")|| infoArr[j]==null)
				{
					infoArr[j]="";
				}
			}
		}
		
		//角色
		SocketClient.ConnectSevert(GET_ROLEINFO);
		String roleInfo=SocketClient.readinfo;
		roleArr = TypeExchangeUtil.getString(roleInfo);		
		//性别
		String gender[] = {"男","女"};
		for(String[] str: list)
		{
			str[3] = (str[3].equals("0"))?gender[0]:gender[1];
			str[4] = getRoleIdToName(str[4]);
		}
		return TypeExchangeUtil.listToVector(list);
	}
	public static List<String[][]> getFKName()
	{
		List<String[][]> list = new ArrayList<String[][]>();
		//角色
		 SocketClient.ConnectSevert(GET_ROLEINFO);
		 String roleInfo=SocketClient.readinfo;
		roleArr = TypeExchangeUtil.getString(roleInfo);
		list.add(roleArr);
		return list;
	}
	//将角色id 转换为角色名称
	public static String getRoleIdToName(String role_id)
	{
		if(role_id.length()!=0)
		{
			for(String arr[] : roleArr)
			{
				if(role_id.equals(arr[0]))
				{
					return arr[1];
				}
			}
		}
		return "";
	}
}