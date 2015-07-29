package com.bn.pcinfo;

import static com.bn.pcinfo.TableListener.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import com.bn.order.GetConfirmOrder;
import com.bn.point.AddPoint;
import com.bn.point.UpdatePoint;
import com.bn.util.SocketClient;
import com.bn.util.TypeExchangeUtil;
import com.bn.vege.AddVegeUI;
import com.bn.vege.CateGoryUI;
import com.bn.vege.MainCateGoryUI;
import com.bn.vege.UpdateVege;
import com.bn.vege.VegeInfo;
import com.bn.worker.AddWorkerInfo;
import com.bn.worker.EditWorkerInfo;
import com.bn.worker.QueryWorkerInfo;
import com.bn.worker.WorkerInfoTransform;
import static com.bn.constant.Constant.*;
import com.bn.account.AccountOrder;
import com.bn.constant.Constant;
/*
 * 搭建主界面左侧为JTree,右侧根据选中的节点进行初始化，有对应的删除内部类，更新内部类，添加内部类，用于具体的按钮的增删改操作
 * 右侧数据显示有JTable显示 给JTable 添加监听
 * */
@SuppressWarnings("serial")
public class MainUI extends JFrame 
{
	Font font=new Font("宋体",Font.ITALIC,13);
	Font f=new Font("宋体",Font.BOLD,18);
	Font fonttitle=new Font("宋体",Font.BOLD,20);//自定义几种字体格式
	public int screenwidth;//定义屏幕宽度
	public int screenheight;//定义屏幕高度
	public static int leftwidth;
	public static int midwidth;
	public static int rightwidth;
	public static int topheight;
	public static int buttomheight;
	public  Vector<Vector<String>> data;//定义向量泛型来存放数据
	public  DefaultMutableTreeNode curnode;
	public static  JTable jtable;
	private static String fail="fail";
	static JSplitPane jspOuter=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);//定义分割面板为水平分割
	DefaultTreeModel dtm=new DefaultTreeModel(temproot);//根节点
	JTree jt=new JTree(dtm);//建立树
	DefaultMutableTreeNode[] roomnode=null;//定义两个树的节点
	DefaultMutableTreeNode pointnode[]=null;
	JScrollPane jsp=new JScrollPane(jt);//定义滚动条
	static JPanel jp=new JPanel();
    public JCheckBox jct[];//定义复选框
    //定义删除、更新、添加监听
	DelButtonListener dellistener=new DelButtonListener();
	 UpdateButtonListener updatelistener=new UpdateButtonListener();
	 AddButtonListener addlistener=new AddButtonListener();
	 
     JLabel jladd=null;
     JTextField jtfadd=null;
	 public JCheckBox jCheckBox;
