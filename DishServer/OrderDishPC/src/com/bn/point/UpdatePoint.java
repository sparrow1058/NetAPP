package com.bn.point;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;

import com.bn.constant.Constant;
import com.bn.pcinfo.MainUI;
import com.bn.util.SocketClient;
import com.bn.util.TypeExchangeUtil;

import static com.bn.constant.Constant.*;
import static com.bn.pcinfo.TableListener.*;
@SuppressWarnings("serial")
/*
 * 更新餐台信息
 * */
public class UpdatePoint extends JFrame implements ActionListener
{
	JLabel jl[]=
	{
			new JLabel("修改餐台"),new JLabel("餐台id"),new JLabel("餐台名称"),
			new JLabel("餐台类型"),new JLabel("是否可用"),
			new JLabel("容纳人数"),new JLabel("关键字"),new JLabel("餐台所属"),		
	};//定义JLabel数组
	JButton jb[]=
	{
			new JButton("修改"),new JButton("取消")
	};//定义JButton数组
	JTextField jtf[]=
	{
			new JTextField(""),new JTextField(""),
			new JTextField(""),new JTextField("")
	};//定义JTextField数组
	JComboBox jcb[]={new JComboBox(),new JComboBox(),new JComboBox()};//定义JComboBox数组
	final StringBuilder sb=new StringBuilder();//定义字符串
	public  DefaultMutableTreeNode curnode;//定义当前节点
	public MainUI mainui;
	//定义各种尺寸
	int width=480;
	int height=400;
	int leftf=width/24;
	int leftm=width/6;
	int leftb=width/4;
	int topf=height/7;
	int topv=height/14;
  public UpdatePoint(DefaultMutableTreeNode curnode,MainUI mainui)//更新餐台方法
  {
	 this.curnode=curnode;
	 this.mainui=mainui;
	 Image image=this.getToolkit().getImage("src/com/bn/image/tb1.jpg");//定义图片
	 this.setIconImage(image);//设置界面图标
	 initUI();//调用initUI方法
  }
  public void initUI()
  {
	    this.setLayout(null);//界面布局器设置为默认
		jl[0].setBounds(width/2-80,2,200,topf+10);//设置jl[0]的位置与大小
		jl[0].setFont(new Font("宋体",Font.BOLD,24));//设置jl[0]的字体
		this.add(jl[0]);//将jl[0]添加到界面中
		for(int i=1;i<jl.length;i++)//设置JLabel的字体、位置与大小，并添加到界面中
		{
			jl[i].setFont(new Font("宋体",Font.BOLD,18));
			if(i%2!=0)
			{
				jl[i].setBounds(leftf, topf+topv*(i-1),leftm,topv);
			}
			else
			{
				jl[i].setBounds(2*leftf+leftm+leftb, topf+topv*(i-2),leftm,topv);
			}
			this.add(jl[i]);
		}
		jtf[0].setBounds(leftf+leftm,topf,leftb,topv);//设置jtf[0]的位置与大小
		jtf[0].setText(values[0]);//设置jtf[0]的值
		jtf[0].setEditable(false);//设置jtf[0]为不可编辑
		jtf[1].setBounds(2*leftf+2*leftm+leftb,topf,leftb,topv);//设置jtf[1]的位置与大小
		jtf[1].setText(values[1]);//设置jtf[1]的值
		jtf[2].setBounds(leftf+leftm,topf+4*topv,leftb,topv);//设置jtf[2]的位置与大小
		jtf[2].setText(values[4]);//设置jtf[2]的值
		jtf[3].setBounds(2*leftf+2*leftm+leftb,topf+4*topv,leftb,topv);//设置jtf[3]的位置与大小
		jtf[3].setText(values[5]);//设置jtf[3]的值
		jcb[0].setBounds(leftf+leftm,topf+2*topv,leftb,topv);//设置jcb[0]的位置与大小
		jcb[1].setBounds(2*leftf+2*leftm+leftb,topf+2*topv,leftb,topv);//设置jcb[1]的位置与大小
		jcb[2].setBounds(leftf+leftm,topf+6*topv,leftb,topv);//设置jcb[2]的位置与大小
	    jb[0].setBounds(leftf+leftm,topf+8*topv,80,30);//设置jb[0]的位置与大小
	    jb[1].setBounds(2*leftf+2*leftm+leftb/2,topf+8*topv,80,30);//设置jb[1]的位置与大小
		for(int i=0;i<jtf.length;i++)//将JTextField添加到界面中，并添加监听
		{
			this.add(jtf[i]);
			jtf[i].addActionListener(this);
		}
		//从网上获取信息
		SocketClient.ConnectSevert(Constant.GET_RMNAME);//从服务器获取餐厅名称
		String allname=SocketClient.readinfo;
		SocketClient.ConnectSevert(Constant.GET_RTNAME);//从服务器获取餐台类别名称
		String alltname=SocketClient.readinfo;
		String str[]=TypeExchangeUtil.getStringInfo(allname,0);
		String strt[]=TypeExchangeUtil.getStringInfo(alltname, 0);
		for(String s:strt)//将餐台类别名称添加到jcb[0]的选项中
		{
			jcb[0].addItem(s);
		}
		for(String s:str)//将餐厅名称添加到jcb[2]的选项中
		{
			jcb[2].addItem(s);
		}
		jcb[1].addItem("正常");jcb[1].addItem("停用");//将“正常”、“停用”两个选项添加到jcb[1]中
		for(int i=0;i<jb.length;i++)//将JButton添加到界面中
		{
			this.add(jb[i]);
		}
		for(int i=0;i<jcb.length;i++)//将JComboBox添加到界面中，并添加监听
		{
			this.add(jcb[i]);
			jcb[i].addActionListener(this);
		}
		jcb[0].setSelectedItem(values[2]);//设置JComboBox的默认选项
		jcb[1].setSelectedItem(values[3]);
		jcb[2].setSelectedItem(values[6]);
		jb[0].addActionListener//jb[0]的监听
		(
		   new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					updateInfo();//调用updataInfo方法
				}
	       }
	     );
		jb[1].addActionListener//jb[1]的监听
		(
		   new ActionListener()
		   {
				@Override
				public void actionPerformed(ActionEvent e)
				{
					UpdatePoint.this.dispose();//更新界面关闭
				}
		   }
		 );
		this.setBounds(300, 150,480,400);//设置界面的位置与大小	
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//设置关闭方式
		this.setVisible(true);//设置为可见
		this.setTitle("更新餐台");//设置标题
		Image image=this.getToolkit().getImage("src/com/bn/image/tb1.jpg");//定义图片
		this.setIconImage(image);//设置图标
		jtf[1].requestFocusInWindow();//获得焦点
  }
  @SuppressWarnings("static-access")
  public void updateInfo()//更新信息方法
    {
	   for(int i=0;i<jtf.length;i++)//将JTextField中的信息添加到sb中
	    {
			sb.append(Constant.UPDATE_POINT+jtf[i].getText().toString());
		}
		for(int i=0;i<jcb.length;i++)//将JComboBox的选中的值添加到sb中
		{
			sb.append(Constant.UPDATE_POINT+jcb[i].getSelectedItem().toString());
		}
		SocketClient.ConnectSevert(sb.toString());//将信息传给服务器
		String addbackinfo=SocketClient.readinfo;
		if(addbackinfo.equals("ok"))//如果返回“ok”，出提示信息"更新餐台成功"
		{
			JOptionPane.showMessageDialog(UpdatePoint.this,"更新餐台成功","消息",JOptionPane.INFORMATION_MESSAGE);
			//更新主界面，重新建表
			SocketClient.ConnectSevert(GET_POINT+curnode.toString());
			String getinfo=SocketClient.readinfo;
			Vector<Vector<String>> data=TypeExchangeUtil.strToVector(getinfo);
			mainui.createJTable(data,title,20,MainUI.topheight,MainUI.midwidth,MainUI.buttomheight);
            mainui.createRight(curnode);
		    this.dispose();//界面关闭
		}
		else
		{   //否则出提示信息"更新失败,请检查输入信息"
			JOptionPane.showMessageDialog(UpdatePoint.this,"更新失败,请检查输入信息","消息",JOptionPane.INFORMATION_MESSAGE);
		}
  }
  public static void main(String args[])//主方法
  {
	  try 
	  {//windows风格
		 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	  } 
	  catch (Exception e) 
	  {
	    e.printStackTrace();
	  }
  }
@Override
public void actionPerformed(ActionEvent e)
{
	 if(e.getSource().equals(jtf[1]))
	 {
		 jcb[0].requestFocus();//jcb[0]获得焦点
	 }
	 else if(e.getSource().equals(jcb[0]))
	 {
		 jcb[1].requestFocus();//jcb[1]获得焦点
	 }
	 else if(e.getSource().equals(jcb[1]))
	 {
		 jtf[2].requestFocus();//jtf[2]获得焦点
	 }
	 else if(e.getSource().equals(jtf[2]))
	 {
		 jtf[3].requestFocus();//jtf[3]获得焦点
	 }
	 else if(e.getSource().equals(jtf[3]))
	 {
		 jcb[2].requestFocus();//jcb[2]获得焦点
	 }
	 else if(e.getSource().equals(jcb[2]))
	 {
		 jb[0].requestFocus();//jb[0]获得焦点
	 }
}
}
