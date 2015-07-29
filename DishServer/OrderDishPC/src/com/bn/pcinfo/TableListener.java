package com.bn.pcinfo;

import java.util.Vector;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
/*
 * 给JTable 添加监听实现选中一行得到第一列的值作为主键，并记录正行的值，之后可以进行相应的删除和更新操作
 * */
public class TableListener implements ListSelectionListener,TableModelListener
{
	private JTable jtable;
	public static String values[]=null;//定义值为空
	public static int row=0;//定义行为0
	public static  Vector<String> title;//定义标题向量
	static String primarykey;//记录选中行的主键
	static String primaryname;//主键名字
	static String[] vegemsg;//只针对于菜品信息
	public TableListener(JTable jtable)//获得当前要编辑的表
	{
		this.jtable=jtable;
	}
	@Override
	public void valueChanged(ListSelectionEvent e)//改变表中值，调用的方法
	{   //如果此事件是多个调整事件之一
		if(e.getValueIsAdjusting()==true){
	     row= jtable.getSelectedRow();//得到当前选中行
	     //如果选中的信息行不为-1
	     if(row!=-1)
	     {
	       primarykey=(String) jtable.getValueAt(row, 0);//得到第一列的值
	       vegemsg=new String[jtable.getColumnCount()];//定义大小为表的列值的数组
	      for(int i=0;i<vegemsg.length;i++)//获取表中的值，添加到数组中
	        {
				vegemsg[i]=(String) jtable.getValueAt(row, i);
			}
	        values=new String[jtable.getColumnCount()];//记录选中行的所有值
			for(int i=0;i<values.length;i++)
			{
				values[i]=(String) jtable.getValueAt(row, i);
			}
	     }
		}
	}
	@Override
	public void tableChanged(TableModelEvent e) 
	{
		values=new String[jtable.getColumnCount()];//定义数组
		row= jtable.getSelectedRow();//记录当前选中行的行数
		if(row!=-1)
		{
			for(int i=0;i<values.length;i++)//记录选中行值
			{
				values[i]=(String) jtable.getValueAt(row, i);
			}
		}
	}
//	public String getUserId()
//	{
//		row=jtable.getSelectedRow();//记录当前选中行的行数
//		if(row==-1)
//		{
//			return null;
//		}
//		String user_id=(String)jtable.getValueAt(row, 0);//得到第一列的值
//		return user_id;
//	}
}