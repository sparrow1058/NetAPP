package com.bn.order;

import java.awt.Font;
import java.awt.Image;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import com.bn.util.TypeExchangeUtil;
/*
 * 根据餐桌号查询具体的订单信息
 * */
@SuppressWarnings("serial")
public class GetConfirmOrder extends JFrame 
{
	String order;
	String xxorder;
	JLabel jtitle=new JLabel("订单详情");//定义“订单详情”JLabel
	JLabel jl[]=
	{
			new JLabel("主餐台"),new JLabel("订单"),new JLabel("人数"),
			new JLabel("服务员id"),new JLabel("总价格：")
	};//定义JLabel数组
	JTextField jtf[]=
	{
			new JTextField(""),new JTextField(""),new JTextField(""),
			new JTextField(""),new JTextField("")
	};//定义JTextField数组
	@SuppressWarnings({ "unchecked", "rawtypes" })
	Vector<String> title=new Vector();//标题向量
	{
	    title.add("菜品名称");
	    title.add("餐台名称");
	    title.add("菜品数量");
		title.add("菜品价格");
		title.add("备注");
	}
	public GetConfirmOrder(String order,String xxorder)//订单确认方法
	{
		Image image=this.getToolkit().getImage("src/com/bn/image/tb1.jpg");//定义图片
		this.setIconImage(image);//设置图标
	    this.order=order;
	    this.xxorder=xxorder;
	    if(order.length()==0)//如果订单信息为空，出提示"此餐桌暂无订单"
	    {
	    	JOptionPane.showMessageDialog(GetConfirmOrder.this, "此餐桌暂无订单","提示",JOptionPane.INFORMATION_MESSAGE);		
		}
	    else
	    {//初始化界面
			Vector<Vector<String>> orderinfo=TypeExchangeUtil.strToVector(xxorder);
			DefaultTableModel dtmtable=new DefaultTableModel(orderinfo,title);//建表模型	
			JTable table=new JTable(dtmtable);//建表
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//设置表自动调整尺寸关
			table.setRowHeight(25);//设置行高25
			String str[][]=TypeExchangeUtil.getString(order);//将订单信息存入数组中
			JScrollPane jspt=new JScrollPane(table);//定义表中的分隔条
			jspt.setBounds(20,100,800,380);//定义分隔条的位置与大小
			jtitle.setBounds(400, 5,200,50);//定义标题的位置与大小
			jtitle.setFont(new Font("宋体",Font.BOLD,24));//设置标题的字体
			this.setLayout(null);//设置界面布局器为默认
			System.out.println("sssss"+str[0].length);
			for(int i=0;i<jl.length;i++)//设置JLabel的字体、JTextField的值，并添加到界面中，设置JTextField为不可编辑
			{
				jl[i].setFont(new Font("宋体",Font.BOLD,12));
				jtf[i].setText(str[0][i]);
				this.add(jl[i]);
				this.add(jtf[i]);
				jtf[i].setEditable(false);
			}
			//设置JLabel、JTextField的位置与大小
			jl[0].setBounds(20,60,60,30);jtf[0].setBounds(60,60,60,30);
			jl[1].setBounds(130,60,30,30);jtf[1].setBounds(160,60,80,30);
			jl[2].setBounds(240,60,30,30);jtf[2].setBounds(270,60,60,30);
			jl[3].setBounds(330,60,80,30);jtf[3].setBounds(400,60,150,30);
			jl[4].setBounds(560,60,80,30);jtf[4].setBounds(630,60,80,30);
			this.add(jtitle);//将标题添加到界面中
			this.add(jspt);//将分给条添加到界面中
			this.setVisible(true);//设置界面可用
			this.setTitle("菜品详细信息");//设置界面标题
			this.setBounds(300, 100, 850,550);//设置界面位置与大小
		}
  }
}
