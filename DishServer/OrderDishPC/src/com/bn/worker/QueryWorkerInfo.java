package com.bn.worker;

import java.util.ArrayList;
import java.util.Map;
import javax.swing.RowFilter;
import javax.swing.table.*;
//根据选择的条件查询对应的员工信息
public class QueryWorkerInfo 
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public QueryWorkerInfo(TableRowSorter sorter,Map map)
	{   //查询员工信息，“与”的关系
		ArrayList andList = new ArrayList();
		RowFilter rfAnd = null;		
		if(map.get("tfWId")!=null)
		{
			RowFilter rfId = RowFilter.regexFilter(((String)map.get("tfWId")).trim(),0);
			andList.add(rfId);
		}	
		if(map.get("tfWName")!=null)
		{
			RowFilter rfName = RowFilter.regexFilter(((String)map.get("tfWName")).trim(),1);
			andList.add(rfName);
		}
		if(map.get("JRole")!=null)
		{
			RowFilter rfRole = RowFilter.regexFilter(((String)map.get("JRole")).trim(),4);
			andList.add(rfRole);
		}
		if(!andList.isEmpty())
		{
			rfAnd = RowFilter.andFilter(andList); 
		}
		sorter.setRowFilter(rfAnd);
	}
}