//搭建主界面方法
	public MainUI()
	{
		getScreen(); //调用getScreen()方法
		CreateTree();//调用Create()方法
	    MainUIListener tl=new  MainUIListener();//定义监听方法
	    //给节点添加图标
		jt.setCellRenderer
		 (
				 new DefaultTreeCellRenderer()
				 {
					 public Icon getClosedIcon() 
					 {
                    //返回你需要的图标
						 Icon myIcon=new ImageIcon("src/com/bn/image/304.png");
						 return myIcon;                                                                                                                                   
					 }
					 public Icon getOpenIcon()
					 {
						 Icon myIcon=new ImageIcon("src/com/bn/image/304.png");
						 return myIcon;                                         
					 }
					 public Icon getLeafIcon()
					 {
						 Icon myIcon=new ImageIcon("src/com/bn/image/13.png");
						 return myIcon;      
					 }
				 }
		);
	    jt.addTreeSelectionListener(tl);//给树添加监听方法
		jt.setEditable(true);//设置树为可编辑
		jt.setEnabled(false);//设置树为不可用
		jt.setShowsRootHandles(true);//设置树显示节点句柄
		jspOuter.setLeftComponent(jsp);//将滚动条设置到分割条的左边
		jspOuter.setRightComponent(jp);//将Jpanel设置到分割条的右边
		//登陆界面
		final JPanel jplogin=new JPanel();//定义登录Jpanel
		jplogin.setBounds(150,100,400,300);//设置JPanel的位置及大小
		jplogin.setLayout(null);//设置此容器的布局管理器为空
		jp.setLayout(null);
		JLabel jtitle=new JLabel("员工登陆");//设置标题为“员工登陆”
		JLabel jllogin[]={new JLabel("编号："),new JLabel("密码：")};//定义JLabel数组，包括编号、密码
		final JTextField jtflogin=new JTextField("");//定义编号输入框
		final JPasswordField jtfpw=new JPasswordField("");//定义密码输入框
		jtitle.setFont(new Font("宋体",Font.BOLD,24));//定义标题的字体
		jplogin.add(jtitle);//将标题添加到登陆Jpanel中
		jtitle.setBounds(150,30,200, 40);//设置标题的位置与大小
		for(int i=0;i<jllogin.length;i++)//用循环语句将编号，密码添加到登陆Jpanel中
		{
			jllogin[i].setBounds(80,80+i*60,80,40);
			jllogin[i].setFont(fonttitle);
			jplogin.add(jllogin[i]);
		}
	     //获取焦点
		jtflogin.setBounds(160,80,120,40);//设置编号输入框的位置与大小
		jtflogin.setFont(font);//设置编号的字体
		jplogin.add(jtflogin);//将编号输入框添加到登陆Jpanel中
		jtfpw.setBounds(160,140,120,40);//设置密码输入框的位置与大小
		jtfpw.setFont(font);//设置密码的字体
		jplogin.add(jtfpw);//将密码输入框添加到登陆Jpanel中
		final JButton jok=new JButton("确定");//定义“确定”按钮
		JButton jreset=new JButton("取消");//定义“取消”按钮
		jok.setBounds(100,220,80,40);//设置“确定”按钮的位置与大小
		jreset.setBounds(200,220,80,40);//设置“取消”按钮的位置与大小
		jplogin.add(jok);//将“确定”按钮添加到登陆Jpanel中
		jplogin.add(jreset);//将“取消”按钮添加到登陆Jpanel中
		jplogin.setBorder(BorderFactory.createLineBorder(Color.black));//设置登陆Jpanel的边框
		jp.add(jplogin);//将登陆Jpanel添加到主界面Jpanel中
		jtflogin.addActionListener//给编号输入框添加监听
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					jtfpw.requestFocus();//获得焦点
				}
		    }
		);
		jtfpw.addActionListener//给密码输入框添加监听
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					jok.requestFocus();//获得焦点
				}
		    }
		);
		
		//给登陆按钮添加监听
		jok.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					String userid=jtflogin.getText().toString();//将编号输入框的值赋给userid
					@SuppressWarnings("deprecation")//表示不检测过期的方法
					String userpw=jtfpw.getText().toString();//将密码输入框的值赋给userpw
					System.out.println("tttt"+userid+userpw);
					//通过调用SocketClient的ConnectSevert方法，将输入框中的用户编号与密码传到服务器
					SocketClient.ConnectSevert(Constant.SURE_WAITER+userid+Constant.SURE_WAITER+userpw);
					if(SocketClient.readinfo.equals("ok"))//如果返回“ok”
					 {
						int i=0;
						SocketClient.ConnectSevert(Constant.GET_LOGINAUTH+userid);//将用户编号传给服务器
						//通过用户编号获得此用户的权限信息
						String authoryinfo[][]=TypeExchangeUtil.getString(SocketClient.readinfo);
						for(i=0;i<authoryinfo.length;i++)
						{
							   if(authoryinfo[i][0].equals("登陆"))//如果权限中有“登陆”权限
							   {
								jt.setEnabled(true);//将树设置为可用
								jplogin.setVisible(false);//使登陆Jpanel不可见
								JOptionPane.showMessageDialog//出现提示信息“登陆成功”
								(
									MainUI.this,
									"登陆成功",
									"提示",
									JOptionPane.INFORMATION_MESSAGE
								);
								//查看今天是否有预定餐台把所有预定餐台置为忙
								String todaytime=TypeExchangeUtil.gettime();//取得当前的时间
								String times[]=todaytime.split(" ");
								for(int j=0;j<times.length;j++)
								{
									System.out.println("time="+times[j]);
								}
								SocketClient.ConnectSevert(UPDATE_TODAY_POINTSTATE+times[0]);
								//记录操作人
								Constant.operator=userid;
								break;
							   }
						 }
						 if(i>=authoryinfo.length)//如果权限中无“登陆”
						 {
							 JOptionPane.showMessageDialog//出现提示信息“无权限登陆”
								(
									MainUI.this,
									"您无登陆权限",
									"提示",
									JOptionPane.INFORMATION_MESSAGE
								);
							 jtflogin.setText("");//将编号输入框置空
							 jtfpw.setText("");//将密码输入框置空
							 jtflogin.requestFocusInWindow();//登陆输入框获得焦点
						 }
					 }
					 else
					 {
						 JOptionPane.showMessageDialog//否则出现提示信息，"请您输入正确的用户和密码"
							(
								MainUI.this,
								"请您输入正确的用户和密码",
								"提示",
								JOptionPane.INFORMATION_MESSAGE
							);
						 jtflogin.setText("");//将编号输入框置空
						 jtfpw.setText("");//将密码输入框置空
						 jtflogin.requestFocusInWindow();//登陆输入框获得焦点
					 }
				}
			}
		);
		jreset.addActionListener//取消按钮的监听
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					jtflogin.setText("");//将编号输入框置空
					jtfpw.setText("");//将密码输入框置空
				}
			}
		);
		//结束
		jspOuter.setDividerSize(15);//设置分隔条大小为15
		jspOuter.setDividerLocation(200);//设置分隔条位置
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);//全屏
		Image image=this.getToolkit().getImage("src/com/bn/image/tb1.jpg");//定义图片
		this.setIconImage(image);//设置图标为image
		this.add(jspOuter);//将分割条添加进来
		this.setEnabled(true);//主界面设置为可用
        this.setBounds(0, 0,1024,768);//设置主界面位置与大小		
		this.setTitle("点菜系统");//设置标题
		this.setVisible(true);//设置为可见
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭方式
		this.addWindowStateListener//主界面添加监听
		(
			new WindowAdapter()
			{
			   public  void windowStateChanged(WindowEvent e)
			   {
				getScreen();//调用getScreen()方法
			  }
		    }
		);
	}
	public void getScreen()//设置各种尺寸
	{
	   screenwidth=1024;
	   screenheight=768;
		System.out.println("ScreenWidth"+screenwidth+"screenheight"+screenheight);
	    leftwidth=(int) (screenwidth*0.25);
	    midwidth=(int)((screenwidth-leftwidth)*0.99);
	    topheight=(int) (screenheight*0.07);
	    buttomheight=(int)(screenheight*0.6);
	}
	//生成树
	public void CreateTree()
	{
		for(int i=0;i<dmtn.length;i++)
		{
			dtm.insertNodeInto(dmtn[i], temproot, i);//将dtm[]中的节点添加到temproot根节点下面
		}
		for(int i=0;i<vegenode.length;i++)//dmtn[0]添加菜品节点
		{ 
			dmtn[0].add(vegenode[i]);
		}
		for(int i=0;i<roompointnode.length;i++)//dmtn[1]添加餐厅节点
		{
			dmtn[1].add(roompointnode[i]);
		}
		for(int i=0;i<ordernode.length;i++)//dmtn[3]添加订单节点
		{
			dmtn[3].add(ordernode[i]);
		}
		
		//员工管理
		for(int i=0;i<authority.length;i++)//dmtn[2]添加员工节点
		{
			dmtn[2].add(authority[i]);
		}
		for(int i=0;i<accountnode.length;i++)//dmtn[4]添加结账节点
		{
			dmtn[4].add(accountnode[i]);
		}
		//获取所有餐厅名称
		SocketClient.ConnectSevert(GET_RMNAME);
		String roomname=SocketClient.readinfo;
		if(roomname.equals(SOCKET_ERROR))//检查网络连接情况
		{
			JOptionPane.showMessageDialog
			(
				MainUI.this,
				"服务器连接超时，请查看网络情况。。。",
				"提示",
				JOptionPane.INFORMATION_MESSAGE
			);
			System.exit(0);
		}
		else if(roomname.equals(SOCKET_IOERROR))
		{
			JOptionPane.showMessageDialog
			(
				MainUI.this,
				"读取数据异常，请查看网络情况。。。",
				"提示",
				JOptionPane.INFORMATION_MESSAGE
			);
		}
		else
		{
			String info[]=TypeExchangeUtil.getStringInfo(roomname, 0);//获得餐厅信息
			roomnode=new DefaultMutableTreeNode[info.length];//定义餐厅节点
			for(int i=0;i<info.length;i++)
			{//餐厅添加子节点
				roomnode[i]=new DefaultMutableTreeNode(info[i]);
				roompointnode[0].add(roomnode[i]);
			}
		}
		
	} 
	@SuppressWarnings("rawtypes")
	public void FitTableColumns(JTable myTable) {               //设置table的列宽随内容调整
		
        JTableHeader header = myTable.getTableHeader();
        int rowCount = myTable.getRowCount();
        Enumeration columns = myTable.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(
                    column.getIdentifier());
            int width = (int) myTable.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(myTable,
                            column.getIdentifier(), false, false, -1, col)
                    .getPreferredSize().getWidth();
            for (int row = 0; row < rowCount; row++){
                int preferedWidth = (int) myTable.getCellRenderer(row, col)
                        .getTableCellRendererComponent(myTable,
                                myTable.getValueAt(row, col), false, false,
                                row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            header.setResizingColumn(column);
            column.setWidth(width + myTable.getIntercellSpacing().width+6);
        }
    }

	//生成右侧的JTable
		public static void createJTable(Vector<Vector<String>> list,
				final Vector<String> title,int x,int y,int width,int height)
		{
			primarykey=null;//定义主键为空
			jp.setLayout(null);//定义JPanel布局器为默认FlowLayout
	        jp.removeAll();//从JPanel中移除所有组件
			DefaultTableModel dtmtable=new DefaultTableModel(list,title)//建立表的模型
			{
				@Override  
				public boolean isCellEditable(int row,int column)//如果 row 和 column 位置的单元格是可编辑的，则返回 true
				{  
				    if(column == 0)
				    {  
					    return false;  
				    }
				    return true;
		        } 
			};
			//建立JTable
			jtable=new JTable(dtmtable){
				//用来设置单击JTable中的单元格时将其变换为可编辑状态
				public void changeSelection(int rowIndex, int columnIndex,
			                boolean toggle, boolean extend)
				{
	                super.changeSelection(rowIndex, columnIndex, false, false);
	                super.editCellAt(rowIndex, columnIndex, null);
	                this.requestFocus();
				}
			};
			jtable.setRowHeight(25);//设置表的单元格高度为25像素
			jtable.requestFocus();//获得输入焦点
			DefaultTableColumnModel dtcm=(DefaultTableColumnModel) jtable.getColumnModel();//创建表列模型
			dtcm.getColumn(0).setResizable(false);//设置第一列尺寸不可变
			JScrollPane jspt=new JScrollPane(jtable);//将表中加入滚动条
			jspt.setBounds(x,y,midwidth,height);//设置滚动条的位置与大小
			jp.add(jspt);//将滚动条添加到Jpanel中
		}
	//根据所点的节点显示对应的内容
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void createRight( DefaultMutableTreeNode curenode)
	{
        JButton jb[]=null;
        //根据节点不同显示内容不同
        if(curenode.toString().equals("计量单位管理"))
		{
			jb=new JButton[3];
			//为JTable创建排序器
			final TableRowSorter sorter = new TableRowSorter(jtable.getModel());  
			jtable.setRowSorter(sorter);
			jb[0]=new JButton("添加计量单位");
			jb[1]=new JButton("删除计量单位");
			jb[2]=new JButton("保存计量单位");
		}else if(curenode.toString().equals("菜系管理"))
		{
			jb=new JButton[3];
			//为JTable创建排序器
			final TableRowSorter sorter = new TableRowSorter(jtable.getModel());  
			jtable.setRowSorter(sorter);
			jb[0]=new JButton("添加菜系");
			jb[1]=new JButton("删除菜系");
			jb[2]=new JButton("保存菜系");
		}else if(curenode.toString().equals("主类管理"))
		{
			jb=new JButton[3];
			jb[0]=new JButton("添加主类");
			jb[1]=new JButton("删除主类");
			jb[2]=new JButton("保存主类");
		}
		else if(curenode.toString().equals("类别管理"))
		{
			jb=new JButton[3];
			jb[0]=new JButton("添加类别");
			jb[1]=new JButton("删除类别");
			jb[2]=new JButton("保存类别");
		}else if(curenode.toString().equals("菜品管理"))
		{
			jb=new JButton[4];
			jb[0]=new JButton("添加菜品");
			jb[1]=new JButton("删除菜品");
			jb[2]=new JButton("修改菜品");
//			jb[3]=new JButton("查询菜品");
			jb[3]=new JButton("菜品详情");
		}else if(curenode.toString().equals("规格管理"))
		{
			jb=new JButton[3];
			//为JTable创建排序器
			final TableRowSorter sorter = new TableRowSorter(jtable.getModel());  
			jtable.setRowSorter(sorter);
			jb[0]=new JButton("添加规格");
			jb[1]=new JButton("删除规格");
			jb[2]=new JButton("保存规格");
		}
		else if(curenode.toString().equals("餐厅信息"))
		{	
		    jb=new JButton[4];
			jb[0]=new JButton("");
			jb[1]=new JButton("");
			jb[2]=new JButton("");
			jb[3]=new JButton("");	
			jb[0].setVisible(false);
			jb[1].setVisible(false);
			jb[2].setVisible(false);
			jb[3].setVisible(false);			
		} 
		else if(curenode.getParent().toString().equals("餐厅信息"))
		{
			 jb=new JButton[3];
				jb[0]=new JButton("添加餐台");
				jb[1]=new JButton("删除餐台");
				jb[2]=new JButton("修改餐台");
				
		}
       /////////////////////单独每一项的功能////////////////////
		//单独订单信息的设定  
         	if(curenode.toString().equals("主类管理"))//如果当前节点是“主类管理”
            {
            	MainCateGoryUI mcui=new MainCateGoryUI(this);//声明mcui
            	//为JTable创建排序器
            	final TableRowSorter sorter = new TableRowSorter(jtable.getModel());  
    			jtable.setRowSorter(sorter);
    			//为三个按钮添加监听
            	jb[0].addActionListener(mcui);//添加操作            	
    			jb[1].addActionListener(dellistener);//删除操作    	    	
    	    	jb[2].addActionListener(updatelistener);//更新操作
            }else if(curenode.toString().equals("类别管理"))//如果当前节点是“类别管理”
            {
            	CateGoryUI cui=new CateGoryUI(this);//声明cui
            	//为JTable创建排序器
            	final TableRowSorter sorter = new TableRowSorter(jtable.getModel());  
    			jtable.setRowSorter(sorter);
    			//为三个按钮添加监听
            	jb[0].addActionListener(cui);//添加操作            	
    			jb[1].addActionListener(dellistener);//删除操作    	    	
    	    	jb[2].addActionListener(updatelistener);//更新操作
            }
            else if(curenode.toString().equals("菜品管理"))//如果当前节点是“菜品管理”
            {
            	//先判断是否存在没有菜品图片的图片如果存在不进行任何操作如果不存在则插入一张没有菜品图片的图片
            	SocketClient.ConnectSevert(ISHAVENULLPIC);
				String readinfo=SocketClient.readinfo;
            	if(readinfo.equals(fail))
            	{
            		//加载当前没有菜品图片的图片
            		File file=new File(IMAGENULL_PATH);
                	byte[] imagemsg=TypeExchangeUtil.filetobyte(file);
                	SocketClient.ConnectSevertBYTE(UPLOAD_IMAGE,UPLOAD_MIMAGE, imagemsg);
                	String imagep=SocketClient.readinfo;
                	String[] mid=imagep.split("#");
                	vegenullBigimage=mid[0];
                	vegenullimage=mid[1];
            	}
            	else
            	{
            		String[] str=readinfo.split("#");
            		vegenullBigimage=str[0];
            		vegenullimage=str[1];
            	}
            	jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//当设置表时，表的自动调整模式关
            	//为JTable创建排序器
            	final TableRowSorter sorter = new TableRowSorter(jtable.getModel());  
    			jtable.setRowSorter(sorter);
            	AddVegeUI addvui=new AddVegeUI(this);//声明addvui
            	//为四个按钮添加监听
            	jb[0].addActionListener(addvui);//添加操作
            	jb[1].addActionListener(dellistener);//删除操作
    			jb[2].addActionListener//修改菜品信息
    			(
    					new ActionListener()
    					{
    						@Override
    						public void actionPerformed(ActionEvent e) 
    						{
    							String[] value=TableListener.vegemsg;//获得菜品信息
    							String pk=TableListener.primarykey;//获得菜品主键
    							if(pk==null)//如果为空，则出提示信息“请选择所需修改的菜品”
    							{
    								JOptionPane.showMessageDialog
    								(
    										MainUI.this,
    										"请选择所需修改的菜品",
    										"提示",
    										JOptionPane.INFORMATION_MESSAGE
    										);
    							}
    							else
    							{    //否则就调用UpdataVege方法，更新菜品信息
    								UpdateVege vmi=new UpdateVege(MainUI.this,value);
    								vmi.setVisible(true);
    							}
    						}		
    					}
    			 );
    			jb[3].addActionListener//获取菜品信息
    			(
    			  new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) 
					{
						String[] value=TableListener.vegemsg;//获得菜品信息
						String pk=TableListener.primarykey;//获取菜品主键
						if(pk==null)//如果为空，则出提示信息“请选择所需查看的菜品”
						{
							JOptionPane.showMessageDialog
							(
									MainUI.this,
									"请选择所需查看的菜品",
									"提示",
									JOptionPane.INFORMATION_MESSAGE
									);
						}
						else
						{
							VegeInfo vi=new VegeInfo(value);//否则就显示菜品详细信息
							vi.setVisible(true);
						}
					}
    			  }
    			);
            }
