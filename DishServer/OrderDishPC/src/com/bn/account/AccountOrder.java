package com.bn.account;

import static com.bn.constant.Constant.GET_ORDER;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import com.bn.constant.Constant;
import com.bn.pcinfo.MainUI;
import com.bn.util.SocketClient;
import com.bn.util.TypeExchangeUtil;
public class AccountOrder extends JFrame 
{
	private static final long serialVersionUID = 1L;
	DefaultMutableTreeNode curenode1=Constant.accountnode[0];//结账节点
	Font font=new Font("宋体",Font.BOLD,20);//定义字体
	Vector<String> title=new Vector<String>();//标题
	{
	    title.add("菜品名称");
	    title.add("菜品数量");
	    title.add("菜品价格");
	    title.add("菜品规格");
	}
	public  Vector<Vector<String>> data;//定义向量泛型
	String str[][]=null;//定义空二维数组
	MainUI mainui;//主界面
	Pattern pattern = Pattern.compile("[0-9]*");//指定必须为数字的正则表达式  
	JLabel jl[]=
	  {
		new JLabel("餐台名称")
		,new JLabel("应   收")
		,new JLabel("实    收")
		,new JLabel("找    回")
	  };//JLabel数组
	  JLabel jrl[]=
	  {
			new JLabel("订单"),new JLabel("人数"),
			new JLabel("点菜时间"),new JLabel("服务员号")
	  };//JLabel数组
	  
	  JTextField jtf[]=
	  {
		new JTextField(""),new JTextField("")
		,new JTextField(""),new JTextField("")
	  };//JTextField数组
	  JTextField jtfr[]=
	  {
		new JTextField(""),new JTextField(""),new JTextField(""),new JTextField(""),
	  };//JTextField数组
	  JButton jcount=null;//计算按钮
	  JButton jok=new JButton("确定");//确定按钮
	  JButton jreset=new JButton("取消");//取消按钮
  public AccountOrder(String order,String xxorder,final MainUI mainui)
  {
	  this.mainui=mainui;
	  JLabel jtitle=new JLabel("菜单");//标题为JLabel菜单
	  JSplitPane jspt=new JSplitPane();//设置分隔条
	  JPanel jpl=new JPanel();//定义两个JPanel
	  JPanel jpr=new JPanel();
	  jspt.setLeftComponent(jpl);//设置分隔条与JPanel的相对位置
	  jspt.setRightComponent(jpr);
	  jspt.setDividerSize(15);//设置分隔条尺寸
	  jspt.setDividerLocation(250);//设置分隔条位置	  
	  
	  final JCheckBox isfp=new JCheckBox("是否开发票");//定义JCheckBox
	  isfp.setFont(font);//设置字体
	  isfp.setSelected(false);//设置默认不选中
	  isfp.setBounds(20,260,160, 30);//设置位置与大小
	  JPanel jinfo1=new JPanel();//定义JPanel
	  jinfo1.setBounds(0,0, 250,300);//设置JPanel位置与大小
	  jinfo1.setBorder(BorderFactory.createLineBorder(Color.black));//设置JPanel边框
	  jinfo1.setLayout(null);//设置JPanel布局器为默认
	  jpl.add(jinfo1);//将JPanel添加到jpl中
	  jinfo1.add(isfp);//将JCheckBox添加到JPanel中
	  jcount=new JButton("计算");//定义计算按钮
	  jcount.setBounds(180,260,60,30);//设置按钮位置与大小
	  jinfo1.add(jcount);//将按钮添加到JPanel中
	  jtf[2].requestFocus();//设置焦点
	  //计算找回钱数
	  jcount.addActionListener//计算按钮监听
	  (
		new ActionListener()
		  {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String allmoney=jtf[2].getText().toString();//将jtf[2]的值赋给allmoney
				String zmoney=jtf[1].getText().toString();//将jtf[1]的值赋给zmoney
				System.out.println("allmoney"+allmoney);
				if(allmoney.equals("")||!pattern.matcher(allmoney).matches())//如果钱数格式不匹配，出提示
				{
					JOptionPane.showMessageDialog(AccountOrder.this,"输入正确格式的钱数","消息",JOptionPane.INFORMATION_MESSAGE);
				}
				else if(Float.parseFloat(allmoney)<Float.parseFloat(zmoney))//如果应收钱数大于实收钱数，出提示
				{
					JOptionPane.showMessageDialog
			  	       (
				    		   AccountOrder.this,
				    		   "输入钱数不对",
				    		   "提示",
				    		   JOptionPane.INFORMATION_MESSAGE
			  	    	);
				}
				else
				{   //否则，找回钱数=实收钱数-应收钱数
					jtf[3].setText(Float.parseFloat(allmoney)-Float.parseFloat(zmoney)+"");
				}
			}
		  
	      }
	   );
	  
