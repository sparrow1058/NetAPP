package com.bn.point;

import static com.bn.constant.Constant.*;

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
import static com.bn.pcinfo.TableListener.*;
import com.bn.pcinfo.MainUI;
import com.bn.util.SocketClient;
import com.bn.util.TypeExchangeUtil;
/*
 * 添加餐台信息并且保存到库中
 * */
@SuppressWarnings("serial")
public class AddPoint extends JFrame 
{
	JLabel jl[]=
	   {
			new JLabel("添加餐台"),new JLabel("餐台id"),new JLabel("餐台名称"),
			new JLabel("餐台类型"),new JLabel("所属餐厅"),
			new JLabel("容纳人数"),new JLabel("关键字")		
	    };//定义JLabel数组
	JButton jb[]={new JButton("添加"),new JButton("取消")};//定义JButton数组
	JTextField jtf[]=
	{
			new JTextField(""),new JTextField(""),new JTextField(""),
			new JTextField(""),new JTextField("")
	};//定义JTextField数组
	JComboBox jcb[]={new JComboBox(),new JComboBox()};//定义JComboBox数组
	final StringBuilder sb=new StringBuilder();//定义字符串
	public  DefaultMutableTreeNode curnode;//定义当前节点
	public MainUI mainui;
	//定义各种尺寸
	int width=480;
	int height=300;
	int leftf=width/24;
	int leftm=width/6;
	int leftb=width/4;
	int topf=height/5;
	int topv=height/10;
  public AddPoint( DefaultMutableTreeNode curnode,MainUI mainui)
  {
	  this.curnode=curnode;
	  this.mainui=mainui;
	  initUI();//调用initUI方法
  }
  //初始化添加餐台界面
  public void initUI()
  {
		Image image=this.getToolkit().getImage("src/com/bn/image/tb1.jpg");//定义图片
		this.setIconImage(image);//设置界面图标
	    this.setLayout(null);//设置界面布局器为默认
		jl[0].setBounds(width/2-80,2,200,topf+10);//设置jl[0]的位置与大小
		jl[0].setFont(new Font("宋体",Font.BOLD,24));//设置jl[0]字体
		this.add(jl[0]);//将jl[0]添加到界面中
		for(int i=1;i<jl.length;i++)//设置JLabel的字体、位置与大小，并将JLabel添加到界面中
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
		jtf[0].setBounds(leftf+leftm,topf,leftb,topv);//设置JTextField的位置与大小
		//获取餐台最大值（自动显示最大值并且不可改）
		SocketClient.ConnectSevert(Constant.GET_MAXPOINTNO);
		String maxno=SocketClient.readinfo;
		jtf[0].setText(maxno);//设置jtf[0]的值
		jtf[0].setEditable(false);//设置jtf[0]为不可编辑
		jtf[1].setBounds(2*leftf+2*leftm+leftb,topf,leftb,topv);//设置jtf[1]的位置与大小
		jtf[2].setBounds(leftf+leftm,topf+4*topv,leftb,topv);//设置jtf[2]的位置与大小
		jtf[3].setBounds(2*leftf+2*leftm+leftb,topf+4*topv,leftb,topv);//设置jtf[3]的位置与大小
		jcb[0].setBounds(leftf+leftm,topf+2*topv,leftb,topv);//设置jcb[0]的位置与大小
		jcb[1].setBounds(2*leftf+2*leftm+leftb,topf+2*topv,leftb,topv);//设置jcb[1]的位置与大小
	    jb[0].setBounds(leftf+leftm,topf+6*topv,80,30);//设置jb[0]的位置与大小
	    jb[1].setBounds(2*leftf+2*leftm+leftb/2,topf+6*topv,80,30);//设置jb[1]的位置与大小
		for(int i=0;i<jtf.length;i++)//将JTextField添加到界面中
		{
			this.add(jtf[i]);
		}
		//通过服务器获得餐厅名称
		SocketClient.ConnectSevert(Constant.GET_RMNAME);
		String allname=SocketClient.readinfo;
		//通过服务器获得餐台类型名
		SocketClient.ConnectSevert(Constant.GET_RTNAME);
		String alltname=SocketClient.readinfo;
		String str[]=TypeExchangeUtil.getStringInfo(allname, 0);//将餐厅名称放入数组中
		String strt[]=TypeExchangeUtil.getStringInfo(alltname, 0);//将餐台类型名放入数组中
		for(String s:strt)//将餐台类型名添加到jcb[0]的选项中
		{
			jcb[0].addItem(s);
		}
		for(String s:str)//将餐厅名添加到jcb[1]的选项中
		{
			jcb[1].addItem(s);
		}
		jcb[1].setSelectedItem(curnode.toString());//jcb[1]的选中项是当前节点
		for(int i=0;i<2;i++)//将JComboBox、JButton添加到界面中
		{
			this.add(jb[i]);
			this.add(jcb[i]);
		}
		jb[0].addActionListener//添加按钮添加监听
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					addInfo();//调用addInfo方法
				}
	       }
		);
		jb[1].addActionListener//取消按钮添加监听
		(
			new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						for(int i=1;i<jtf.length;i++)
						{
							jtf[i].setText("");//JTextField置空
						}
					}
		      }
		);
		
		jtf[1].addActionListener//jtf[1]添加监听
		(
		 new ActionListener()
		  {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				jcb[0].requestFocus();//获得焦点
			}
		  }
		);
		jcb[0].addActionListener//jcb[0]添加监听
		(
				 new ActionListener()
				  {
					@Override
					public void actionPerformed(ActionEvent e)
					{
						jcb[1].requestFocus();//获得焦点
					}
				  }
			);
		jtf[2].addActionListener//jtf[2]添加监听
		(
		 new ActionListener()
		  {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				jtf[3].requestFocus();//获得焦点
			}
		  }
		);
		jtf[3].addActionListener//jtf[3]添加监听
		(
				new ActionListener()
				  {
					@Override
					public void actionPerformed(ActionEvent e)
					{
						jb[0].requestFocus();//jb[0]获得焦点
					}
				  }	
		);
		jcb[1].addActionListener//jcb[1]添加监听
		(
				new ActionListener()
				  {
					@Override
					public void actionPerformed(ActionEvent e)
					{
						jtf[2].requestFocus();//jtf[2]获得焦点
					}
				  }	
		);
		this.setBounds(300, 150,480,320);//设置界面的位置与大小
		this.setTitle("添加餐台");//设置界面的标题
		this.setAlwaysOnTop(true);//设置界面总是在顶
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//设置关闭方式
		this.setVisible(true);//设置为可见
		jtf[1].requestFocusInWindow();//jtf[1]获得焦点
  }
  //添加餐台信息方法
  public void addInfo()
    {
	    for(int i=0;i<jtf.length-1;i++)
	    {
			sb.append(Constant.ADD_POINT+jtf[i].getText().toString());//将JTextField的值添加到字符串sb中
		}
		for(int i=0;i<jcb.length;i++)//将JComboBox的值添加到字符串sb中
		{
			sb.append(Constant.ADD_POINT+jcb[i].getSelectedItem().toString());
		}
		SocketClient.ConnectSevert(sb.toString());//将信息传给服务器
		String addbackinfo=SocketClient.readinfo;
		if(addbackinfo.equals("ok"))//如果返回“ok”
		{
			//添加成功后重新显示
			JOptionPane.showMessageDialog(AddPoint.this,"添加餐台成功","消息",JOptionPane.INFORMATION_MESSAGE);
			this.dispose();//关闭当前界面
			SocketClient.ConnectSevert(GET_POINT+curnode.toString());//从服务器获得餐台信息
			String getinfo=SocketClient.readinfo;
			Vector<Vector<String>> data=TypeExchangeUtil.strToVector(getinfo);//重新建表
		    MainUI.createJTable(data,title,20,MainUI.topheight,MainUI.midwidth,MainUI.buttomheight);
			new AddPoint(curnode,mainui);
		}
		else
		{   //否则，出提示信息"添加失败,请检查输入信息"
			JOptionPane.showMessageDialog(AddPoint.this,"添加失败,请检查输入信息","消息",JOptionPane.INFORMATION_MESSAGE);
		}
		for(int i=1;i<jtf.length;i++)//JTextField置空
		{
			jtf[i].setText("");
		}
  }
  public static void main(String args[])//主方法
  {
	  try {//windows风格
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
  }
}