/****************************************************订单管理begin*******************************************************************/
           else if(curenode.toString().equals("订单信息"))//如果当前节点是“订单信息”
           {
        	    JPanel jpOrderInfo=new JPanel();//定义订单信息JPanel
        	    jpOrderInfo.setLayout(null);//订单信息JPanel布局器为默认FlowLayout
    			jladd=new JLabel("输入餐台号：");//定义JLabel
    			jladd.setFont(f);//定义JLabel的字体
    			jtfadd=new JTextField("");//定义输入框
    			final JButton jsearch=new JButton("查找订单");//定义“查找订单”按钮
    			JButton jsearchxx=new JButton("查看详细订单");//定义“查看详细订单”按钮
    			jsearch.setFont(font);//设置“查找订单”按钮的字体    			
    			jsearchxx.setFont(font);//设置“查找详细订单”按钮的字体
    			jladd.setBounds(60,20,120,35);//设置JLbel的位置与大小
    			jtfadd.setBounds(180,20,150,35);//设置输入框的位置与大小
    			jsearch.setBounds(360,20,100,35);//设置“查找订单”按钮的位置与大小
    			jsearchxx.setBounds(500,20,140,35);//设置“查看详细订单”按钮的位置与大小
    			//将JLbel、输入框、“查找订单”按钮、“查看详细订单”按钮添加到订单信息JPanel中
    			jpOrderInfo.add(jladd);jpOrderInfo.add(jtfadd);jpOrderInfo.add(jsearch);jpOrderInfo.add(jsearchxx);
    			jpOrderInfo.setVisible(true);//将订单信息JPanel设置为可见
    			//设置订单信息JPanel的边框
    			jpOrderInfo.setBorder(BorderFactory.createLineBorder(Color.black));
    			jpOrderInfo.setBounds(20,60,midwidth,60);//设置订单信息JPanel的位置与大小
    			jp.add(jpOrderInfo);//将订单信息JPanel添加到主界面JPanel中
    			//按enter让查询按钮获取焦点
    			jtfadd.addActionListener
    			(
    				new ActionListener()
    					{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								jsearch.requestFocus();//“查找订单”按钮获得焦点
							}
    			        }
    			);
    		jsearch.addActionListener//“查找订单”按钮添加监听
    			(
					new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e) 
						{
							 String pointid=jtfadd.getText();//将输入框中的值赋给pointid
							 if(pointid.equals(""))//如果为空,出提示信息“请输入餐台号”
							 {
								 JOptionPane.showMessageDialog
		                 	       (
		                 	    		   MainUI.this,
		                 	    		   "请输入餐台号",
		                 	    		   "提示",
		                 	    		   JOptionPane.INFORMATION_MESSAGE
		                 	    	);
							 }
							 else
							 {   //否则，就将餐台号传给服务器，并获得此餐台的订单信息
								 SocketClient.ConnectSevert(SEARCH_ORDER+pointid);
								 String list=SocketClient.readinfo;
								title=new Vector();//定义标题
								{
									title.add("餐台");title.add("订单号");title.add("顾客人数");
									title.add("订菜时间");title.add("服务员id");title.add("总价格");
								}
								if(list.length()==0)//如果订单信息为空，出提示信息“此餐桌暂无订单”
								{
									JOptionPane.showMessageDialog
			                 	       (
		                 	    		   MainUI.this,
		                 	    		   "此餐桌暂无订单",
		                 	    		   "提示",
		                 	    		   JOptionPane.INFORMATION_MESSAGE
			                 	    	);
								    jtfadd.setText("");//将输入框置空
								}
								else
								{  //否则就将订单的信息与标题传给createJTable方法，建表
								   data=TypeExchangeUtil.strToVector(list);
								   createJTable(data,title,20,topheight+80,midwidth+100,buttomheight);
								   createRight(curnode);
								}
							 }
    					}
    			     }
    			);
    		jsearchxx.addActionListener//“查看详细订单”按钮监听
    		(
    			new ActionListener()
    			{
    				@Override
    			    public void actionPerformed(ActionEvent e)
    				{
						String pointid=jtfadd.getText();//获得餐桌号
						 if(pointid.equals(""))//如果餐桌号为空
						 {
							 if(primarykey!=null)//如果主键不为空，餐桌号就等于主键
							 {
								 pointid=primarykey;
							 }
							 else
							 {     //否则出提示信息"请选择中行或者输入餐台号"
								   JOptionPane.showMessageDialog
		                 	       (
	                 	    		   MainUI.this,
	                 	    		   "请选择中行或者输入餐台号",
	                 	    		   "提示",
	                 	    		   JOptionPane.INFORMATION_MESSAGE
		                 	    	);
    						 }
    					 }
    					if(pointid.length()!=0) //如果餐桌号不为空
    					{   //将餐桌号传给服务器，查询订单
    						SocketClient.ConnectSevert(SEARCH_ACCORDER+pointid);
    					    String order=SocketClient.readinfo;//得到订单
    					    System.out.println("---------order="+order);
    					    if(order.length()!=0)//如果订单不为空
    						{   //根据订单查询订单详情
    							SocketClient.ConnectSevert(SEARCH_ODXX+pointid);
    							String list=SocketClient.readinfo;//得到订单详情
    							try 
    							{//windows风格
    								UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    							} 
    							catch (Exception e1)
    							{
    								e1.printStackTrace();
    							}
    						    new GetConfirmOrder(order,list);//将订单、订单详情作为参数传给GetConfirmOrder()
    						}
    						else//否则出提示信息"此餐桌暂无订单"
    						{
    							JOptionPane.showMessageDialog
    	                 	       (
	                 	    		   MainUI.this,
	                 	    		   "此餐桌暂无订单",
	                 	    		   "提示",
	                 	    		   JOptionPane.INFORMATION_MESSAGE
    	                 	    	);
    						    jtfadd.setText("");//将输入框置空
    						}
    					}
    				}
    			}
    		);
    	   }
         else if(curenode.toString().equals("结账"))//如果当前节点是结账
            {
    			//jp.updateUI();
        	    JPanel jpOrderInfo=new JPanel();//定义订单信息Jpanel
        	    jpOrderInfo.setLayout(null);//订单信息Jpanel布局器为默认FlowLayout
    			jladd=new JLabel("输入餐台号：");//定义Jpanel
    			jladd.setFont(f);//设置Jpanel的字体
    			jtfadd=new JTextField("");//定义输入框
    			final JButton jsearch=new JButton("结账");//定义“结账”按钮
    			jladd.setBounds(60,20,120,35);//设置Jpanel的位置与大小
    			jtfadd.setBounds(180,20,150,35);//设置输入框的位置与大小
    			jsearch.setBounds(360,20,80,35);//设置“结账”按钮的位置与大小
    			//将JLbel、输入框、“结账”按钮添加到订单信息Jpanel中
    			jpOrderInfo.add(jladd);jpOrderInfo.add(jtfadd);jpOrderInfo.add(jsearch);
    			jtfadd.requestFocusInWindow();//输入框获得焦点
    			jpOrderInfo.setVisible(true);//将订单信息Jpanel设置为可见
    			jpOrderInfo.setBorder(BorderFactory.createLineBorder(Color.black));//设置Jpanel的边框
    			jpOrderInfo.setBounds(20,60,midwidth,60);//设置订单信息Jpanel的位置与大小
    			jp.add(jpOrderInfo);//将订单信息Jpanel添加到主界面Jpanel中
    			jtfadd.addActionListener//按enter让查询按钮获取焦点
    			(
    				new ActionListener()
    				{
						@Override
						public void actionPerformed(ActionEvent e) 
						{
							jsearch.requestFocus();//“结账”按钮获得焦点
						}
    			     }
    			);
    			jsearch.addActionListener//给“结账”按钮添加监听
    			(
					new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e) 
						{
							String pointid=jtfadd.getText();//获得餐台号
							 if(pointid.equals(""))//如果为空
							 {
								 if(primarykey!=null)//如果主键不为空
								 {
									 pointid=primarykey;//餐台号就等于主键
								 }
								 else
								 {     //否则就出提示信息"请选择中行或者输入餐台号"
									   JOptionPane.showMessageDialog
			                 	       (
		                 	    		   MainUI.this,
		                 	    		   "请选择中行或者输入餐台号",
		                 	    		   "提示",
		                 	    		   JOptionPane.INFORMATION_MESSAGE
			                 	    	);
	    						 }
	    					 }
	    					if(pointid.length()!=0) //如果餐台号不为空
	    					{
	    						//订单信息
	    						SocketClient.ConnectSevert(SEARCH_ORDER+pointid);
	    					    String order=SocketClient.readinfo;
	    						if(order.length()!=0)
	    						{//具体菜品信息
	    							SocketClient.ConnectSevert(SEARCH_ACCOUNT_VEGE+pointid);
	    							String list=SocketClient.readinfo;
	    							try 
	    							{//windows风格
	    								UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    							} 
	    							catch (Exception e1) 
	    							{
	    								e1.printStackTrace();
	    							}
	    						   new AccountOrder(order,list,MainUI.this);//启动支付	
	    						}
	    						else
	    						{  //否则出提示信息"此餐桌暂无订单"
	    						   JOptionPane.showMessageDialog
	    	                 	    (
		                 	    		   MainUI.this,
		                 	    		   "此餐桌暂无订单",
		                 	    		   "提示",
		                 	    		   JOptionPane.INFORMATION_MESSAGE
	    	                 	    );
	    						    jtfadd.setText("");//输入框置空
	    						}
							 }
    					}
    			     }
    			);
    			
            }
