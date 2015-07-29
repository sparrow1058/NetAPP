package com.bn.vege;
/*添加主类别的界面*/
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
//import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;


import com.bn.constant.Constant;
import com.bn.pcinfo.MainUI;
import com.bn.util.SocketClient;
import com.bn.util.TypeExchangeUtil;

import static com.bn.constant.Constant.*;
import static com.bn.pcinfo.TableListener.title;
//import static com.bn.util.TypeExchangeUtil.*;
public class MainCateGoryUI implements ActionListener
{
    String strinfo;
    MainUI mui;
    public MainCateGoryUI(MainUI mui)
    {
    	this.mui=mui;
    }
	@Override
	public void actionPerformed(ActionEvent e)
	{
		SocketClient.ConnectSevert(GET_MCGMAXNO);//获得菜品主类最大ID
		String index=SocketClient.readinfo;
		System.out.println("index"+index);
		strinfo=((Integer.parseInt(index)+1)<10)?("00"+(Integer.parseInt(index)+1)):
			(((Integer.parseInt(index)+1)<100)?("0"+(Integer.parseInt(index)+1)):((Integer.parseInt(index)+1)+""));
        addMainCateGory addmcg=new addMainCateGory(mui);
        addmcg.setVisible(true);//跳到添加主类界面，并设为可见
	}
   @SuppressWarnings("serial")
class addMainCateGory extends JFrame
{
	   DefaultMutableTreeNode curenode=Constant.vegenode[0];//当前节点为菜品主类
	   MainUI mainui;
	   int width;//设置宽度
	   int height;//设置高度
	   Font font=new Font("宋体",Font.BOLD,14);//设置两种字体
	   Font fonttitle=new Font("宋体",Font.BOLD,20);
	   @SuppressWarnings("rawtypes")
	public addMainCateGory(final MainUI mainui)
	   {
			 JPanel jp=new JPanel();//定义JPanel
			jp.setLayout(null);//设置JPanel布局器为默认
			 this.setBounds(200, 100, 520, 250);//设置界面位置与大小
			 width=540;//设置宽度
			 height=300;//设置高度
			 //左边距
			 int leftwidth=width/20;
			 //高的距离
			 int topheight=height/60;
			 //右边距
			 int rightwidth=width/2;
			 //jlabel的宽度
			 int lwidth=width/2/3;
			 //JLabel的高度
			 int lheight=height/10;
			 //text的宽度
			 int twidth=width/2/2;
			 //text的高度
			 int theight=height/12;
			 //得到左边text左边的边距
			 int tleftwidth=leftwidth+lwidth;
			 //得到右边text左边的边距
			 int trightwidth=rightwidth+lwidth;
			 
			 JLabel jltitle=new JLabel("添加菜品主类");//定义标题
			 jltitle.setFont(fonttitle);//设置字体
			 jltitle.setBounds(width/3, topheight, 2*lwidth, lheight);//设置位置与大小
			 jp.add(jltitle);//将标题添加到JPanel中
				 
		//添加第一行控件
			//添加编号控件
			 JLabel jlid=new JLabel("主类ID：");//定义主类id JLabel
			 jlid.setFont(font);//设置字体
			 jlid.setBounds(leftwidth, 3*topheight+2*lheight, lwidth, lheight);//设置位置与大小
			 jp.add(jlid);//将JLabel添加到JPanel
			 JLabel jlidshow=new JLabel(strinfo);//定义“主类ID”JLabel
			 jlidshow.setFont(font);//设置字体
			 jlidshow.setBounds(tleftwidth, 3*topheight+2*lheight, twidth, lheight);//设置位置与大小
			 jp.add(jlidshow);//将JLabel添加到JPanel中

	       //添加主类名称名称
	         final JLabel jlmcgname=new JLabel("主类名称：");//定义名称JLabel
	         jlmcgname.setFont(font);//设置字体
	         jlmcgname.setBounds(rightwidth,3*topheight+2*lheight,lwidth,lheight);//设置位置与大小
			 jp.add(jlmcgname);//将JLabel添加到JPanel中
	         final JTextField jetcgname=new JTextField();//定义“主类名称”JTextField
	         jetcgname.setBounds(trightwidth, 3*topheight+2*lheight, twidth, theight);//设置位置与大小
	         jp.add(jetcgname);//将JTextField添加到JPanel中
				         				         
	       //添加按钮控件
	         JButton jbok=new JButton("添加");//定义添加按钮
	         jbok.setFont(font);//设置字体
	         jp.add(jbok);//将按钮添加到JPanel中
	         jbok.setBounds(width/2/4,  7*topheight+lheight*5, lwidth, theight);//设置位置与大小
	         jbok.addActionListener//按钮添加监听
	         (
	            new ActionListener()
	            {
					@SuppressWarnings({ "unchecked", "static-access" })
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						if(jetcgname.getText().equals(""))
						{
							JOptionPane.showMessageDialog(addMainCateGory.this, "主类名称不能为空，请输入","提示",JOptionPane.INFORMATION_MESSAGE);
						}
						else 
						{
							String pmcgname=jetcgname.getText().toString();
							String msg= ADD_CMG+strinfo+ ADD_CMG+pmcgname;							
							SocketClient.ConnectSevert(msg);//将主类信息传给服务器
							String readinfo=SocketClient.readinfo;
							if(readinfo.equals("ok"))
							{
								JOptionPane.showMessageDialog(addMainCateGory.this, "入库信息添加成功","提示",JOptionPane.INFORMATION_MESSAGE);
								title=new Vector();//标题
								{
									title.add("主类ID");title.add("名称");

								}
								SocketClient.ConnectSevert(GET_MCG);//获得主类信息
								String getinfo=SocketClient.readinfo;
								if(getinfo.equals("fail"))
								{
		                        	JOptionPane.showMessageDialog(addMainCateGory.this, "获取信息失败","提示",JOptionPane.INFORMATION_MESSAGE);
								}
								else
								{   //重新建表
									Vector<Vector<String>> data=TypeExchangeUtil.strToVector(getinfo);
									int topwidth=(int) (768*0.07);
									int midwidth=(int) ((1024-1024*0.25)*0.78);
									int buttomheight=(int) (768*0.6);
									mainui.createJTable(data,title,20,topwidth,midwidth,buttomheight);
									mainui.createRight(curenode);
								}
								addMainCateGory.this.setVisible(false);//设置添加主类界面不可见
							}
							else
							{
								JOptionPane.showMessageDialog(addMainCateGory.this, "入库信息添加失败","提示",JOptionPane.INFORMATION_MESSAGE);
								addMainCateGory.this.setVisible(false);
							}
						}
					}
	            }
	         );
	         //取消按钮控件
	         JButton jbfali=new JButton("取消");
	         jbfali.setFont(font);//设置字体
	         jp.add(jbfali);//将取消按钮添加到JPanel中
	         jbfali.setBounds(width/2+width/2/4, 7*topheight+lheight*5, lwidth, theight);//设置位置与大小
	         jbfali.addActionListener//按钮添加监听
	         (
	           new ActionListener()
	           {
				@Override
				public void actionPerformed(ActionEvent e)
				{
					jlmcgname.setText("");//主类名称置空
				}
	           }
	         );
				         
	         this.add(jp);//将JPanel添加到界面中
	         this.setVisible(true);//界面设置为可见
	         this.setEnabled(true);//界面设置为可用	         
	         this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//设置关闭方式
	 		 this.setResizable(false);//设置界面尺寸固定
	 		 this.setTitle("添加菜品主类");//设置标题
	 		 Image image=this.getToolkit().getImage("src/com/bn/img/tb1.jpg");//定义标题
			 this.setIconImage(image);//设置图标      
		 }
   }
}