	  jok.setBounds(20,570,80,40);//确定按钮位置与大小
	  jpl.add(jok);//将确定按钮添加到jpl中
	  jreset.setBounds(140,570,80,40);//取消按钮位置与大小
	  jpl.add(jreset);//将取消按钮添加到jpl中
	  str=TypeExchangeUtil.getString(order);//订单信息
	  for(int i=0;i<4;i++)//通过循环语句，设置JLabel的位置与大小、字体，并添加到jinfo1中
	  {
		  jl[i].setBounds(5,20+i*40,100,30);
		  jinfo1.add(jl[i]);
		  jl[i].setFont(font);
	  }
	  jtf[0].setText(str[0][0]);//设置jtf[0]的值
	  jtf[1].setText(str[0][5]);//设置jtf[1]的值
	  
	  for(int i=0;i<jtf.length;i++)//通过循环语句设置JTextField的位置与大小，并添加到jinfo1中
	  {
		  jtf[i].setBounds(100,20+i*40,120,30);
		  jinfo1.add(jtf[i]);
	  }
	  for(int i=0;i<2;i++)//将JTextField设置为不可编辑
	  {
		  jtf[i].setEditable(false); 
	  }
	  jtf[3].setEditable(false);//将jtf[3]设置为不可编辑
	  for(int i=0;i<jrl.length;i++)//设置JLabel的字体，并添加到jpr中
	  {
		  jrl[i].setFont(new Font("宋体",Font.BOLD,14));
		  jpr.add(jrl[i]);
	  }
	  
	  jtitle.setBounds(280,5,100,40);//设置标题的位置与大小
	  jtitle.setFont(font);//设置标题的字体
	  jpr.add(jtitle);//将标题添加到jpr中
	  jrl[0].setBounds(20,40,60,30);//设置jrl[0]的位置与大小
	  jrl[1].setBounds(140,40,60,30);//设置jrl[1]的位置与大小
	  jrl[2].setBounds(220,40,80,30);//设置jrl[2]的位置与大小
	  jrl[3].setBounds(460,40,80,30);//设置jrl[3]的位置与大小
	  