/****************************************************订单end*******************************************************************/


            else if(curenode.toString().equals("员工管理"))//如果当前节点为“员工管理”
            {
            	int contentFont=14;
    			jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//当调整表的大小时，表的自动调整模式关
    			//为JTable创建排序器
    			final TableRowSorter sorter = new TableRowSorter(jtable.getModel());  
    			jtable.setRowSorter(sorter);
    			
    			jb=new JButton[4];
    			//为以下四个按钮添加监听
    			jb[0] = new JButton("添加员工");//添加
    			jb[0].addActionListener(new ActionListener()
    			{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						new AddWorkerInfo(MainUI.this,title);//启动添加员工信息界面
					}
    			});
    			jb[1] = new JButton("删除员工");//删除
    			jb[1].addActionListener(dellistener);
    			jb[2] = new JButton("编辑员工");//编辑
    			jb[2].addActionListener( new ActionListener()
    			{
					@Override
					public void actionPerformed(ActionEvent arg0) 
					{
						String pk = TableListener.primarykey;//获得主键
						if(pk==null)//如果为空，出提示信息"请选择所需修改的员工信息"
						{   
							JOptionPane.showMessageDialog
	                 	       (
	                 	    		   MainUI.this,
	                 	    		   "请选择所需修改的员工信息",
	                 	    		   "提示",
	                 	    		   JOptionPane.INFORMATION_MESSAGE
	                 	    	);
						}
						else
						{   //否则，将主键传给服务器，通过主键查询员工信息
							SocketClient.ConnectSevert(QUERY_W_ONEOFWORKERINFOBYID+pk);
							String getinfo=SocketClient.readinfo;//获得员工信息
							System.out.println("QUERY_W_ONEOFWORKERINFOBYID---"+getinfo);
							if(getinfo!=null && getinfo.length()!=0)//如果员工信息不为空
							{
								String val[] = getinfo.split("#");//获得员工信息的数组
								for(int i=0; i<val.length; i++)
								{
									if(val[i].equals("null"))
									{
										val[i]="";
									}
									System.out.println(val[i]+" ");
								}
								EditWorkerInfo ewi = new EditWorkerInfo(MainUI.this,title);
								ewi.editWorker(val);//启动编辑员工信息界面
							}
							else
							{   //否则出提示信息"获取信息失败"
								JOptionPane.showMessageDialog
		                  	       (
				       	    		   MainUI.this,
				       	    		   "获取信息失败",
				       	    		   "提示",
				       	    		   JOptionPane.INFORMATION_MESSAGE
		                  	    	);
							}
						}
					}    				
    			});
    			jb[3] = new JButton("查看全部");
    			jb[3].addActionListener//获取全部员工信息
    			(
    				new ActionListener()
    				{
					 @Override
					 public void actionPerformed(ActionEvent arg0) 
					  { //通过服务器获取所有员工信息
						SocketClient.ConnectSevert(GET_WORKERINFO);
						String getinfo=SocketClient.readinfo;
						JOptionPane.showMessageDialog//提示信息"获取信息成功"
              	       (
              	    		   MainUI.this,
              	    		   "获取信息成功",
              	    		   "提示",
              	    		   JOptionPane.INFORMATION_MESSAGE
              	    	);
						if(getinfo.equals("fail"))//如果返回“fail”,出提示信息"获取信息失败"
						{
							JOptionPane.showMessageDialog
	                 	       (
	                 	    		   MainUI.this,
	                 	    		   "获取信息失败",
	                 	    		   "提示",
	                 	    		   JOptionPane.INFORMATION_MESSAGE
	                 	    	);
						}
						else
						{    //否则将员工信息与标题传给createJTable()来建表
							 data = WorkerInfoTransform.Transform(getinfo);
							 createJTable(data,title,20,250,midwidth,400);
							 createRight(curnode);
						}
					}
    				
    			});    			
                JPanel jpQuery = new JPanel();//查询JPanel
    			jpQuery.setLayout(null);  //JPanel的默认布局控制器是FlowLayout
    			jpQuery.setBorder(BorderFactory.createLineBorder(Color.black));  //为JPanel设置线边框
    			jpQuery.setBounds(20,90,midwidth,115);//设置查询Jpanel的位置与大小
    			jp.add(jpQuery);//将查询Jpanel添加到主界面Jpanel中
    			JLabel jlTitle = new JLabel("查询：");//定义JLabel
    			jlTitle.setBounds(10,5,40,20);//设置JLabel的位置与大小
    			jlTitle.setFont(new Font("宋体",Font.TRUETYPE_FONT,13));//设置JLabel的字体
    			jpQuery.add(jlTitle);//将JLabel添加到查询Jpanel中
    			JLabel jlWorkerInfo[] = 
    				{
    					new JLabel("员工id：")
    					,new JLabel("员工姓名：")
    					,new JLabel("角色：")
    				};
    			//通过WorkerInfoTransform方法获得主键
    			List<String[][]> FKList = WorkerInfoTransform.getFKName();
    			String roleArr[][] = FKList.get(0);//获得角色数组
    			String roleName[] = new String[roleArr.length];//建立roleName数组
    			for(int i=0; i<roleArr.length; i++){
    				roleName[i] = roleArr[i][1]; //将角色名称赋给roleName数组
    			}
    			//员工id
    			jlWorkerInfo[0].setBounds(25,30,70,20);//设置员工id JLabel的位置与大小
    			jlWorkerInfo[0].setFont(new Font("宋体",Font.TRUETYPE_FONT,contentFont));//设置字体
    			jpQuery.add(jlWorkerInfo[0]);//将员工id JLbel添加到查询JPanel中
    			final JTextField tfWId = new JTextField();//定义JTextField
    			tfWId.setBounds(100,30,70,20);  //默认情况差不多一个字符占七个像素
    			jpQuery.add(tfWId);//将输入框添加到查询JPanel中
    			//员工姓名
    			jlWorkerInfo[1].setBounds(25,55,70,20);//设置员工姓名JLbel的位置与大小
    			jlWorkerInfo[1].setFont(new Font("宋体",Font.TRUETYPE_FONT,contentFont));//设置字体
    			jpQuery.add(jlWorkerInfo[1]);//将JLabel添加到查询JPanel中
    			final JTextField tfWName = new JTextField();//定义员工姓名输入框
    			tfWName.setBounds(100,55,70,20);  //默认情况差不多一个字符占七个像素
    			jpQuery.add(tfWName);//将员工姓名输入框添加到查询JPanel中
    			//角色
    			jlWorkerInfo[2].setBounds(25,80,70,20);//设置员工角色JLbel的位置与大小
    			jlWorkerInfo[2].setFont(new Font("宋体",Font.TRUETYPE_FONT,contentFont));//设置字体
    			jpQuery.add(jlWorkerInfo[2]);//将员工角色JLbel添加到查询JPanel中
    			final JComboBox jRole = new JComboBox(roleName);//定义员工角色JComboBox
    			jRole.setBounds(100,80,80,25);  //默认情况差不多一个字符占七个像素
    			jpQuery.add(jRole);//将JComboBox添加到查询JPanel中
    			//查询按钮
    			JButton jQue = new JButton("查询");
    			jQue.setBounds(420,77,60,30);//设置按钮的位置与大小
    			jQue.setFont(new Font("宋体",Font.TRUETYPE_FONT,contentFont));//设置字体
    			jpQuery.add(jQue);//将按钮添加到查询JPanel中
    			//按钮添加监听
    			jQue.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) 
					{
						Map<String,String> map = new HashMap<String,String>();//建立map
						if(tfWId.getText().length()!=0){
							map.put("tfWId", tfWId.getText());
						}//如果员工id不为空，将员工id添加到map中
						if(tfWName.getText().length()!=0){
							map.put("tfWName", tfWName.getText());
						}//如果员工姓名不为空，将员工姓名添加到map中
						if(!jRole.getSelectedItem().toString().equals("---")){
							map.put("JRole",jRole.getSelectedItem().toString() );
						}//如果员工角色不为“---”，将员工角色添加到map中
						new QueryWorkerInfo(sorter,map);//调用QueryWorkerInfo查询员工
					}    				
    			});
    		}
    		else
    		{
	            //给表添加监听
    	        jb[0].addActionListener(addlistener);//添加操作    	    	
    	        jb[1].addActionListener(dellistener);//删除操作
    	        jb[2].addActionListener(updatelistener);//更新操作       
    		}
            JPanel toolsJP=new JPanel();//定义JPanel
    		toolsJP.setLayout(null);//设置JPanel的布局器为默认Flowlayout
    		toolsJP.setBorder(BorderFactory.createLineBorder(Color.black));  //为JPanel设置线边框
    		toolsJP.setBounds(20,10,midwidth,40);//设置JPanel的位置与大小
    		JLabel jl=new JLabel();//定义JLabel
     	    jl.setText(curenode.toString());//设置JLabel的值为当前节点的值
     		jl.setFont(fonttitle);//设置JLabel的字体
     		jl.setBounds(10,5,200,30);//设置JLabel的位置与大小
     		jl.setVisible(true);//设置JLabel为可见
     		toolsJP.add(jl);//将JLabel添加到JPanel
    		jp.add(toolsJP);//将JPanel添加到主界面JPanel中
    		if(jb!=null)
    		{
	    		for(int i=0;i<jb.length ;i++)//通过循环语句设置三个按钮的字体、位置大小，并添加到JPanel中
	    		{
	    			jb[jb.length-1-i].setFont(font);
	    			jb[jb.length-1-i].setBounds(midwidth-(35+80)*(i+1),3,110,35);
	    			toolsJP.add(jb[jb.length-1-i]);
	    		}
    		}
            jspOuter.setDividerSize(1);	//设置分隔条尺寸为1
     		jp.setVisible(true);//设置主界面JPanel为可见
            jp.updateUI();//更新界面
            if(jtfadd!=null)
            {
            	jtfadd.requestFocusInWindow();//获得焦点
            }
            //设置JTable根据内容的长度调整列宽
            FitTableColumns(jtable);
			//为JTable设置绘制器
			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();   
			//JTable中的内容居中显示
			renderer.setHorizontalAlignment(JLabel.CENTER);
			jtable.setDefaultRenderer(Object.class,renderer);//JTable的渲染器
			//JTable中的表头居中显示
			((DefaultTableCellRenderer)jtable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
          //给表添加监听
			TableListener tablelistener=new TableListener(jtable);
			jtable.getSelectionModel().addListSelectionListener(tablelistener);
			jtable.getModel().addTableModelListener(tablelistener);					
	}
	//树监听（根据点击的节点来调用右侧的布局）
	 private class MainUIListener implements TreeSelectionListener
	 {
		   private TreePath tp;
			@SuppressWarnings({ "rawtypes", "unchecked" })//压制警告
			@Override
			public void valueChanged(TreeSelectionEvent e) 
			{
				tp=e.getNewLeadSelectionPath();//返回当前前导路径
				if(tp!=null)
				{//获取选中的节点
					curnode=(DefaultMutableTreeNode) tp.getLastPathComponent();
					//如果是主类
					if(curnode.equals(Constant.vegenode[0]))
					{   //通过服务器获得主类
						SocketClient.ConnectSevert(GET_MCG);
						String getinfo=SocketClient.readinfo;
						title=new Vector();//主类表的标题
						{
							title.add("主类ID");title.add("名称");

						}
                        if(getinfo.equals("fail"))
                        {   //如果返回“fail”，出提示信息"获取信息失败"
                        	JOptionPane.showMessageDialog
                   	       (
                   	    		   MainUI.this,
                   	    		   "获取信息失败",
                   	    		   "提示",
                   	    		   JOptionPane.INFORMATION_MESSAGE
                   	    	);
						}
                        else
                        {    //否则将数据、标题传给createJTable，建表
							 data=TypeExchangeUtil.strToVector(getinfo);
							 createJTable(data,title,20,topheight,midwidth,buttomheight);
							 createRight(curnode);
						}
					}//如果是菜品类别
					else if(curnode.equals(Constant.vegenode[1]))
					{   //通过服务器获得菜品类别
						SocketClient.ConnectSevert(GET_CG);
						String getinfo=SocketClient.readinfo;
						title=new Vector();//标题
						{
							title.add("类别ID");
							title.add("名称");
							title.add("创建时间");
							title.add("最后编辑时间");
                            title.add("所属主类");
						}
                        if(getinfo.equals("fail"))//如果返回“fail”，则出提示信息"获取信息失败"
                        {
                        	JOptionPane.showMessageDialog
                   	       (
                   	    		   MainUI.this,
                   	    		   "获取信息失败",
                   	    		   "提示",
                   	    		   JOptionPane.INFORMATION_MESSAGE
                   	    	);
						}
                        else
                        {    //否则，将数据、标题传给createJTable，建表
							 data=TypeExchangeUtil.strToVector(getinfo);
							 createJTable(data,title,20,topheight,midwidth,buttomheight);
							 createRight(curnode);
						}
					}//如果是菜系
					else if(curnode.equals(Constant.vegenode[2])){
						SocketClient.ConnectSevert(GET_VT);//通过服务器获得菜系
						String getinfo=SocketClient.readinfo;
						title=new Vector();//标题
						{
							title.add("菜系编号");title.add("名称");
						}
                        if(getinfo.equals("fail"))//如果返回“fail”，出提示信息"获取信息失败"
                        {
                        	JOptionPane.showMessageDialog
                   	       (
                   	    		   MainUI.this,
                   	    		   "获取信息失败",
                   	    		   "提示",
                   	    		   JOptionPane.INFORMATION_MESSAGE
                   	    	);
						}
                        else
                        {    //否则将数据、标题传给createJTable，建表
							 data=TypeExchangeUtil.strToVector(getinfo);
							 createJTable(data,title,20,topheight,midwidth,buttomheight);
							 createRight(curnode);
						}
					}//如果是计量单位
					else if(curnode.equals(Constant.vegenode[3]))
					{   //通过服务器获得计量单位
						SocketClient.ConnectSevert(GET_CU);
						String getinfo=SocketClient.readinfo;
						title=new Vector();//标题
						{
							title.add("计量单位编号");title.add("名称");title.add("备注");

						}
                        if(getinfo.equals("fail"))//如果返回“fail”，则出提示信息"获取信息失败"
                        {
                        	JOptionPane.showMessageDialog
                   	       (
                   	    		   MainUI.this,
                   	    		   "获取信息失败",
                   	    		   "提示",
                   	    		   JOptionPane.INFORMATION_MESSAGE
                   	    	);
                        }
                        else
                        {    //否则将数据、标题传给createJTable，建表
							 data=TypeExchangeUtil.strToVector(getinfo);
							 createJTable(data,title,20,topheight,midwidth,buttomheight);
							 createRight(curnode);
						}
					}//如果是规格
					else if(curnode.equals(Constant.vegenode[4]))
					{   //通过服务器获得规格
						SocketClient.ConnectSevert(GET_VS);
						String getinfo=SocketClient.readinfo;
						title=new Vector();//标题
						{
							title.add("规格编号");title.add("名称");
						}
                        if(getinfo.equals("fail"))//如果返回“fail”，出提示信息"获取信息失败"
                        {
                        	JOptionPane.showMessageDialog
                   	       (
                   	    		   MainUI.this,
                   	    		   "获取信息失败",
                   	    		   "提示",
                   	    		   JOptionPane.INFORMATION_MESSAGE
                   	    	);
                        }
                        else
                        {    //否则将数据、标题传给createJTable，建表
							 data=TypeExchangeUtil.strToVector(getinfo);
							 createJTable(data,title,20,topheight,midwidth,buttomheight);
							 createRight(curnode);
						}
					}					
					//如果是菜品
					else if(curnode.equals(Constant.vegenode[5]))
					{   //通过服务器获得菜品信息
						SocketClient.ConnectSevert(GET_USEVEGE);
						String getinfo=SocketClient.readinfo;
						title=new Vector();//标题
						{
							title.add("菜品ID");
							title.add("菜品名称");
							title.add("价格");
							title.add("计量单位");
							title.add("菜品类别");
							title.add("菜品主类");
							title.add("系别");
							title.add("规格");
							title.add("介绍");
						}
                        if(getinfo.equals("fail"))//如果返回“fail”，出提示信息"获取信息失败"
                        {
                        	JOptionPane.showMessageDialog
                   	       (
                   	    		   MainUI.this,
                   	    		   "获取信息失败",
                   	    		   "提示",
                   	    		   JOptionPane.INFORMATION_MESSAGE
                   	    	);
						}
                        else
						{    //否则将数据、标题传给createJTable，建表
							 data=TypeExchangeUtil.strToVector(getinfo);
							 createJTable(data,title,20,topheight,midwidth,buttomheight);
							 createRight(curnode);
						}
					}//如果是餐厅
					else if(curnode.equals(roompointnode[0]))
					{   //通过服务器获得餐厅信息
						SocketClient.ConnectSevert(GET_ROOM);
						String getinfo=SocketClient.readinfo;
						title=new Vector();//标题
						{
							title.add("餐厅编号");title.add("餐厅名称");
						}
						if(getinfo.equals("fail"))//如果返回“fail”，出提示信息"获取信息失败"
						{
							JOptionPane.showMessageDialog
	                  	       (
	                  	    		   MainUI.this,
	                  	    		   "获取信息失败",
	                  	    		   "提示",
	                  	    		   JOptionPane.INFORMATION_MESSAGE
	                  	    	);
						}
						else
						{   //否则将数据、标题传给createJTable，建表
							data=TypeExchangeUtil.strToVector(getinfo);
							createJTable(data,title,20,topheight+10,midwidth,buttomheight+100);
							createRight(curnode);
						}
					}//如果是餐台
					else if(curnode.getParent().equals(roompointnode[0]))
					{   
						curnode.removeAllChildren();//情况当前节点的子节点
						SocketClient.ConnectSevert(GET_POINT+curnode.toString());//通过服务器获得当前节点的信息
						String getinfo=SocketClient.readinfo;
						title=new Vector();//标题
						{
							title.add("餐台号");title.add("餐台名称");title.add("类型");
							title.add("是否停用");title.add("容纳人数");title.add("当前状态");title.add("所在餐厅");
						}
						if(getinfo.equals("fail"))//如果返回“fail”，出提示信息"获取信息失败"
						{
							JOptionPane.showMessageDialog
	                  	       (
                  	    		   MainUI.this,
                  	    		   "获取信息失败",
                  	    		   "提示",
                  	    		   JOptionPane.INFORMATION_MESSAGE
	                  	    	);
						}
						else
						{//给餐厅添加具体餐台节点
							if(getinfo.length()!=0){
							String str[]=TypeExchangeUtil.getStringInfo(getinfo, 1);
							 pointnode=new DefaultMutableTreeNode[str.length];//得到餐厅子节点个数
								for(int i=0;i<str.length;i++)
								{   //将子节点添加到餐厅节点下
									pointnode[i]=new DefaultMutableTreeNode(str[i]);
									curnode.add(pointnode[i]);
								}
						}
						else
						{   //否则出提示信息"获取信息失败"
							JOptionPane.showMessageDialog
	                  	       (
                  	    		   MainUI.this,
                  	    		   "获取信息失败",
                  	    		   "提示",
                  	    		   JOptionPane.INFORMATION_MESSAGE
	                  	    	);
						}//然后将数据、标题传给createJTable，建表
						data=TypeExchangeUtil.strToVector(getinfo);
						createJTable(data,title,20,topheight,midwidth,buttomheight+100);
						createRight(curnode);
						}
					}
                    //如果是未结账的订单信息
					else if(curnode.equals(ordernode[0]))
					{   //通过服务器获得账单信息
						SocketClient.ConnectSevert(GET_ORDER);
						String getinfo=SocketClient.readinfo;
						title=new Vector();//标题
						{
							title.add("餐台名称");title.add("订单号");title.add("顾客人数");
							title.add("订菜时间");title.add("服务员id");title.add("总价格");
						}
						if(getinfo.equals("fail"))//如果返回“fail”，出提示信息"获取信息失败"
						{
							JOptionPane.showMessageDialog
	                  	       (
			       	    		   MainUI.this,
			       	    		   "获取信息失败",
			       	    		   "提示",
			       	    		   JOptionPane.INFORMATION_MESSAGE
	                  	    	);
						}
						else{
							if(getinfo.length()==0)//如果信息为空，则出提示信息"信息为空"
							{
								JOptionPane.showMessageDialog
		                  	       (
				       	    		   MainUI.this,
				       	    		   "信息为空",
				       	    		   "提示",
				       	    		   JOptionPane.INFORMATION_MESSAGE
		                  	    	);
						     }//将数据、标题传给createJTable，建表
							data=TypeExchangeUtil.strToVector(getinfo);
							createJTable(data,title,20,topheight+80,midwidth+100,buttomheight);
							createRight(curnode);
						}
					}
					//如果是员工信息
					else if(curnode.equals(Constant.authority[0]))
					{  //定义Table的表头和表中的数据
						SocketClient.ConnectSevert(GET_WORKERINFO);
						String getinfo=SocketClient.readinfo;
						title = new Vector();  //定义Jtable的标题向量
						{
							for(int i=0; i<workerPrivateInfo.length; i++)
							{
								title.add(workerPrivateInfo[i]);
							}
						}
						if(getinfo.equals("fail"))//如果返回“fail”，出提示信息"获取信息失败"
						{
							JOptionPane.showMessageDialog
	                  	       (
			       	    		   MainUI.this,
			       	    		   "获取信息失败",
			       	    		   "提示",
			       	    		   JOptionPane.INFORMATION_MESSAGE
	                  	    	);
						}
						else
						{   //否则将数据、标题传给createJTable，建表
							data = WorkerInfoTransform.Transform(getinfo);
							createJTable(data,title,20,250,midwidth,400);
							createRight(curnode);
						}
					}
					else if(curnode.equals(Constant.accountnode[0]))
					{   //通过服务器获得账单信息
						SocketClient.ConnectSevert(GET_ORDER);
						String getinfo=SocketClient.readinfo;
						title=new Vector();//标题
						{
							title.add("餐台名称");title.add("订单号");title.add("顾客人数");
							title.add("订菜时间");title.add("服务员id");title.add("总价格");
						}
						if(getinfo.equals("fail"))//如果返回“fail”，出提示信息"获取信息失败"
						{
							JOptionPane.showMessageDialog
	                  	       (
			       	    		   MainUI.this,
			       	    		   "获取信息失败",
			       	    		   "提示",
			       	    		   JOptionPane.INFORMATION_MESSAGE
	                  	    	);
						}
						else{
							if(getinfo.length()==0)//如果信息为空，出提示信息"信息为空"
							{
								JOptionPane.showMessageDialog
		                  	       (
				       	    		   MainUI.this,
				       	    		   "信息为空",
				       	    		   "提示",
				       	    		   JOptionPane.INFORMATION_MESSAGE
		                  	    	);
						     }//将数据、标题传给createJTable，建表
							data=TypeExchangeUtil.strToVector(getinfo);
							createJTable(data,title,20,topheight+80,midwidth+100,buttomheight);
							createRight(curnode);
						}
					}				
				}
			}
		}
	//内部类删除按钮的监听
	 private class DelButtonListener implements ActionListener
	 {
		 String getinfo=null;
		 //点击按钮后根据删除标记和得到标记来更新数据
	      public void getBackInfo(String dels,String gets)
	      {//主键为空则未选中行
	    	  if(primarykey==null)
	    	  {
	    		  JOptionPane.showMessageDialog//出提示信息"请选中删除行"
         	       (
      	    		   MainUI.this,
      	    		   "请选中删除行",
      	    		   "提示",
      	    		   JOptionPane.INFORMATION_MESSAGE
         	    	);
			    }
	    	  else
	    	  {//提示是否删除
	    	    String backinfo = "";
	            int index=JOptionPane.showConfirmDialog//出提示信息框，是否删除
	            (
	            		MainUI.this,
			           "确认删除"+primarykey,
			           "提示",
			           JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE
			    );
		        if(index==0)//如果是0则执行
		          {    	 
		    	    SocketClient.ConnectSevert(dels+primarykey);//进行删除
		    	    backinfo=SocketClient.readinfo;
					if(backinfo.equals("ok"))
					{
						SocketClient.ConnectSevert(gets);
						getinfo=SocketClient.readinfo;
						if(getinfo.equals("fail"))//如果返回“fail”，出提示信息"获取信息失败"
						{
							JOptionPane.showMessageDialog
	               	       (
			       	    		   MainUI.this,
			       	    		   "获取信息失败",
			       	    		   "提示",
			       	    		   JOptionPane.INFORMATION_MESSAGE
	               	    	);
						}
						else
						{
							data=TypeExchangeUtil.strToVector(getinfo);//将获得的信息转化为向量泛型
						}
					}
					//把TABle的行置0和主键置空
					row=0;
					primarykey=null;
		        }
		     }
	      }
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JButton b=(JButton) e.getSource();
				if(b.getText().equals("删除计量单位"))
				{
					getBackInfo(DEL_CU,GET_CU);//调用getBackInfo()根据删除标记和得到标记来更新数据
					createJTable(data,title,20,topheight,midwidth,buttomheight);
					createRight(curnode);
				}
				else if(b.getText().equals("删除菜系"))
				{
					getBackInfo(DEL_VT,GET_VT);//调用getBackInfo()根据删除标记和得到标记来更新数据
					createJTable(data,title,20,topheight,midwidth,buttomheight);
					createRight(curnode);
				}
				else if(b.getText().equals("删除主类"))
				{
					getBackInfo(DEL_CMG,GET_MCG);//调用getBackInfo()根据删除标记和得到标记来更新数据
					createJTable(data,title,20,topheight,midwidth,buttomheight);
					createRight(curnode);
				}
				else if(b.getText().equals("删除类别"))
				{
					getBackInfo(DEL_CG,GET_CG);//调用getBackInfo()根据删除标记和得到标记来更新数据
					createJTable(data,title,20,topheight,midwidth,buttomheight);
					createRight(curnode);
				}
				else if(b.getText().equals("删除规格"))
				{
					getBackInfo(DEL_VS,GET_VS);//调用getBackInfo()根据删除标记和得到标记来更新数据
					createJTable(data,title,20,topheight,midwidth,buttomheight);
					createRight(curnode);
				}
				else if(b.getText().equals("删除菜品"))
				{
					getBackInfo(DEL_VEGE,GET_VEGE);//调用getBackInfo()根据删除标记和得到标记来更新数据
					createJTable(data,title,20,topheight,midwidth,buttomheight);
					createRight(curnode);
				}
				else if(b.getText().equals("删除餐台"))
				{
					getBackInfo(DEL_POINT,GET_POINT+curnode.toString());//调用getBackInfo()根据删除标记和得到标记来更新数据
					createJTable(data,title,20,topheight,midwidth,buttomheight+100);
					createRight(curnode);
					
				}
			    else if(b.getText().equals("删除员工"))
			    {
					getBackInfo(DEL_WORKERINFO,GET_WORKERINFO);//调用getBackInfo()根据删除标记和得到标记来更新数据
					createJTable(data,title,20,250,midwidth,400);
					createRight(curnode);
				}
			}
		}
	//更新按钮的监听
	 private class UpdateButtonListener implements ActionListener
	 {
    //更新操作
		public void getBackInfo(String s, String gets)
		{
    	 StringBuffer info=new StringBuffer();//定义字符串info
			for(int i=0;i<values.length;i++)//将获得的值添加到info中
			{
			  info.append(s+values[i]);
			}
			SocketClient.ConnectSevert(info.toString());//将info传给服务器
			String backinfo=SocketClient.readinfo;
			if(backinfo.equals("ok"))//如果返回“ok”
			{
				SocketClient.ConnectSevert(gets);//更新信息
				String getinfo=SocketClient.readinfo;
				JOptionPane.showMessageDialog//出提示信息"保存成功"
       	       (
    	    		   MainUI.this,
    	    		   "保存成功",
    	    		   "提示",
    	    		   JOptionPane.INFORMATION_MESSAGE
       	    	);
				if(getinfo.equals("fail"))//如果返回“fail”，出提示信息"获取信息失败"
				{
					JOptionPane.showMessageDialog
           	       (
	       	    		MainUI.this,
	       	    		"获取信息失败",
	       	    		"提示",
	       	    		JOptionPane.INFORMATION_MESSAGE
           	    	);
				}
				else
				{    //将数据、标题传给createJTable，建表
					 data=TypeExchangeUtil.strToVector(getinfo);
					 createJTable(data,title,20,topheight,midwidth,buttomheight);
					 createRight(curnode);
				}
			}
			else if(backinfo.equals("fail"))//如果返回“fail”
			{
				JOptionPane.showMessageDialog//出提示信息"保存失败"
       	       (
    	    		   MainUI.this,
    	    		   "保存失败",
    	    		   "提示",
    	    		   JOptionPane.INFORMATION_MESSAGE
       	    	);
			}
			//把TABle的行置0和主键置空
			row=0;
			primarykey=null;
     }
		@Override
		public void actionPerformed(ActionEvent e)
		{
	       JButton b=(JButton) e.getSource();
		   if(primarykey==null)//如果主键为空，出提示信息"请选中修改行"
		   {
			  JOptionPane.showMessageDialog
    	       (
 	    		   MainUI.this,
 	    		   "请选中修改行",
 	    		   "提示",
 	    		   JOptionPane.INFORMATION_MESSAGE
    	    	);
		    }
		 else
		 {
			if(b.getText().equals("保存计量单位"))
			{
				getBackInfo(UPDATE_CU, GET_CU);//调用getBackInfo方法，更新数据
				createJTable(data,title,20,topheight,midwidth,buttomheight);
			      createRight(curnode);
			}else if(b.getText().equals("保存菜系"))
			{
				getBackInfo(UPDATE_VT, GET_VT);//调用getBackInfo方法，更新数据
				createJTable(data,title,20,topheight,midwidth,buttomheight);
			      createRight(curnode);
			}else if(b.getText().equals("保存主类"))
			{
				getBackInfo(UPDATE_CMG, GET_MCG);//调用getBackInfo方法，更新数据
				createJTable(data,title,20,topheight,midwidth,buttomheight);
			      createRight(curnode);
			}else if(b.getText().equals("保存类别"))
			{
				getBackInfo(UPDATE_CG, GET_CG);//调用getBackInfo方法，更新数据
				createJTable(data,title,20,topheight,midwidth,buttomheight);
			      createRight(curnode);
			}else if(b.getText().equals("保存规格"))
			{
				getBackInfo(UPDATE_VS, GET_VS);//调用getBackInfo方法，更新数据
				createJTable(data,title,20,topheight,midwidth,buttomheight);
			      createRight(curnode);
			}
			else if(b.getText().equals("修改餐台"))
			{ 
			      new UpdatePoint(curnode,MainUI.this);//启用UpdatePoint方法，更新餐台
		    }
			}
		}
	}
	 //添加按钮的监听
	 private class AddButtonListener implements ActionListener
	 {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JButton b=(JButton) e.getSource();
				if(b.getText().equals("添加计量单位"))
				{
					getBackInfo(GET_CUMAXNO);//调用getBackInfo方法，添加信息
				}else if(b.getText().equals("添加菜系"))
				{
					getBackInfo(GET_VTMAXNO);//调用getBackInfo方法，添加信息
				}else if(b.getText().equals("添加规格"))
				{
					getBackInfo(GET_VSMAXNO);//调用getBackInfo方法，添加信息
				}

				else if(b.getText().equals("添加类别"))
				{
					getBackInfo(GET_CGMAXNO);//调用getBackInfo方法，添加信息
				}
				else if(b.getText().equals("添加餐台"))
				{
					 try 
					 {//windows风格
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					 } 
					 catch (Exception e1) 
					 {
						e1.printStackTrace();
					 }
					new AddPoint(curnode,MainUI.this);//启用AddPiont方法，添加餐台
				}
			}
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void getBackInfo(String s)//添加信息方法
			{
				DefaultTableModel dtm=(DefaultTableModel) jtable.getModel();
				Vector newvc=new Vector();
				if(s.equals(GET_AUTHROLEMAXNO))//添加员工信息
				{
					newvc.add("");
					newvc.add("");
				}
				else
				{
					SocketClient.ConnectSevert(s);//通过服务器获得信息
					String index=SocketClient.readinfo;
					System.out.println("index="+index);
					String in=((Integer.parseInt(index)+1)<10)?("00"+(Integer.parseInt(index)+1)):
									(((Integer.parseInt(index)+1)<100)?
											("0"+(Integer.parseInt(index)+1)):((Integer.parseInt(index)+1)+""));
					
					if(s.equals(GET_SA_MAXNO)||s.equals(GET_VSMAXNO))//添加规格信息
					{
						newvc.add(String.valueOf(in));
						newvc.add("");	
					}
					else if(s.equals(GET_AUTHORITYMAXNO) || s.equals(GET_ROLEMAXNO))//添加角色信息
					{	
						newvc.add(in);
						newvc.add("");
					}
					else 
					{
						newvc.add(String.valueOf(in));//添加其他信息
						newvc.add("");
						newvc.add("暂无");
					}
				}
			    dtm.getDataVector().add(newvc);//将新信息添加到表中
			    ((DefaultTableModel)jtable.getModel()).fireTableStructureChanged();//通知所有监听器，表的结构已更改
			    //设置JTable根据内容的长度调整列宽
	            FitTableColumns(jtable);
	            //设置新添加的行的第1列为编辑状态
	            jtable.changeSelection(jtable.getRowCount()-1,1,false,false);
			    row=0;
				primarykey="0";
			}
		}
	 public static void main(String[] args)//主方法
	 {
		try 
		{//windows风格
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		new MainUI();
	}
}