	  jtfr[0].setBounds(60,40,80,30);//设置jtfr[0]的位置与大小
	  jtfr[1].setBounds(180,40,40,30);//设置jtfr[1]的位置与大小
	  jtfr[2].setBounds(280,40,180,30);//设置jtfr[2]的位置与大小
	  jtfr[3].setBounds(530,40,60,30);//设置jtfr[3]的位置与大小
	  for(int i=0;i<jtfr.length;i++)//将JTextField添加到jpr中，并设置JTextField的值、字体，设置为可编辑
	  {
		  jpr.add(jtfr[i]);
		  jtfr[i].setText(str[0][i+1]);
		  jtfr[i].setEnabled(false);
		  jtfr[i].setFont(new Font("宋体",Font.BOLD,14));
	  }
	  //初始化表内容
	  Vector<Vector<String>> orderinfo=TypeExchangeUtil.strToVector(xxorder);//具体菜品信息
	  DefaultTableModel dtmtable=new DefaultTableModel(orderinfo,title);//定义表的模型	
	  JTable table=new JTable(dtmtable);//建表
	  DefaultTableColumnModel dtcm=(DefaultTableColumnModel) table.getColumnModel();//获得JTable的列
	  dtcm.getColumn(0).setResizable(false);//第一列设置为固定尺寸
	  table.setRowHeight(25);//设置行高为25
	  JScrollPane jscrollp=new JScrollPane(table);//定义滚动条，添加到表中
	  jscrollp.setBounds(20,80,580,450);//设置滚动条的位置与大小
	  jpr.add(jscrollp);//将滚动条添加到jpr中
	  jpl.setLayout(null);//设置jpl的布局器为默认
	  jpr.setLayout(null);//设置jpr的布局器为默认
	  this.add(jspt);//将分隔条添加到主界面
	  this.setEnabled(true);//主界面设置为可见
      this.setBounds(150,50,900,650);//设置主界面的位置与大小		
	  this.setTitle("结账");//设置标题
	  this.setVisible(true);//设置为可用
	  this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//设置关闭方式
	  Image image=this.getToolkit().getImage("src/com/bn/image/tb1.jpg");//定义图片
	  this.setIconImage(image);//设置界面图标
	  //添加焦点
	  jtf[2].requestFocusInWindow();
	jok.addActionListener//确定按钮添加监听
	(
	   new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					String isfpflg;
					//是否开发票
					if(isfp.isSelected())
					{
						isfpflg="1";
					}
					else
					{
						isfpflg="0";
					}
					//订单id
					String info=Constant.INSERT_ACCOUNT_VEGE+str[0][1];//订单
					//获取钱数信息
					for(int i=0;i<jtf.length;i++)
					{
						String infot=jtf[i].getText().toString();
						if(infot.equals(""))
						{
							infot="0";
						}
						info=info+Constant.INSERT_ACCOUNT_VEGE+infot;
					}
					info=info+Constant.INSERT_ACCOUNT_VEGE+Constant.operator;//操作人
					info=info+Constant.INSERT_ACCOUNT_VEGE+isfpflg;//打发票标示
					String allmoney=jtf[2].getText().toString();//将jtf[2]的值赋给allmoney										
					if(allmoney.equals("")||!pattern.matcher(allmoney).matches())//如果钱数格式不匹配，出提示
					{
						JOptionPane.showMessageDialog(AccountOrder.this,"请输入数值钱数","消息",JOptionPane.INFORMATION_MESSAGE);
					}
					else
					{   //将信息传给服务器
						SocketClient.ConnectSevert(info);
						if(SocketClient.readinfo.equals("ok"))//如果返回“ok”,出提示"保存成功"
						{
							JOptionPane.showMessageDialog
							(
									AccountOrder.this,
									"保存成功",
									"消息",
									JOptionPane.INFORMATION_MESSAGE
							);
						    AccountOrder.this.dispose();//结账界面关闭
						    
						}
						else
						{
							JOptionPane.showMessageDialog//否则出提示"请检查信息"
							(
									AccountOrder.this,
									"请检查信息",
									"消息",
									JOptionPane.INFORMATION_MESSAGE
							);
						}
						SocketClient.ConnectSevert(GET_ORDER);//从服务器获取订单信息
						String getinfo=SocketClient.readinfo;
						Vector<String>title=new Vector<String>();//标题
						{
							title.add("餐台名称");title.add("订单号");title.add("顾客人数");
							title.add("订菜时间");title.add("服务员id");title.add("总价格");
						}
						if(getinfo.equals("fail"))//如果返回“fail”,返回提示信息"获取信息失败"
						{
							JOptionPane.showMessageDialog
	                  	       (
			       	    		   AccountOrder.this,
			       	    		   "获取信息失败",
			       	    		   "提示",
			       	    		   JOptionPane.INFORMATION_MESSAGE
	                  	    	);
						}
						else{
							if(getinfo.length()==0)
							{
								JOptionPane.showMessageDialog//如果信息为空，出提示"信息为空"
		                  	       (
		                  	    	   AccountOrder.this,
				       	    		   "信息为空",
				       	    		   "提示",
				       	    		   JOptionPane.INFORMATION_MESSAGE
		                  	    	);
						     }
							//将数据、标题传给createJTable，建表
						    data=TypeExchangeUtil.strToVector(getinfo);
							MainUI.createJTable(data,title,20,MainUI.topheight+80,MainUI.midwidth+100,MainUI.buttomheight);
							mainui.createRight(curenode1);
						}
					}
				}
			}
		);
	  }
}